<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Library Browser</p>
      <h1 class="page-title">资料浏览</h1>
      <p class="page-subtitle">通过分类、专辑和关键词筛选内容列表。</p>
    </section>

    <section class="layout-grid">
      <aside class="filter-card sidebar-stack">
        <div class="filter-head">
          <p class="eyebrow">Filters</p>
          <h2 class="section-title">浏览条件</h2>
        </div>
        <el-select v-model="filters.categoryId" placeholder="分类" clearable>
          <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-select v-model="filters.albumId" placeholder="专辑" clearable>
          <el-option v-for="item in albums" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-input v-model="filters.keyword" placeholder="标题/作者" @keyup.enter="loadMaterials" />
        <el-button type="primary" @click="loadMaterials">查询</el-button>
      </aside>

      <section class="panel-stack">
        <div class="ui-panel">
          <div class="summary-row">
            <span class="badge-soft">{{ materials.length }} 条结果</span>
            <span class="badge-neutral">{{ filters.keyword || '全部资料' }}</span>
          </div>
        </div>

        <div v-if="!materials.length" class="ui-panel empty-shell">
          <el-empty description="暂无资料，请调整筛选条件" />
        </div>

        <div v-else class="result-grid">
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
