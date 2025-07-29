import { defineStore } from 'pinia'
import 'pinia-plugin-persistedstate'

interface UserState {
    token: string
}

export const useUserStore = defineStore('user', {
    state: (): UserState => ({
        token: '',
    }),
    actions: {
        setToken(token: string) {
            this.token = token
            // localStorage.setItem('token', token)
        },
        clearToken() {
            this.token = ''
            // localStorage.removeItem('token')
        }
    },
    persist: true
}) 