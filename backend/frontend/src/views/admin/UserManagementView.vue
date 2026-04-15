<template>
  <div>
    <h1 class="page-title">用户管理</h1>
    <div class="toolbar">
      <el-button type="primary" @click="openCreate">新建用户</el-button>
    </div>
    <el-table :data="users">
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="displayName" label="显示名" />
      <el-table-column prop="role" label="角色" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="280">
        <template #default="scope">
          <el-button text @click="openEdit(scope.row)">编辑</el-button>
          <el-button text @click="openPermissions(scope.row)">权限</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="visible" :title="editingId ? '编辑用户' : '新建用户'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名" v-if="!editingId"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="显示名"><el-input v-model="form.displayName" /></el-form-item>
        <el-form-item label="密码" v-if="!editingId"><el-input v-model="form.password" type="password" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role"><el-option label="管理员" value="ADMIN" /><el-option label="普通用户" value="USER" /></el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status"><el-option label="激活" value="ACTIVE" /><el-option label="未激活" value="INACTIVE" /><el-option label="停用" value="DISABLED" /></el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="permissionVisible" title="分类授权">
      <el-checkbox-group v-model="permissionForm.categoryIds">
        <el-checkbox v-for="item in categories" :key="item.id" :value="item.id">{{ item.name }}</el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="permissionVisible = false">取消</el-button>
        <el-button type="primary" @click="savePermissions">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { adminCategoryApi, adminUserApi } from '../../api/modules'
import type { Category, UserProfile } from '../../types'

const users = ref<UserProfile[]>([])
const categories = ref<Category[]>([])
const visible = ref(false)
const permissionVisible = ref(false)
const editingId = ref<number | null>(null)
const permissionUserId = ref<number | null>(null)
const form = reactive<any>({ username: '', displayName: '', password: '', role: 'USER', status: 'ACTIVE' })
const permissionForm = reactive<{ categoryIds: number[] }>({ categoryIds: [] })

async function load() {
  const [userResp, categoryResp] = await Promise.all([adminUserApi.list(), adminCategoryApi.list()])
  users.value = userResp.data
  categories.value = categoryResp.data
}

function openCreate() {
  editingId.value = null
  Object.assign(form, { username: '', displayName: '', password: '', role: 'USER', status: 'ACTIVE' })
  visible.value = true
}

function openEdit(user: UserProfile) {
  editingId.value = user.id
  Object.assign(form, { displayName: user.displayName, role: user.role, status: user.status })
  visible.value = true
}

function openPermissions(user: UserProfile) {
  permissionUserId.value = user.id
  permissionForm.categoryIds = [...user.categoryIds]
  permissionVisible.value = true
}

async function submit() {
  if (editingId.value) {
    await adminUserApi.update(editingId.value, form)
  } else {
    await adminUserApi.create(form)
  }
  visible.value = false
  ElMessage.success('保存成功')
  await load()
}

async function savePermissions() {
  await adminUserApi.updatePermissions(permissionUserId.value!, permissionForm)
  permissionVisible.value = false
  ElMessage.success('权限已更新')
  await load()
}

onMounted(load)
</script>
