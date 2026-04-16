export interface UserProfile {
  id: number
  username: string
  displayName: string
  role: 'ADMIN' | 'USER'
  status: 'INACTIVE' | 'ACTIVE' | 'DISABLED'
  categoryIds: number[]
}

export interface Category {
  id: number
  name: string
  code?: string
  description?: string
  coverUrl?: string
  sortOrder: number
  status: string
}

export interface Album {
  id: number
  categoryId: number
  name: string
  description?: string
  coverUrl?: string
  sortOrder: number
  status: string
}

export interface ImportTask {
  id: number
  importType: string
  status: string
  totalCount: number
  successCount: number
  failCount: number
  errorSummary?: string
  sourceProvider?: string
  sourceBucket?: string
  sourcePrefix?: string
  categoryId?: number
  albumId?: number
  taskName?: string
  createdBy: number
  createdAt: string
  updatedAt?: string
}

export interface ImportTaskItem {
  id: number
  taskId: number
  originalFilename: string
  fileType?: string
  fileSize?: number
  storagePath?: string
  status: string
  errorMessage?: string
  materialId?: number
  createdAt: string
  updatedAt?: string
}

export interface Material {
  id: number
  title: string
  author: string
  fileType: string
  storageProvider: string
  objectKey: string
  originalFilename?: string
  mimeType?: string
  previewObjectKey?: string
  previewStatus?: string
  categoryId: number
  albumId?: number | null
  subtitle?: string
  summary?: string
  coverUrl?: string
  fileSize?: number
  tags?: string
  sortOrder: number
  remark?: string
  publishStatus: string
  publishTime?: string
  ingestTime?: string
}

export interface StorageConfig {
  id?: number
  provider: string
  endpoint: string
  bucket: string
  region?: string
  accessKeyId: string
  accessKeySecret: string
  domain?: string
  basePath?: string
  status: string
  isDefault?: boolean
}

export interface FileTypePreset {
  id: number
  code: string
  name: string
  extensions: string
  mimeTypes?: string
  previewMode: string
  enabled: boolean
  sortOrder: number
}

export interface MaterialPreview {
  viewerType: 'iframe' | 'audio'
  previewUrl?: string
  downloadUrl?: string
  onlineSupported: boolean
  fallbackMessage?: string
  previewStatus?: string
}
