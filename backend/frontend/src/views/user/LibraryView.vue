<template>
  <div>
    <h1 class="page-title">资料浏览</h1>
    <div class="toolbar">
      <el-select v-model="filters.categoryId" placeholder="分类" clearable style="width: 180px">
        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select v-model="filters.albumId" placeholder="专辑" clearable style="width: 180px">
        <el-option v-for="item in albums" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-input v-model="filters.keyword" placeholder="标题/作者" style="max-width: 280px" />
      <el-button type="primary" @click="loadMaterials">查询</el-button>
    </div>
    <el-table :data="materials" @row-click="openDetail">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="fileType" label="类型" />
      <el-table-column prop="publishStatus" label="状态" />
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { userLearningApi } from '../../api/modules'
import type { Album, Category, Material } from '../../types'

const route = useRoute()
const router = useRouter()
const categories = ref<Category[]>([])
const albums = ref<Album[]>([])
const materials = ref<Material[]>([])
const filters = reactive<{ categoryId?: number; albumId?: number; keyword?: string }>({
  keyword: String(route.query.keyword || ''),
})

async function loadBase() {
  const categoryResp = await userLearningApi.categories()
  categories.value = categoryResp.data
}

async function loadAlbums() {
  const albumResp = await userLearningApi.albums(filters.categoryId)
  albums.value = albumResp.data
}

async function loadMaterials() {
  const materialResp = await userLearningApi.materials(filters)
  materials.value = materialResp.data
}

function openDetail(row: Material) {
  router.push(`/materials/${row.id}`)
}

watch(() => filters.categoryId, async () => {
  filters.albumId = undefined
  await loadAlbums()
})

onMounted(async () => {
  await loadBase()
  await loadAlbums()
  await loadMaterials()
})
</script>
