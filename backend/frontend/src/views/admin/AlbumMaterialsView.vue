<template>
  <div class="page-stack">
    <section class="section-head">
      <p class="eyebrow">Album</p>
      <h1 class="page-title">专辑内资料</h1>
      <p class="page-subtitle">查看专辑下的资料文件及上传结果。</p>
    </section>

    <section class="ui-panel">
      <div class="toolbar">
        <span class="badge-soft">{{ filteredMaterials.length }} 条资料</span>
      </div>
      <div class="table-shell">
        <el-table :data="filteredMaterials">
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="author" label="作者" />
          <el-table-column prop="fileType" label="类型" />
          <el-table-column prop="objectKey" label="OSS路径" min-width="240" />
          <el-table-column prop="previewStatus" label="预览状态" />
          <el-table-column prop="publishStatus" label="发布状态" />
        </el-table>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { adminMaterialApi } from '../../api/modules'
import type { Material } from '../../types'

const route = useRoute()
const materials = ref<Material[]>([])
const albumId = Number(route.params.id)

const filteredMaterials = computed(() => materials.value.filter((item) => item.albumId === albumId))

async function load() {
  const response = await adminMaterialApi.list()
  materials.value = response.data
}

onMounted(load)
</script>
