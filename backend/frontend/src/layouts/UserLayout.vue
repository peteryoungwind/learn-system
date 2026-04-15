<template>
  <div class="page-shell">
    <div class="glass-card" style="padding: 16px; display: flex; gap: 12px; justify-content: space-between; align-items: center; margin-bottom: 16px;">
      <div>
        <strong>学习资料知识库</strong>
        <div style="font-size: 12px; color: #6b7280">学习中心</div>
      </div>
      <div style="display: flex; gap: 8px; align-items: center;">
        <span>{{ auth.user?.displayName }}</span>
        <el-button text @click="logout">退出</el-button>
      </div>
    </div>
    <div class="glass-card" style="padding: 16px; margin-bottom: 16px;">
      <el-menu :default-active="route.path" mode="horizontal" router>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/library">资料浏览</el-menu-item>
      </el-menu>
    </div>
    <div class="glass-card" style="padding: 20px; min-height: 70vh;">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const router = useRouter()
const route = useRoute()

function logout() {
  auth.logout()
  router.push('/login')
}
</script>
