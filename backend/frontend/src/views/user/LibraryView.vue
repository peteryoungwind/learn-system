<template>
  <div class="page-stack">
    <section class="hero-panel ui-panel">
      <div class="hero-content">
        <p class="eyebrow">Library Browser</p>
        <h1 class="page-title">专辑资料库</h1>
        <p class="page-subtitle">按分类查看你可访问的专辑内容，进入专辑后继续浏览资料。</p>
      </div>
      <div class="hero-side stats-grid two-up">
        <article class="metric-card">
          <span class="metric-label">分类数</span>
          <h2 class="metric-value">{{ categoryGroups.length }}</h2>
        </article>
        <article class="metric-card">
          <span class="metric-label">专辑数</span>
          <h2 class="metric-value">{{ albumCount }}</h2>
        </article>
      </div>
    </section>

    <section v-if="!categoryGroups.length" class="ui-panel empty-shell">
      <el-empty description="暂无可浏览的专辑内容" />
    </section>

    <section v-else class="page-stack">
      <article v-for="group in categoryGroups" :key="group.category.id" class="ui-panel">
        <div class="summary-row album-group-head">
          <div class="library-group-copy">
            <div class="library-group-title-row">
              <div class="library-group-cover-shell">
                <img v-if="group.category.coverUrl" :src="group.category.coverUrl" alt="cover" class="library-group-cover-image" />
                <div v-else class="library-group-cover-placeholder">{{ group.category.name.slice(0, 1) }}</div>
              </div>
              <div>
                <h2 class="section-title">{{ group.category.name }}</h2>
                <p class="section-copy">{{ group.category.description || '浏览该分类下的专辑内容。' }}</p>
              </div>
            </div>
          </div>
          <span class="badge-soft">{{ group.albums.length }} 个专辑</span>
        </div>

        <div class="album-card-grid">
          <article v-for="album in group.albums" :key="album.id" class="album-card library-album-card" @click="openAlbum(album.id)">
            <div class="album-cover-shell">
              <img v-if="album.coverUrl" :src="album.coverUrl" alt="cover" class="album-cover-image" />
              <div v-else class="album-cover-placeholder">{{ album.name.slice(0, 1) }}</div>
            </div>
            <div class="album-card-body">
              <h3 class="card-title">{{ album.name }}</h3>
              <p class="card-copy line-clamp-2">{{ album.description || '暂无专辑简介' }}</p>
              <div class="result-meta">
                <span>{{ album.status }}</span>
                <span>排序 {{ album.sortOrder }}</span>
              </div>
            </div>
          </article>
        </div>
      </article>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userLearningApi } from '../../api/modules'
import type { Album, Category } from '../../types'

const router = useRouter()
const categories = ref<Category[]>([])
const albums = ref<Album[]>([])

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

const albumCount = computed(() => albums.value.length)

async function load() {
  const categoryResp = await userLearningApi.categories()
  categories.value = categoryResp.data

  const albumResponses = await Promise.all(categories.value.map((item) => userLearningApi.albums(item.id)))
  albums.value = albumResponses.flatMap((response) => response.data)
}

function openAlbum(id: number) {
  router.push(`/albums/${id}`)
}

onMounted(load)
</script>
