<template>
  <div>
    <h1 class="page-title">学习页</h1>
    <div v-if="material" style="display: grid; gap: 16px;">
      <div>
        <h2 style="margin: 0 0 8px">{{ material.title }}</h2>
        <div style="color: #6b7280">{{ material.author }} · {{ material.fileType }}</div>
      </div>
      <div class="toolbar">
        <el-button @click="setProgress('READ', 'IN_PROGRESS')">标记已读</el-button>
        <el-button @click="setProgress('UNREAD', 'IN_PROGRESS')">标记未读</el-button>
        <el-button type="success" @click="setProgress('READ', 'COMPLETED')">标记完成</el-button>
      </div>
      <div class="glass-card" style="padding: 16px; min-height: 480px;">
        <iframe v-if="previewUrl" :src="previewUrl" style="width: 100%; height: 480px; border: 0" />
        <el-empty v-else description="预览地址加载中" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { userLearningApi } from '../../api/modules'
import type { Material } from '../../types'

const route = useRoute()
const material = ref<Material | null>(null)
const previewUrl = ref('')
const materialId = Number(route.params.id)

async function load() {
  const [detailResp, previewResp] = await Promise.all([
    userLearningApi.detail(materialId),
    userLearningApi.preview(materialId),
    userLearningApi.access(materialId),
  ])
  material.value = detailResp.data
  previewUrl.value = previewResp.data.url
}

async function setProgress(readStatus: string, completionStatus: string) {
  await userLearningApi.progress(materialId, { readStatus, completionStatus })
  ElMessage.success('已更新')
}

onMounted(load)
</script>
