import api from './http'

export const adminUserApi = {
  list: () => api.get('/api/admin/users'),
  create: (payload: unknown) => api.post('/api/admin/users', payload),
  update: (id: number, payload: unknown) => api.put(`/api/admin/users/${id}`, payload),
  updateStatus: (id: number, payload: unknown) => api.put(`/api/admin/users/${id}/status`, payload),
  updatePermissions: (id: number, payload: unknown) => api.put(`/api/admin/users/${id}/permissions/categories`, payload),
}

export const adminCategoryApi = {
  list: () => api.get('/api/admin/categories'),
  create: (payload: unknown) => api.post('/api/admin/categories', payload),
  update: (id: number, payload: unknown) => api.put(`/api/admin/categories/${id}`, payload),
  remove: (id: number) => api.delete(`/api/admin/categories/${id}`),
}

export const adminAlbumApi = {
  list: () => api.get('/api/admin/albums'),
  create: (payload: unknown) => api.post('/api/admin/albums', payload),
  update: (id: number, payload: unknown) => api.put(`/api/admin/albums/${id}`, payload),
  remove: (id: number) => api.delete(`/api/admin/albums/${id}`),
}

export const adminMaterialApi = {
  list: () => api.get('/api/admin/materials'),
  create: (payload: unknown) => api.post('/api/admin/materials', payload),
  update: (id: number, payload: unknown) => api.put(`/api/admin/materials/${id}`, payload),
  remove: (id: number) => api.delete(`/api/admin/materials/${id}`),
}

export const userLearningApi = {
  categories: () => api.get('/api/me/categories'),
  albums: (categoryId?: number) => api.get('/api/me/albums', { params: { categoryId } }),
  materials: (params?: Record<string, unknown>) => api.get('/api/me/materials', { params }),
  detail: (id: number) => api.get(`/api/me/materials/${id}`),
  preview: (id: number) => api.get(`/api/me/materials/${id}/preview`),
  access: (id: number) => api.post(`/api/me/materials/${id}/access`),
  progress: (id: number, payload: unknown) => api.put(`/api/me/materials/${id}/progress`, payload),
  continueLearning: () => api.get('/api/me/continue-learning'),
}
