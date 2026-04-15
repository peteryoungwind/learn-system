import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import LoginView from '../views/auth/LoginView.vue';
import AdminLayout from '../layouts/AdminLayout.vue';
import UserLayout from '../layouts/UserLayout.vue';
import AdminDashboardView from '../views/admin/AdminDashboardView.vue';
import UserManagementView from '../views/admin/UserManagementView.vue';
import CategoryManagementView from '../views/admin/CategoryManagementView.vue';
import AlbumManagementView from '../views/admin/AlbumManagementView.vue';
import MaterialManagementView from '../views/admin/MaterialManagementView.vue';
import UserHomeView from '../views/user/UserHomeView.vue';
import LibraryView from '../views/user/LibraryView.vue';
import MaterialDetailView from '../views/user/MaterialDetailView.vue';
const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/login', component: LoginView },
        {
            path: '/admin',
            component: AdminLayout,
            meta: { requiresAuth: true, role: 'ADMIN' },
            children: [
                { path: '', component: AdminDashboardView },
                { path: 'users', component: UserManagementView },
                { path: 'categories', component: CategoryManagementView },
                { path: 'albums', component: AlbumManagementView },
                { path: 'materials', component: MaterialManagementView },
            ],
        },
        {
            path: '/',
            component: UserLayout,
            meta: { requiresAuth: true },
            children: [
                { path: '', component: UserHomeView },
                { path: 'library', component: LibraryView },
                { path: 'materials/:id', component: MaterialDetailView },
            ],
        },
    ],
});
router.beforeEach(async (to) => {
    const auth = useAuthStore();
    if (auth.token && !auth.user) {
        try {
            await auth.hydrate();
        }
        catch {
            auth.logout();
            return '/login';
        }
    }
    if (to.meta.requiresAuth && !auth.isAuthenticated) {
        return '/login';
    }
    if (to.path === '/login' && auth.isAuthenticated) {
        return auth.user?.role === 'ADMIN' ? '/admin' : '/';
    }
    if (to.meta.role && auth.user?.role !== to.meta.role) {
        return auth.user?.role === 'ADMIN' ? '/admin' : '/';
    }
    return true;
});
export default router;
