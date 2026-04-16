<template>
  <div class="page-stack">
    <section class="hero-panel ui-panel">
      <div class="hero-content">
        <p class="eyebrow">User Home</p>
        <h1 class="hero-title">学习首页</h1>
        <p class="hero-copy">从继续学习、按分类浏览或直接搜索三种方式进入资料内容。</p>
        <div class="action-row">
          <el-input v-model="keyword" placeholder="搜索标题或作者" style="max-width: 320px" @keyup.enter="search" />
          <el-button type="primary" @click="search">搜索</el-button>
        </div>
      </div>
      <div class="hero-side">
        <div class="info-card">
          <div class="info-key">已授权分类</div>
          <div class="info-value">{{ categories.length }} 个可访问分类</div>
        </div>
        <div class="info-card">
          <div class="info-key">继续学习</div>
          <div class="info-value">{{ continueList.length }} 条最近记录</div>
        </div>
      </div>
    </section>

    <section class="card-grid two-up">
      <article class="ui-panel">
        <div class="section-head">
          <p class="eyebrow">Authorized</p>
          <h2 class="section-title">授权分类</h2>
          <p class="section-copy">只展示当前账号有访问权限的分类。</p>
        </div>
        <el-empty v-if="!categories.length" description="暂无授权分类" />
        <div v-else class="chip-row">
          <span v-for="item in categories" :key="item.id" class="filter-pill is-active">{{ item.name }}</span>
        </div>
      </article>

      <article class="ui-panel">
        <div class="section-head">
          <p class="eyebrow">Continue</p>
          <h2 class="section-title">继续学习</h2>
          <p class="section-copy">最近访问记录会保留在这里，方便继续阅读。</p>
        </div>
        <el-empty v-if="!continueList.length" description="暂无学习记录" />
        <div v-else class="list-stack">
          <article v-for="item in continueList" :key="item.materialId" class="list-card">
            <h3 class="card-title">资料 #{{ item.materialId }}</h3>
            <div class="result-meta">
              <span>{{ item.completionStatus }}</span>
              <span>{{ item.lastAccessedAt }}</span>
            </div>
          </article>
        </div>
      </article>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userLearningApi } from '../../api/modules'
import type { Category } from '../../types'

const router = useRouter()
const categories = ref<Category[]>([])
const continueList = ref<any[]>([])
const keyword = ref('')

async function load() {
  const [categoryResp, continueResp] = await Promise.all([
    userLearningApi.categories(),
    userLearningApi.continueLearning(),
  ])
  categories.value = categoryResp.data
  continueList.value = continueResp.data
}

function search() {
  router.push({ path: '/search', query: { keyword: keyword.value } })
}

onMounted(load)
</script>
