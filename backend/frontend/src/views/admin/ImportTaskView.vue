<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Imports</p>
      <h1 class="page-title">导入任务</h1>
      <p class="page-subtitle">选择分类和专辑后批量上传资料，并查看每个文件的导入结果。</p>
    </section>

    <section class="ui-panel">
      <div class="panel-head">
        <h2 class="section-title">新建导入任务</h2>
        <p class="section-copy">资料会上传到 OSS，并自动创建到目标专辑下。</p>
      </div>

      <el-form :model="form" label-width="90px" class="form-stack">
        <div class="card-grid two-up import-form-grid">
          <el-form-item label="任务名称">
            <el-input v-model="form.taskName" placeholder="例如：Java 入门批量导入" />
          </el-form-item>
          <el-form-item label="发布状态">
            <el-select v-model="form.publishStatus" class="full-width">
              <el-option label="草稿" value="DRAFT" />
              <el-option label="已发布" value="PUBLISHED" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属分类">
            <el-select v-model="form.categoryId" class="full-width" placeholder="请选择分类" @change="handleCategoryChange">
              <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属专辑">
            <el-select v-model="form.albumId" class="full-width" placeholder="请选择专辑">
              <el-option v-for="item in filteredAlbums" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </div>

        <el-form-item label="导入文件">
          <div class="upload-stack full-width">
            <el-upload :auto-upload="false" :show-file-list="false" multiple @change="handleFileChange">
              <el-button type="primary" plain>选择文件</el-button>
            </el-upload>
            <div v-if="selectedFiles.length" class="selected-file-list">
              <div v-for="file in selectedFiles" :key="file.name + file.size" class="selected-file-item">
                <span>{{ file.name }}</span>
                <span class="muted">{{ formatFileSize(file.size) }}</span>
              </div>
            </div>
            <span v-else class="muted">支持一次选择多个文件导入。</span>
          </div>
        </el-form-item>

        <div class="action-row">
          <el-button type="primary" :loading="submitting" @click="submit">开始导入</el-button>
          <el-button @click="resetForm">重置</el-button>
        </div>
      </el-form>
    </section>

    <section class="ui-panel">
      <div class="toolbar toolbar-between">
        <div class="meta-row">
          <span class="badge-soft">{{ tasks.length }} 个任务</span>
          <span class="badge-neutral">支持查看文件明细</span>
        </div>
        <el-button @click="load">刷新</el-button>
      </div>

      <div v-if="!tasks.length" class="empty-shell">
        <el-empty description="暂无导入任务记录" />
      </div>

      <div v-else class="result-grid">
        <article v-for="task in tasks" :key="task.id" class="result-item task-card">
          <div class="summary-row">
            <div>
              <h3 class="card-title">{{ task.taskName || `任务 #${task.id}` }}</h3>
              <p class="card-copy">{{ categoryName(task.categoryId) }} / {{ albumName(task.albumId) }}</p>
            </div>
            <span :class="taskBadgeClass(task.status)">{{ task.status }}</span>
          </div>

          <div class="result-meta">
            <span>总数 {{ task.totalCount }}</span>
            <span>成功 {{ task.successCount }}</span>
            <span>失败 {{ task.failCount }}</span>
            <span>{{ task.createdAt }}</span>
          </div>

          <p class="card-copy">{{ task.errorSummary || '暂无失败摘要' }}</p>

          <div class="action-row">
            <el-button text @click="openItems(task)">查看明细</el-button>
          </div>
        </article>
      </div>
    </section>

    <el-dialog v-model="itemsVisible" title="导入明细" width="840px">
      <div class="toolbar toolbar-between compact-toolbar">
        <span class="badge-soft">{{ currentTaskTitle }}</span>
        <span class="badge-neutral">{{ taskItems.length }} 个文件</span>
      </div>
      <div class="table-shell">
        <el-table :data="taskItems">
          <el-table-column prop="originalFilename" label="文件名" min-width="220" />
          <el-table-column prop="fileType" label="类型" width="110" />
          <el-table-column label="大小" width="120">
            <template #default="scope">
              {{ formatFileSize(scope.row.fileSize || 0) }}
            </template>
          </el-table-column>
          <el-table-column prop="storagePath" label="OSS路径" min-width="240" />
          <el-table-column label="状态" width="130">
            <template #default="scope">
              <span :class="taskBadgeClass(scope.row.status)">{{ scope.row.status }}</span>
            </template>
          </el-table-column>
          <el-table-column label="错误信息" min-width="220">
            <template #default="scope">
              <span class="muted">{{ scope.row.errorMessage || '—' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { adminAlbumApi, adminCategoryApi, adminImportApi } from '../../api/modules'
import type { Album, Category, ImportTask, ImportTaskItem } from '../../types'

const tasks = ref<ImportTask[]>([])
const categories = ref<Category[]>([])
const albums = ref<Album[]>([])
const selectedFiles = ref<File[]>([])
const submitting = ref(false)
const itemsVisible = ref(false)
const taskItems = ref<ImportTaskItem[]>([])
const currentTaskTitle = ref('')

const form = reactive<ImportForm>({
  taskName: '',
  categoryId: undefined,
  albumId: undefined,
  publishStatus: 'DRAFT',
})

interface ImportForm {
  taskName: string
  categoryId?: number
  albumId?: number
  publishStatus: string
}

const filteredAlbums = computed(() => albums.value.filter((item) => item.categoryId === form.categoryId))

async function load() {
  const [taskResp, categoryResp, albumResp] = await Promise.all([
    adminImportApi.list(),
    adminCategoryApi.list(),
    adminAlbumApi.list(),
  ])
  tasks.value = taskResp.data
  categories.value = categoryResp.data
  albums.value = albumResp.data

  if (!form.categoryId && categories.value.length) {
    form.categoryId = categories.value[0].id
  }
  if (!form.albumId && filteredAlbums.value.length) {
    form.albumId = filteredAlbums.value[0].id
  }
}

function handleCategoryChange() {
  form.albumId = filteredAlbums.value[0]?.id
}

function handleFileChange(uploadFile: any) {
  const raw = uploadFile.raw as File | undefined
  if (!raw) return
  const exists = selectedFiles.value.some((item) => item.name === raw.name && item.size === raw.size)
  if (!exists) {
    selectedFiles.value = [...selectedFiles.value, raw]
  }
}

function resetForm() {
  Object.assign(form, {
    taskName: '',
    categoryId: categories.value[0]?.id,
    albumId: albums.value.find((item) => item.categoryId === categories.value[0]?.id)?.id,
    publishStatus: 'DRAFT',
  })
  selectedFiles.value = []
}

async function submit() {
  if (!form.categoryId || !form.albumId) {
    ElMessage.warning('请选择分类和专辑')
    return
  }
  if (!selectedFiles.value.length) {
    ElMessage.warning('请先选择导入文件')
    return
  }

  submitting.value = true
  try {
    const formData = new FormData()
    formData.append('taskName', form.taskName)
    formData.append('categoryId', String(form.categoryId))
    formData.append('albumId', String(form.albumId))
    formData.append('publishStatus', form.publishStatus)
    selectedFiles.value.forEach((file) => formData.append('files', file))
    await adminImportApi.create(formData)
    ElMessage.success('导入任务已创建')
    resetForm()
    await load()
  } finally {
    submitting.value = false
  }
}

async function openItems(task: ImportTask) {
  const response = await adminImportApi.items(task.id)
  taskItems.value = response.data
  currentTaskTitle.value = task.taskName || `任务 #${task.id}`
  itemsVisible.value = true
}

function categoryName(id?: number) {
  return categories.value.find((item) => item.id === id)?.name || '未指定分类'
}

function albumName(id?: number) {
  return albums.value.find((item) => item.id === id)?.name || '未指定专辑'
}

function taskBadgeClass(status: string) {
  if (status === 'SUCCESS') return 'badge-success'
  if (status === 'FAILED') return 'badge-danger'
  if (status === 'PARTIAL_SUCCESS') return 'badge-warning'
  return 'badge-neutral'
}

function formatFileSize(size: number) {
  if (!size) return '0 B'
  if (size < 1024) return `${size} B`
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`
  return `${(size / 1024 / 1024).toFixed(1)} MB`
}

onMounted(load)
</script>
