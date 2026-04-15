<template>
  <div class="page-shell" style="display: flex; align-items: center; justify-content: center;">
    <div class="glass-card" style="width: 420px; padding: 32px;">
      <h1 class="page-title">登录系统</h1>
      <el-form :model="form" @submit.prevent="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-button type="primary" :loading="loading" style="width: 100%" @click="submit">登录</el-button>
      </el-form>
      <div style="margin-top: 12px; color: #6b7280; font-size: 12px;">默认管理员：admin / admin123</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../../api/auth'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'admin123' })

async function submit() {
  loading.value = true
  try {
    const response = await login(form.username, form.password)
    auth.setSession(response.data.token, response.data.user)
    router.push(response.data.user.role === 'ADMIN' ? '/admin' : '/')
  } catch (error) {
    ElMessage.error((error as Error).message)
  } finally {
    loading.value = false
  }
}
</script>
