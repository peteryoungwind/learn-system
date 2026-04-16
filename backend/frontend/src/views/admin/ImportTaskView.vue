<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Imports</p>
      <h1 class="page-title">导入任务</h1>
      <p class="page-subtitle">跟踪资料导入的执行状态、成功数量和失败摘要。</p>
    </section>

    <section class="ui-panel">
      <div class="toolbar">
        <span class="badge-soft">{{ tasks.length }} 个任务</span>
        <span class="badge-neutral">当前先展示导入任务记录</span>
      </div>

      <div v-if="!tasks.length" class="empty-shell">
        <el-empty description="暂无导入任务记录" />
      </div>

      <div v-else class="result-grid">
        <article v-for="task in tasks" :key="task.id" class="result-item">
          <div class="summary-row">
            <h3 class="card-title">任务 #{{ task.id }}</h3>
            <span class="badge-soft">{{ task.status }}</span>
          </div>
          <div class="result-meta">
            <span>{{ task.importType }}</span>
            <span>总数 {{ task.totalCount }}</span>
            <span>成功 {{ task.successCount }}</span>
            <span>失败 {{ task.failCount }}</span>
          </div>
          <p class="card-copy">{{ task.errorSummary || '暂无失败摘要' }}</p>
          <div class="result-meta">
            <span>{{ task.createdAt }}</span>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { adminImportApi } from '../../api/modules'
import type { ImportTask } from '../../types'

const tasks = ref<ImportTask[]>([])

async function load() {
  const response = await adminImportApi.list()
  tasks.value = response.data
}

onMounted(load)
</script>
