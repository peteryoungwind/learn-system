<template>
  <div class="page-shell">
    <div class="glass-card" style="padding: 16px; display: flex; gap: 12px; justify-content: space-between; align-items: center; margin-bottom: 16px;">
      <div>
        <strong>学习资料知识库</strong>
        <div style="font-size: 12px; color: #6b7280">管理员后台</div>
      </div>
      <div style="display: flex; gap: 8px; align-items: center;">
        <span>{{ auth.user?.displayName }}</span>
        <el-button text @click="logout">退出</el-button>
      </div>
    </div>
    <div style="display: grid; grid-template-columns: 220px 1fr; gap: 16px;">
      <div class="glass-card" style="padding: 12px;">
        <el-menu :default-active="route.path" router>
          <el-menu-item index="/admin">概览</el-menu-item>
          <el-menu-item index="/admin/users">用户管理</el-menu-item>
          <el-menu-item index="/admin/categories">分类管理</el-menu-item>
          <el-menu-item index="/admin/albums">专辑管理</el-menu-item>
          <el-menu-item index="/admin/materials">资料管理</el-menu-item>
        </el-menu>
      </div>
      <div class="glass-card" style="padding: 20px; min-height: 70vh;">
        <router-view />
      </div>
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
