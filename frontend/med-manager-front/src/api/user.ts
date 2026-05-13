import request from '@/utils/request'
import type { AxiosResponse } from 'axios'

export interface UserVO {
  id: number
  username: string
  nickname: string
  phone: string
  roleId: number
  roleName: string
  status: number
  createdAt: string
  updatedAt: string
}

export interface UserRequest {
  username: string
  password?: string
  nickname: string
  phone?: string
  roleId?: number
  status?: number
}

export interface UserQueryRequest {
  username?: string
  nickname?: string
  phone?: string
  status?: number
  roleId?: number
  pageNum?: number
  pageSize?: number
}

export interface PageResponse<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

export const getUserList = (params: UserQueryRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: PageResponse<UserVO>
}>> => {
  return request.get('/users', { params })
}

export const getUserById = (id: number): Promise<AxiosResponse<{
  code: number
  message: string
  data: UserVO
}>> => {
  return request.get(`/users/${id}`)
}

export const createUser = (data: UserRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: UserVO
}>> => {
  return request.post('/users', data)
}

export const updateUser = (id: number, data: UserRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: UserVO
}>> => {
  return request.put(`/users/${id}`, data)
}

export const deleteUser = (id: number): Promise<AxiosResponse<{
  code: number
  message: string
}>> => {
  return request.delete(`/users/${id}`)
}

export const updateUserStatus = (id: number, status: number): Promise<AxiosResponse<{
  code: number
  message: string
  data: UserVO
}>> => {
  return request.put(`/users/${id}/status`, {}, { params: { status } })
}
