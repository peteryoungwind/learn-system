<template>
  <el-upload
    :show-file-list="false"
    :auto-upload="false"
    accept="image/*"
    @change="handleChange"
  >
    <div class="cover-upload-card">
      <img v-if="modelValue" :src="modelValue" alt="cover" class="cover-upload-image" />
      <div v-else class="cover-upload-placeholder">上传封面</div>
    </div>
  </el-upload>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { adminUploadApi } from '../../api/modules'

const props = defineProps<{ modelValue?: string }>()
const emit = defineEmits<{ 'update:modelValue': [value: string] }>()

async function handleChange(uploadFile: any) {
  const raw = uploadFile.raw as File | undefined
  if (!raw) return
  const formData = new FormData()
  formData.append('file', raw)
  const response = await adminUploadApi.image(formData)
  emit('update:modelValue', response.data.url)
  ElMessage.success('上传成功')
}
</script>
