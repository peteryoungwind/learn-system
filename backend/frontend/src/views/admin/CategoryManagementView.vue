<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Taxonomy</p>
      <h1 class="page-title">分类管理</h1>
      <p class="page-subtitle">维护分类结构，供资料和专辑关联使用。</p>
    </section>

    <section class="ui-panel">
      <div class="toolbar">
        <span class="badge-soft">{{ categories.length }} 个分类</span>
        <el-button type="primary" @click="openCreate">新建分类</el-button>
      </div>
      <div class="table-shell">
        <el-table :data="categories">
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="code" label="编码" />
          <el-table-column prop="status" label="状态" />
          <el-table-column prop="sortOrder" label="排序" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button text @click="openEdit(scope.row)">编辑</el-button>
              <el-button text @click="remove(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>

    <el-dialog v-model="visible" :title="editingId ? '编辑分类' : '新建分类'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="form.code" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status"><el-option label="启用" value="ACTIVE" /><el-option label="停用" value="DISABLED" /></el-select></el-form-item>
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
import { ElMessage } from 'element-plus'
import { adminCategoryApi } from '../../api/modules'
import type { Category } from '../../types'

const categories = ref<Category[]>([])
const visible = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<any>({ name: '', code: '', description: '', sortOrder: 0, status: 'ACTIVE' })

async function load() {
  const response = await adminCategoryApi.list()
  categories.value = response.data
}

function openCreate() {
  editingId.value = null
  Object.assign(form, { name: '', code: '', description: '', sortOrder: 0, status: 'ACTIVE' })
  visible.value = true
}

function openEdit(category: Category) {
  editingId.value = category.id
  Object.assign(form, category)
  visible.value = true
}

async function submit() {
  if (editingId.value) {
    await adminCategoryApi.update(editingId.value, form)
  } else {
    await adminCategoryApi.create(form)
  }
  visible.value = false
  ElMessage.success('保存成功')
  await load()
}

async function remove(id: number) {
  await adminCategoryApi.remove(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
