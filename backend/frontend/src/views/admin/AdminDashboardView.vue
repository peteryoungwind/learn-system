<template>
  <div>
    <h1 class="page-title">后台概览</h1>
    <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px;">
      <div class="glass-card" style="padding: 20px;">
        <div style="font-size: 12px; color: #6b7280">总用户</div>
        <div style="font-size: 32px; font-weight: 600">{{ stats.users }}</div>
      </div>
      <div class="glass-card" style="padding: 20px;">
        <div style="font-size: 12px; color: #6b7280">总分类</div>
        <div style="font-size: 32px; font-weight: 600">{{ stats.categories }}</div>
      </div>
      <div class="glass-card" style="padding: 20px;">
        <div style="font-size: 12px; color: #6b7280">总资料</div>
        <div style="font-size: 32px; font-weight: 600">{{ stats.materials }}</div>
      </div>
    </div>
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
