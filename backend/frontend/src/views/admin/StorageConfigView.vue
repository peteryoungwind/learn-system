<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Storage</p>
      <h1 class="page-title">存储配置</h1>
      <p class="page-subtitle">统一维护全局 OSS 存储配置，上传与预览都从这里读取。</p>
    </section>

    <section class="ui-panel">
      <el-form :model="form" label-width="120px" class="storage-form">
        <el-form-item label="存储商">
          <el-input v-model="form.provider" placeholder="如 OSS" />
        </el-form-item>
        <el-form-item label="Endpoint">
          <el-input v-model="form.endpoint" />
        </el-form-item>
        <el-form-item label="Bucket">
          <el-input v-model="form.bucket" />
        </el-form-item>
        <el-form-item label="Region">
          <el-input v-model="form.region" />
        </el-form-item>
        <el-form-item label="AccessKeyId">
          <el-input v-model="form.accessKeyId" />
        </el-form-item>
        <el-form-item label="AccessKeySecret">
          <el-input v-model="form.accessKeySecret" show-password />
        </el-form-item>
        <el-form-item label="访问域名">
          <el-input v-model="form.domain" placeholder="https://cdn.example.com" />
        </el-form-item>
        <el-form-item label="基础目录">
          <el-input v-model="form.basePath" placeholder="learn-system" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="停用" value="DISABLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">保存配置</el-button>
        </el-form-item>
      </el-form>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { adminStorageApi } from '../../api/modules'
import type { StorageConfig } from '../../types'

const form = reactive<StorageConfig>({
  provider: 'OSS',
  endpoint: '',
  bucket: '',
  region: '',
  accessKeyId: '',
  accessKeySecret: '',
  domain: '',
  basePath: 'learn-system',
  status: 'ACTIVE',
})

async function load() {
  const response = await adminStorageApi.get()
  if (response.data) {
    Object.assign(form, response.data)
  }
}

async function submit() {
  await adminStorageApi.update(form)
  ElMessage.success('保存成功')
}

onMounted(load)
</script>
