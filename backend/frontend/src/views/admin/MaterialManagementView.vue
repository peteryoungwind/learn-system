<template>
  <div>
    <h1 class="page-title">资料管理</h1>
    <div class="toolbar">
      <el-button type="primary" @click="openCreate">新建资料</el-button>
    </div>
    <el-table :data="materials">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="fileType" label="类型" />
      <el-table-column prop="publishStatus" label="发布状态" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button text @click="openEdit(scope.row)">编辑</el-button>
          <el-button text @click="remove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="visible" :title="editingId ? '编辑资料' : '新建资料'" width="720px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="作者"><el-input v-model="form.author" /></el-form-item>
        <el-form-item label="文件类型"><el-input v-model="form.fileType" placeholder="PDF / AUDIO / VIDEO / HTML" /></el-form-item>
        <el-form-item label="存储提供商"><el-select v-model="form.storageProvider"><el-option label="OSS" value="OSS" /><el-option label="QINIU" value="QINIU" /></el-select></el-form-item>
        <el-form-item label="Object Key"><el-input v-model="form.objectKey" /></el-form-item>
        <el-form-item label="分类"><el-select v-model="form.categoryId"><el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item>
        <el-form-item label="专辑"><el-select v-model="form.albumId" clearable><el-option v-for="item in albums" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" /></el-form-item>
        <el-form-item label="发布状态"><el-select v-model="form.publishStatus"><el-option label="草稿" value="DRAFT" /><el-option label="已发布" value="PUBLISHED" /><el-option label="停用" value="DISABLED" /></el-select></el-form-item>
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
import { adminAlbumApi, adminCategoryApi, adminMaterialApi } from '../../api/modules'
import type { Album, Category, Material } from '../../types'

const materials = ref<Material[]>([])
const categories = ref<Category[]>([])
const albums = ref<Album[]>([])
const visible = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<any>({
  title: '',
  author: '',
  fileType: 'PDF',
  storageProvider: 'OSS',
  objectKey: '',
  categoryId: undefined,
  albumId: undefined,
  subtitle: '',
  summary: '',
  coverUrl: '',
  fileSize: undefined,
  tags: '',
  sortOrder: 0,
  remark: '',
  publishStatus: 'DRAFT',
})

async function load() {
  const [materialResp, categoryResp, albumResp] = await Promise.all([
    adminMaterialApi.list(),
    adminCategoryApi.list(),
    adminAlbumApi.list(),
  ])
  materials.value = materialResp.data
  categories.value = categoryResp.data
  albums.value = albumResp.data
}

function openCreate() {
  editingId.value = null
  Object.assign(form, {
    title: '', author: '', fileType: 'PDF', storageProvider: 'OSS', objectKey: '', categoryId: undefined,
    albumId: undefined, subtitle: '', summary: '', coverUrl: '', fileSize: undefined, tags: '', sortOrder: 0,
    remark: '', publishStatus: 'DRAFT',
  })
  visible.value = true
}

function openEdit(material: Material) {
  editingId.value = material.id
  Object.assign(form, material)
  visible.value = true
}

async function submit() {
  if (editingId.value) {
    await adminMaterialApi.update(editingId.value, form)
  } else {
    await adminMaterialApi.create(form)
  }
  visible.value = false
  ElMessage.success('保存成功')
  await load()
}

async function remove(id: number) {
  await adminMaterialApi.remove(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
