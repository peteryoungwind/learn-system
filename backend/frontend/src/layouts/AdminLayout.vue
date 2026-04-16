<template>
  <div class="app-shell admin-shell">
    <header class="app-topbar">
      <div class="brand-block">
        <div class="brand-mark">AD</div>
        <div class="brand-copy">
          <p class="eyebrow">Administration</p>
          <h1 class="brand-title">学习资料知识库</h1>
        </div>
      </div>
      <div class="topbar-actions">
        <span class="badge-soft">{{ auth.user?.displayName }}</span>
        <el-button @click="goToUserSystem">进入学习中心</el-button>
        <el-button text @click="logout">退出</el-button>
      </div>
    </header>

    <main class="app-main">
      <section class="shell-banner ui-panel">
        <div class="banner-copy">
          <div class="system-badge">管理员端</div>
          <h2 class="section-title">后台工作台</h2>
          <p class="section-copy">集中处理用户、分类、专辑、资料和导入任务。</p>
        </div>
        <nav class="app-nav">
          <router-link class="nav-pill" :class="{ 'is-active': route.path === '/admin' }" to="/admin">概览</router-link>
          <router-link class="nav-pill" :class="{ 'is-active': route.path.startsWith('/admin/users') }" to="/admin/users">用户管理</router-link>
          <router-link class="nav-pill" :class="{ 'is-active': route.path.startsWith('/admin/categories') || route.path.startsWith('/admin/albums') }" to="/admin/categories">分类体系</router-link>
          <router-link class="nav-pill" :class="{ 'is-active': route.path.startsWith('/admin/materials') }" to="/admin/materials">资料管理</router-link>
          <router-link class="nav-pill" :class="{ 'is-active': route.path.startsWith('/admin/storage') }" to="/admin/storage">存储配置</router-link>
          <router-link class="nav-pill" :class="{ 'is-active': route.path.startsWith('/admin/imports') }" to="/admin/imports">导入任务</router-link>
        </nav>
      </section>

      <section class="content-shell ui-panel">
        <router-view />
      </section>
    </main>
  </div>
</template>


<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const router = useRouter()
const route = useRoute()

function goToUserSystem() {
  router.push('/')
}

function logout() {
  auth.logout()
  router.push('/login')
}
</script>
