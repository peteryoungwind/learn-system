<template>
  <div class="app-shell user-shell">
    <header class="app-topbar">
      <div class="brand-block">
        <div class="brand-mark">LS</div>
        <div class="brand-copy">
          <p class="eyebrow">Learning System</p>
          <h1 class="brand-title">学习资料知识库</h1>
        </div>
      </div>
      <div class="topbar-actions">
        <span class="badge-soft">{{ auth.user?.displayName }}</span>
        <el-button v-if="auth.isAdmin" @click="goToAdminSystem">进入管理后台</el-button>
        <el-button text @click="logout">退出</el-button>
      </div>
    </header>

    <main class="app-main">
      <section class="shell-banner ui-panel">
        <div class="banner-copy">
          <div class="system-badge">用户端</div>
          <h2 class="section-title">学习中心</h2>
          <p class="section-copy">按资料浏览、搜索和继续学习三条路径访问内容。</p>
        </div>
        <nav class="app-nav">
          <router-link class="nav-pill" :class="{ 'is-active': route.path === '/' }" to="/">首页</router-link>
          <router-link class="nav-pill" :class="{ 'is-active': route.path.startsWith('/library') }" to="/library">资料浏览</router-link>
          <router-link class="nav-pill" :class="{ 'is-active': route.path.startsWith('/search') }" to="/search">搜索</router-link>
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

function goToAdminSystem() {
  router.push('/admin')
}

function logout() {
  auth.logout()
  router.push('/login')
}
</script>
