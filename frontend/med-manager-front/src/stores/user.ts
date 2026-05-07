import { defineStore } from 'pinia'
import { ref } from 'vue'
import env from '@/utils/env'

interface UserInfo {
  id: number
  username: string
  nickname?: string
  avatar?: string
  roleCode?: string
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(localStorage.getItem(env.app.tokenKey))
  const userInfo = ref<UserInfo | null>(null)

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem(env.app.tokenKey, newToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem(env.app.userInfoKey, JSON.stringify(info))
  }

  const logout = () => {
    token.value = null
    userInfo.value = null
    localStorage.removeItem(env.app.tokenKey)
    localStorage.removeItem(env.app.userInfoKey)
  }

  const initFromStorage = () => {
    const storedUserInfo = localStorage.getItem(env.app.userInfoKey)
    if (storedUserInfo) {
      userInfo.value = JSON.parse(storedUserInfo)
    }
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    logout,
    initFromStorage
  }
})

