<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Dashboard</p>
      <h1 class="page-title">后台概览</h1>
      <p class="page-subtitle">查看资料库当前规模与核心管理入口。</p>
    </section>

    <section class="metric-grid">
      <article class="metric-card">
        <div class="metric-label">总用户</div>
        <div class="metric-value">{{ stats.users }}</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">总分类</div>
        <div class="metric-value">{{ stats.categories }}</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">总资料</div>
        <div class="metric-value">{{ stats.materials }}</div>
      </article>
    </section>

    <section class="card-grid two-up">
      <article class="ui-panel">
        <div class="section-head">
          <p class="eyebrow">Taxonomy</p>
          <h2 class="section-title">分类体系维护</h2>
          <p class="section-copy">维护分类与专辑结构，控制前台资料归属关系。</p>
        </div>
        <div class="chip-row">
          <span class="filter-pill is-active">分类管理</span>
          <span class="filter-pill">专辑管理</span>
        </div>
      </article>
      <article class="ui-panel">
        <div class="section-head">
          <p class="eyebrow">Operations</p>
          <h2 class="section-title">资料与导入</h2>
          <p class="section-copy">管理资料元数据，跟踪导入任务状态和处理结果。</p>
        </div>
        <div class="chip-row">
          <span class="filter-pill is-active">资料管理</span>
          <span class="filter-pill">导入任务</span>
        </div>
      </article>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { adminUserApi, adminCategoryApi, adminMaterialApi } from '../../api/modules'

const stats = reactive({ users: 0, categories: 0, materials: 0 })

onMounted(async () => {
  const [users, categories, materials] = await Promise.all([
    adminUserApi.list(),
    adminCategoryApi.list(),
    adminMaterialApi.list(),
  ])
  stats.users = users.data.length
  stats.categories = categories.data.length
  stats.materials = materials.data.length
})
</script>
