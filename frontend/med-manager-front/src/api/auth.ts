

import request from '@/utils/request'

export interface LoginData {
  username: string
  password: string
}

export interface LoginVO {
  userId: number
  username: string
  nickname: string
  roleCode: string
  permissions: string[]
}

export interface LoginResponse {
  code: number
  message: string
  data: {
    token: string
    user: LoginVO
  }
}

export const login = (data: LoginData): Promise<LoginResponse> => {
  return request.post('/auth/login', data)
}

export const logout = (): Promise<any> => {
  return request.post('/auth/logout')
}

export const getCurrentUser = (): Promise<any> => {
  return request.get('/auth/info')
}

