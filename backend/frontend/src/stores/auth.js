import { defineStore } from 'pinia';
import { fetchCurrentUser } from '../api/auth';
export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        user: null,
    }),
    getters: {
        isAuthenticated: (state) => Boolean(state.token),
        isAdmin: (state) => state.user?.role === 'ADMIN',
    },
    actions: {
        setSession(token, user) {
            this.token = token;
            this.user = user;
            localStorage.setItem('token', token);
        },
        async hydrate() {
            if (!this.token)
                return;
            const response = await fetchCurrentUser();
            this.user = response.data;
        },
        logout() {
            this.token = '';
            this.user = null;
            localStorage.removeItem('token');
        },
    },
});
