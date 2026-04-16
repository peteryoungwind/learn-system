<template>
  <div class="page-stack">
    <section v-if="album" class="hero-panel ui-panel">
      <div class="hero-content">
        <p class="eyebrow">Album</p>
        <h1 class="page-title">{{ album.name }}</h1>
        <p class="page-subtitle">{{ album.description || '浏览专辑下的全部资料内容。' }}</p>
        <div class="result-meta">
          <span>{{ category?.name || '未分类' }}</span>
          <span>{{ materials.length }} 份资料</span>
        </div>
      </div>
      <div class="hero-side">
        <div class="album-detail-cover-shell">
          <img v-if="album.coverUrl" :src="album.coverUrl" alt="cover" class="album-detail-cover-image" />
          <div v-else class="album-detail-cover-placeholder">{{ album.name.slice(0, 1) }}</div>
        </div>
      </div>
    </section>

    <section class="ui-panel">
      <div class="toolbar toolbar-between">
        <div class="meta-row">
          <span class="badge-soft">{{ materials.length }} 条资料</span>
          <span class="badge-neutral">{{ category?.name || '未分类' }}</span>
        </div>
        <el-button @click="goBack">返回资料库</el-button>
      </div>

      <div v-if="!materials.length" class="empty-shell">
        <el-empty description="该专辑下暂无资料" />
      </div>

      <div v-else class="result-grid">
        <article v-for="item in materials" :key="item.id" class="result-item" @click="openMaterial(item.id)">
          <h3 class="result-title">{{ item.title }}</h3>
          <p class="card-copy line-clamp-2">{{ item.summary || '暂无摘要' }}</p>
          <div class="result-meta">
            <span>{{ item.author || '未知作者' }}</span>
            <span>{{ item.fileType }}</span>
            <span>{{ item.publishStatus }}</span>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { userLearningApi } from '../../api/modules'
import type { Album, Category, Material } from '../../types'

const route = useRoute()
const router = useRouter()
const albumId = Number(route.params.id)
const categories = ref<Category[]>([])
const album = ref<Album | null>(null)
const materials = ref<Material[]>([])

const category = computed(() => categories.value.find((item) => item.id === album.value?.categoryId))

async function load() {
  const categoryResp = await userLearningApi.categories()
  categories.value = categoryResp.data

  const albumResponses = await Promise.all(categories.value.map((item) => userLearningApi.albums(item.id)))
  const allAlbums = albumResponses.flatMap((response) => response.data)
  album.value = allAlbums.find((item) => item.id === albumId) || null

  const materialResp = await userLearningApi.materials({ albumId })
  materials.value = materialResp.data
}

function openMaterial(id: number) {
  router.push(`/materials/${id}`)
}

function goBack() {
  router.push('/library')
}

onMounted(load)
</script>
