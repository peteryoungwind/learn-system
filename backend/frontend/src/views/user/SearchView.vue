<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Search</p>
      <h1 class="page-title">资料搜索</h1>
      <p class="page-subtitle">按关键词、分类、专辑和文件类型筛选可访问资料。</p>
    </section>

    <el-alert
      v-if="deniedAdmin"
      title="你没有管理员权限，已切换到用户端。"
      type="warning"
      :closable="false"
      style="margin-bottom: 16px;"
    />

    <section class="layout-grid">
      <aside class="filter-card sidebar-stack">
        <div class="filter-head">
          <p class="eyebrow">Filters</p>
          <h2 class="section-title">搜索条件</h2>
        </div>
        <el-input v-model="filters.keyword" placeholder="关键词" @keyup.enter="applyFilters" />
        <el-select v-model="filters.categoryId" placeholder="分类" clearable>
          <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-select v-model="filters.albumId" placeholder="专辑" clearable>
          <el-option v-for="item in albums" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-select v-model="filters.fileType" placeholder="文件类型" clearable>
          <el-option label="PDF" value="PDF" />
          <el-option label="AUDIO" value="AUDIO" />
          <el-option label="VIDEO" value="VIDEO" />
          <el-option label="HTML" value="HTML" />
        </el-select>
        <div class="action-row">
          <el-button type="primary" @click="applyFilters">搜索</el-button>
          <el-button @click="resetFilters">清空</el-button>
        </div>
      </aside>

      <section class="panel-stack">
        <div class="ui-panel">
          <div class="summary-row">
            <span class="badge-soft">{{ materials.length }} 条结果</span>
            <span v-if="filters.keyword" class="badge-neutral">关键词：{{ filters.keyword }}</span>
            <span v-if="filters.fileType" class="badge-neutral">{{ filters.fileType }}</span>
          </div>
        </div>

        <div v-if="!loading && !materials.length" class="ui-panel empty-shell">
          <el-empty description="暂无匹配资料，请调整搜索条件" />
        </div>

        <div v-else class="result-grid" v-loading="loading">
          <article v-for="item in materials" :key="item.id" class="result-item" @click="openDetail(item)">
            <h3 class="result-title">{{ item.title }}</h3>
            <p class="card-copy">{{ item.summary || '暂无摘要' }}</p>
            <div class="result-meta">
              <span>{{ item.author || '未知作者' }}</span>
              <span>{{ item.fileType }}</span>
              <span>{{ item.publishStatus }}</span>
            </div>
          </article>
        </div>
      </section>
    </section>
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
const loading = ref(false)
const deniedAdmin = ref(route.query.denied === 'admin')
const filters = reactive<{
  categoryId?: number
  albumId?: number
  keyword?: string
  fileType?: string
}>({
  categoryId: route.query.categoryId ? Number(route.query.categoryId) : undefined,
  albumId: route.query.albumId ? Number(route.query.albumId) : undefined,
  keyword: typeof route.query.keyword === 'string' ? route.query.keyword : '',
  fileType: typeof route.query.fileType === 'string' ? route.query.fileType : undefined,
})

function syncFromRoute() {
  filters.categoryId = route.query.categoryId ? Number(route.query.categoryId) : undefined
  filters.albumId = route.query.albumId ? Number(route.query.albumId) : undefined
  filters.keyword = typeof route.query.keyword === 'string' ? route.query.keyword : ''
  filters.fileType = typeof route.query.fileType === 'string' ? route.query.fileType : undefined
  deniedAdmin.value = route.query.denied === 'admin'
}

async function loadBase() {
  const categoryResp = await userLearningApi.categories()
  categories.value = categoryResp.data
}

async function loadAlbums() {
  const albumResp = await userLearningApi.albums(filters.categoryId)
  albums.value = albumResp.data
}

async function loadMaterials() {
  loading.value = true
  try {
    const materialResp = await userLearningApi.materials({
      categoryId: filters.categoryId,
      albumId: filters.albumId,
      keyword: filters.keyword,
    })
    const data = materialResp.data as Material[]
    materials.value = filters.fileType ? data.filter((item) => item.fileType === filters.fileType) : data
  } finally {
    loading.value = false
  }
}

function applyFilters() {
  router.push({
    path: '/search',
    query: {
      ...(filters.keyword ? { keyword: filters.keyword } : {}),
      ...(filters.categoryId ? { categoryId: String(filters.categoryId) } : {}),
      ...(filters.albumId ? { albumId: String(filters.albumId) } : {}),
      ...(filters.fileType ? { fileType: filters.fileType } : {}),
    },
  })
}

function resetFilters() {
  router.push({ path: '/search' })
}

function openDetail(row: Material) {
  router.push(`/materials/${row.id}`)
}

watch(() => filters.categoryId, async (value, oldValue) => {
  if (value !== oldValue) {
    filters.albumId = undefined
  }
  await loadAlbums()
})

watch(() => route.fullPath, async () => {
  syncFromRoute()
  await loadAlbums()
  await loadMaterials()
})

onMounted(async () => {
  await loadBase()
  await loadAlbums()
  await loadMaterials()
})
</script>
