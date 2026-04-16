<template>
  <div class="login-shell">
    <div class="login-panel">
      <section class="login-portal ui-panel">
        <div class="banner-copy">
          <div class="system-badge">统一入口</div>
          <p class="eyebrow">Knowledge Portal</p>
          <h1 class="hero-title">学习资料知识库</h1>
          <p class="hero-copy">同一套账号进入学习端或后台工作台，页面样式参考原型，但保留现有登录和跳转逻辑。</p>
        </div>
        <div class="portal-grid">
          <article class="portal-card">
            <span class="badge-soft">用户端</span>
            <h2 class="card-title">浏览、搜索与学习</h2>
            <p class="card-copy">用于查看已授权分类、浏览资料并继续学习。</p>
          </article>
          <article class="portal-card">
            <span class="badge-soft">管理员端</span>
            <h2 class="card-title">运营、维护与导入</h2>
            <p class="card-copy">用于管理用户、分类、专辑、资料和导入任务。</p>
          </article>
        </div>
        <div class="info-card">
          <div class="info-key">当前目标系统</div>
          <div class="info-value">登录后进入{{ targetLabel }}</div>
        </div>
      </section>

      <section class="login-form-card ui-panel">
        <div class="section-head">
          <p class="eyebrow">Sign in</p>
          <h2 class="section-title">登录系统</h2>
          <p class="section-copy">输入账号密码后进入对应系统。</p>
        </div>

        <el-alert
          v-if="isAdminDenied"
          title="普通用户不能进入管理员端，请登录后进入学习中心。"
          type="warning"
          :closable="false"
          style="margin-bottom: 16px;"
        />

        <el-form :model="form" class="field-grid" @submit.prevent="submit">
          <el-form-item label="用户名">
            <el-input v-model="form.username" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" show-password />
          </el-form-item>
          <el-button type="primary" :loading="loading" class="full-width" @click="submit">登录</el-button>
        </el-form>
        <div class="form-note">默认管理员：admin / admin123</div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../../api/auth'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'admin123' })

const targetSystem = computed(() => (route.query.system === 'admin' ? 'admin' : 'user'))
const targetLabel = computed(() => (targetSystem.value === 'admin' ? '管理员端' : '用户端'))
const isAdminDenied = computed(() => route.query.denied === 'admin')

async function submit() {
  loading.value = true
  try {
    const response = await login(form.username, form.password)
    auth.setSession(response.data.token, response.data.user)

    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : ''
    if (redirect) {
      if (redirect.startsWith('/admin') && response.data.user.role !== 'ADMIN') {
        ElMessage.error('普通用户不能进入管理员端')
        await router.push({ path: '/', query: { denied: 'admin' } })
        return
      }
      await router.push(redirect)
      return
    }

    const destination = targetSystem.value === 'admin' && response.data.user.role === 'ADMIN' ? '/admin' : '/'
    if (targetSystem.value === 'admin' && response.data.user.role !== 'ADMIN') {
      ElMessage.error('普通用户不能进入管理员端')
    }
    await router.push(destination)
  } catch (error) {
    ElMessage.error((error as Error).message)
  } finally {
    loading.value = false
  }
}
</script>
