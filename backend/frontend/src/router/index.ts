import { createRouter, createWebHistory } from 'vue-router'
import type { RouteLocationNormalized } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import LoginView from '../views/auth/LoginView.vue'
import AdminLayout from '../layouts/AdminLayout.vue'
import UserLayout from '../layouts/UserLayout.vue'
import AdminDashboardView from '../views/admin/AdminDashboardView.vue'
import UserManagementView from '../views/admin/UserManagementView.vue'
import CategoryManagementView from '../views/admin/CategoryManagementView.vue'
import AlbumManagementView from '../views/admin/AlbumManagementView.vue'
import MaterialManagementView from '../views/admin/MaterialManagementView.vue'
import ImportTaskView from '../views/admin/ImportTaskView.vue'
import StorageConfigView from '../views/admin/StorageConfigView.vue'
import AlbumMaterialsView from '../views/admin/AlbumMaterialsView.vue'
import UserHomeView from '../views/user/UserHomeView.vue'
import LibraryView from '../views/user/LibraryView.vue'
import SearchView from '../views/user/SearchView.vue'
import AlbumDetailView from '../views/user/AlbumDetailView.vue'
import MaterialDetailView from '../views/user/MaterialDetailView.vue'

function loginRedirect(to: RouteLocationNormalized) {
  return {
    path: '/login',
    query: {
      redirect: to.fullPath,
      system: String(to.meta.system || 'user'),
    },
  }
}

function resolveDefaultPath(role?: string, system?: string) {
  if (system === 'admin' && role === 'ADMIN') {
    return '/admin'
  }
  return role === 'ADMIN' && system !== 'user' ? '/admin' : '/'
}

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: LoginView },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAuth: true, role: 'ADMIN', system: 'admin' },
      children: [
        { path: '', component: AdminDashboardView, meta: { system: 'admin' } },
        { path: 'users', component: UserManagementView, meta: { system: 'admin' } },
        { path: 'categories', component: CategoryManagementView, meta: { system: 'admin' } },
        { path: 'albums', component: AlbumManagementView, meta: { system: 'admin' } },
        { path: 'albums/:id/materials', component: AlbumMaterialsView, meta: { system: 'admin' } },
        { path: 'materials', component: MaterialManagementView, meta: { system: 'admin' } },
        { path: 'storage', component: StorageConfigView, meta: { system: 'admin' } },
        { path: 'imports', component: ImportTaskView, meta: { system: 'admin' } },
      ],
    },
    {
      path: '/',
      component: UserLayout,
      meta: { requiresAuth: true, system: 'user' },
      children: [
        { path: '', component: UserHomeView, meta: { system: 'user' } },
        { path: 'library', component: LibraryView, meta: { system: 'user' } },
        { path: 'albums/:id', component: AlbumDetailView, meta: { system: 'user' } },
        { path: 'search', component: SearchView, meta: { system: 'user' } },
        { path: 'materials/:id', component: MaterialDetailView, meta: { system: 'user' } },
      ],
    },
  ],
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()

  if (auth.token && !auth.user) {
    try {
      await auth.hydrate()
    } catch {
      auth.logout()
      return loginRedirect(to)
    }
  }

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return loginRedirect(to)
  }

  if (to.path === '/login' && auth.isAuthenticated) {
    const redirect = typeof to.query.redirect === 'string' ? to.query.redirect : ''
    const system = typeof to.query.system === 'string' ? to.query.system : undefined

    if (redirect) {
      if (redirect.startsWith('/admin') && auth.user?.role !== 'ADMIN') {
        return { path: '/', query: { denied: 'admin' } }
      }
      return redirect
    }

    return resolveDefaultPath(auth.user?.role, system)
  }

  if (to.meta.role && auth.user?.role !== to.meta.role) {
    return { path: '/', query: { denied: 'admin' } }
  }

  return true
})

export default router
