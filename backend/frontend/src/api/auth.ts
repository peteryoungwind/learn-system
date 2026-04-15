import api from './http'
import type { UserProfile } from '../types'

export async function login(username: string, password: string) {
  return api.post('/api/auth/login', { username, password }) as Promise<{
    success: boolean
    data: { token: string; user: UserProfile }
  }>
}

export async function fetchCurrentUser() {
  return api.get('/api/auth/me') as Promise<{ success: boolean; data: UserProfile }>
}
