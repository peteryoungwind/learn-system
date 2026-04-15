<template>
  <div>
    <h1 class="page-title">学习首页</h1>
    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索标题或作者" style="max-width: 320px" @keyup.enter="search" />
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px;">
      <div class="glass-card" style="padding: 16px;">
        <h3>授权分类</h3>
        <el-empty v-if="!categories.length" description="暂无授权分类" />
        <el-space wrap>
          <el-tag v-for="item in categories" :key="item.id">{{ item.name }}</el-tag>
        </el-space>
      </div>
      <div class="glass-card" style="padding: 16px;">
        <h3>继续学习</h3>
        <el-empty v-if="!continueList.length" description="暂无学习记录" />
        <el-table v-else :data="continueList">
          <el-table-column prop="materialId" label="资料ID" />
          <el-table-column prop="completionStatus" label="状态" />
          <el-table-column prop="lastAccessedAt" label="最近访问" />
        </el-table>
      </div>
    </div>
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
  router.push({ path: '/library', query: { keyword: keyword.value } })
}

onMounted(load)
</script>
