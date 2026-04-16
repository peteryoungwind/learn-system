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
  code: string
  description?: string
  sortOrder: number
  status: string
}

export interface Album {
  id: number
  categoryId: number
  name: string
  description?: string
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
  createdBy: number
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
