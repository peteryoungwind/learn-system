<template>
  <div class="page-stack">
    <section v-if="material" class="section-head">
      <p class="eyebrow">Viewer</p>
      <h1 class="page-title">{{ material.title }}</h1>
      <p class="page-subtitle">{{ material.author }} · {{ material.fileType }}</p>
    </section>

    <section v-if="material" class="viewer-layout">
      <div class="viewer-frame">
        <iframe v-if="previewUrl" :src="previewUrl" />
        <el-empty v-else description="预览地址加载中" />
      </div>

      <aside class="panel-stack">
        <div class="ui-panel">
          <div class="section-head">
            <p class="eyebrow">Progress</p>
            <h2 class="section-title">学习状态</h2>
          </div>
          <div class="action-row">
            <el-button @click="setProgress('READ', 'IN_PROGRESS')">标记已读</el-button>
            <el-button @click="setProgress('UNREAD', 'IN_PROGRESS')">标记未读</el-button>
            <el-button type="success" @click="setProgress('READ', 'COMPLETED')">标记完成</el-button>
          </div>
        </div>

        <div class="ui-panel info-list">
          <div class="info-item">
            <div class="info-key">作者</div>
            <div class="info-value">{{ material.author || '未知作者' }}</div>
          </div>
          <div class="info-item">
            <div class="info-key">文件类型</div>
            <div class="info-value">{{ material.fileType }}</div>
          </div>
          <div class="info-item">
            <div class="info-key">发布状态</div>
            <div class="info-value">{{ material.publishStatus }}</div>
          </div>
          <div class="info-item">
            <div class="info-key">摘要</div>
            <div class="info-value">{{ material.summary || '暂无摘要' }}</div>
          </div>
        </div>
      </aside>
    </section>
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
