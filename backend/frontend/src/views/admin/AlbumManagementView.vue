<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Albums</p>
      <h1 class="page-title">专辑管理</h1>
      <p class="page-subtitle">按分类聚合展示专辑，用封面卡片维护每个分类下的内容集合。</p>
    </section>

    <section class="ui-panel">
      <div class="toolbar toolbar-between">
        <div class="meta-row">
          <span class="badge-soft">{{ albums.length }} 个专辑</span>
          <span class="badge-neutral">按分类聚合</span>
        </div>
        <el-button type="primary" @click="openCreate">新建专辑</el-button>
      </div>

      <div v-if="!categoryGroups.length" class="empty-shell">
        <el-empty description="暂无专辑，先创建一个专辑" />
      </div>

      <div v-else class="album-group-stack">
        <section v-for="group in categoryGroups" :key="group.category.id" class="album-group-block">
          <div class="summary-row album-group-head">
            <div class="album-group-copy">
              <h2 class="section-title">{{ group.category.name }}</h2>
              <p class="card-copy">{{ group.category.description || '该分类下的专辑内容集合。' }}</p>
            </div>
            <span class="badge-soft">{{ group.albums.length }} 个专辑</span>
          </div>

          <div class="album-card-grid">
            <article v-for="album in group.albums" :key="album.id" class="album-card">
              <div class="album-cover-shell">
                <img v-if="album.coverUrl" :src="album.coverUrl" alt="cover" class="album-cover-image" />
                <div v-else class="album-cover-placeholder">{{ album.name.slice(0, 1) }}</div>
              </div>

              <div class="album-card-body">
                <div class="summary-row">
                  <h3 class="card-title">{{ album.name }}</h3>
                  <span :class="statusBadgeClass(album.status)">{{ album.status }}</span>
                </div>
                <p class="card-copy line-clamp-2">{{ album.description || '暂无专辑简介' }}</p>
                <div class="result-meta">
                  <span>资料 {{ materialCountMap[album.id] || 0 }}</span>
                  <span>排序 {{ album.sortOrder }}</span>
                </div>
                <div class="action-row">
                  <el-button text @click="openEdit(album)">编辑</el-button>
                  <el-button text @click="goToMaterials(album.id)">资料管理</el-button>
                  <el-button text type="danger" @click="remove(album.id)">删除</el-button>
                </div>
              </div>
            </article>
          </div>
        </section>
      </div>
    </section>

    <el-dialog v-model="visible" :title="editingId ? '编辑专辑' : '新建专辑'" width="680px">
      <el-form :model="form" label-width="90px" class="form-stack">
        <el-form-item label="所属分类">
          <el-select v-model="form.categoryId" class="full-width" placeholder="请选择分类">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图">
          <ImageUploadField v-model="form.coverUrl" />
        </el-form-item>
        <el-form-item label="专辑名称">
          <el-input v-model="form.name" placeholder="例如：Java 基础课程" />
        </el-form-item>
        <el-form-item label="专辑简介">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="填写专辑简介" />
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
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminAlbumApi, adminCategoryApi, adminMaterialApi } from '../../api/modules'
import ImageUploadField from '../../components/upload/ImageUploadField.vue'
import type { Album, Category, Material } from '../../types'

const router = useRouter()
const albums = ref<Album[]>([])
const categories = ref<Category[]>([])
const materials = ref<Material[]>([])
const visible = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<AlbumForm>({
  categoryId: undefined,
  name: '',
  description: '',
  coverUrl: '',
  sortOrder: 0,
  status: 'ACTIVE',
})

interface AlbumForm {
  categoryId?: number
  name: string
  description: string
  coverUrl: string
  sortOrder: number
  status: string
}

const materialCountMap = computed<Record<number, number>>(() =>
  materials.value.reduce((acc, item) => {
    if (!item.albumId) return acc
    acc[item.albumId] = (acc[item.albumId] || 0) + 1
    return acc
  }, {} as Record<number, number>),
)

const categoryGroups = computed(() =>
  categories.value
    .map((category) => ({
      category,
      albums: albums.value
        .filter((album) => album.categoryId === category.id)
        .sort((a, b) => a.sortOrder - b.sortOrder || a.id - b.id),
    }))
    .filter((group) => group.albums.length > 0),
)

async function load() {
  const [albumResp, categoryResp, materialResp] = await Promise.all([
    adminAlbumApi.list(),
    adminCategoryApi.list(),
    adminMaterialApi.list(),
  ])
  albums.value = albumResp.data
  categories.value = categoryResp.data
  materials.value = materialResp.data
}

function resetForm() {
  Object.assign(form, {
    categoryId: categories.value[0]?.id,
    name: '',
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

function openEdit(album: Album) {
  editingId.value = album.id
  Object.assign(form, {
    categoryId: album.categoryId,
    name: album.name,
    description: album.description || '',
    coverUrl: album.coverUrl || '',
    sortOrder: album.sortOrder ?? 0,
    status: album.status || 'ACTIVE',
  })
  visible.value = true
}

async function submit() {
  const payload = {
    categoryId: form.categoryId,
    name: form.name,
    description: form.description,
    coverUrl: form.coverUrl || undefined,
    sortOrder: form.sortOrder,
    status: form.status,
  }

  if (editingId.value) {
    await adminAlbumApi.update(editingId.value, payload)
  } else {
    await adminAlbumApi.create(payload)
  }
  visible.value = false
  ElMessage.success('保存成功')
  await load()
}

async function remove(id: number) {
  await ElMessageBox.confirm('删除后该专辑将不可恢复，是否继续？', '删除专辑', { type: 'warning' })
  await adminAlbumApi.remove(id)
  ElMessage.success('删除成功')
  await load()
}

function goToMaterials(id: number) {
  router.push(`/admin/albums/${id}/materials`)
}

function statusBadgeClass(status: string) {
  return status === 'ACTIVE' ? 'badge-success' : 'badge-warning'
}

onMounted(load)
</script>
