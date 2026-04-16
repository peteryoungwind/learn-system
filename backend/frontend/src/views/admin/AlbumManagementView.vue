<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Taxonomy</p>
      <h1 class="page-title">专辑管理</h1>
      <p class="page-subtitle">维护专辑并归属到具体分类下。</p>
    </section>

    <section class="ui-panel">
      <div class="toolbar">
        <span class="badge-soft">{{ albums.length }} 个专辑</span>
        <el-button type="primary" @click="openCreate">新建专辑</el-button>
      </div>
      <div class="table-shell">
        <el-table :data="albums">
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="categoryId" label="分类ID" />
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

    <el-dialog v-model="visible" :title="editingId ? '编辑专辑' : '新建专辑'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类">
          <el-select v-model="form.categoryId">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
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
import { adminAlbumApi, adminCategoryApi } from '../../api/modules'
import type { Album, Category } from '../../types'

const albums = ref<Album[]>([])
const categories = ref<Category[]>([])
const visible = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<any>({ categoryId: undefined, name: '', description: '', sortOrder: 0, status: 'ACTIVE' })

async function load() {
  const [albumResp, categoryResp] = await Promise.all([adminAlbumApi.list(), adminCategoryApi.list()])
  albums.value = albumResp.data
  categories.value = categoryResp.data
}

function openCreate() {
  editingId.value = null
  Object.assign(form, { categoryId: undefined, name: '', description: '', sortOrder: 0, status: 'ACTIVE' })
  visible.value = true
}

function openEdit(album: Album) {
  editingId.value = album.id
  Object.assign(form, album)
  visible.value = true
}

async function submit() {
  if (editingId.value) {
    await adminAlbumApi.update(editingId.value, form)
  } else {
    await adminAlbumApi.create(form)
  }
  visible.value = false
  ElMessage.success('保存成功')
  await load()
}

async function remove(id: number) {
  await adminAlbumApi.remove(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
