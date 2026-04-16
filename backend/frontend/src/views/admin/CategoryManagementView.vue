<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Taxonomy</p>
      <h1 class="page-title">分类管理</h1>
      <p class="page-subtitle">维护分类信息、封面图和排序，供专辑归类展示使用。</p>
    </section>

    <section class="ui-panel">
      <div class="toolbar toolbar-between">
        <div class="meta-row">
          <span class="badge-soft">{{ categories.length }} 个分类</span>
          <span class="badge-neutral">用户侧不展示编码</span>
        </div>
        <el-button type="primary" @click="openCreate">新建分类</el-button>
      </div>

      <div v-if="!categories.length" class="empty-shell">
        <el-empty description="暂无分类，先创建一个分类" />
      </div>

      <div v-else class="table-shell">
        <el-table :data="categories">
          <el-table-column label="封面" width="108">
            <template #default="scope">
              <div class="table-cover-shell">
                <img v-if="scope.row.coverUrl" :src="scope.row.coverUrl" alt="cover" class="table-cover-image" />
                <div v-else class="table-cover-placeholder">无封面</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称" min-width="180" />
          <el-table-column label="描述" min-width="220">
            <template #default="scope">
              <span class="muted">{{ scope.row.description || '暂无描述' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="sortOrder" label="排序" width="90" />
          <el-table-column label="状态" width="120">
            <template #default="scope">
              <span :class="statusBadgeClass(scope.row.status)">{{ scope.row.status }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button text @click="openEdit(scope.row)">编辑</el-button>
              <el-button text type="danger" @click="remove(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>

    <el-dialog v-model="visible" :title="editingId ? '编辑分类' : '新建分类'" width="640px">
      <el-form :model="form" label-width="90px" class="form-stack">
        <el-form-item label="封面图">
          <ImageUploadField v-model="form.coverUrl" />
        </el-form-item>
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="例如：前端开发" />
        </el-form-item>
        <el-form-item label="内部编码">
          <el-input v-model="form.code" placeholder="仅后台内部使用" />
        </el-form-item>
        <el-form-item label="分类描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="补充分类说明" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" class="full-width">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="停用" value="DISABLED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminCategoryApi } from '../../api/modules'
import ImageUploadField from '../../components/upload/ImageUploadField.vue'
import type { Category } from '../../types'

const categories = ref<Category[]>([])
const visible = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<CategoryForm>({
  name: '',
  code: '',
  description: '',
  coverUrl: '',
  sortOrder: 0,
  status: 'ACTIVE',
})

interface CategoryForm {
  name: string
  code: string
  description: string
  coverUrl: string
  sortOrder: number
  status: string
}

async function load() {
  const response = await adminCategoryApi.list()
  categories.value = response.data
}

function resetForm() {
  Object.assign(form, {
    name: '',
    code: '',
    description: '',
    coverUrl: '',
    sortOrder: 0,
    status: 'ACTIVE',
  })
}

function openCreate() {
  editingId.value = null
  resetForm()
  visible.value = true
}

function openEdit(category: Category) {
  editingId.value = category.id
  Object.assign(form, {
    name: category.name,
    code: category.code || '',
    description: category.description || '',
    coverUrl: category.coverUrl || '',
    sortOrder: category.sortOrder ?? 0,
    status: category.status || 'ACTIVE',
  })
  visible.value = true
}

async function submit() {
  const payload = {
    name: form.name,
    code: form.code,
    description: form.description,
    coverUrl: form.coverUrl || undefined,
    sortOrder: form.sortOrder,
    status: form.status,
  }

  if (editingId.value) {
    await adminCategoryApi.update(editingId.value, payload)
  } else {
    await adminCategoryApi.create(payload)
  }
  visible.value = false
  ElMessage.success('保存成功')
  await load()
}

async function remove(id: number) {
  await ElMessageBox.confirm('删除后该分类将不可恢复，是否继续？', '删除分类', { type: 'warning' })
  await adminCategoryApi.remove(id)
  ElMessage.success('删除成功')
  await load()
}

function statusBadgeClass(status: string) {
  return status === 'ACTIVE' ? 'badge-success' : 'badge-warning'
}

onMounted(load)
</script>
