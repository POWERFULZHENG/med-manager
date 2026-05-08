import request from '@/utils/request'
import type { AxiosResponse } from 'axios'

export interface MedicineVO {
  id: number
  medicineName: string
  specification: string
  manufacturer: string
  unit: string
  dosageForm: string
  remark: string
  createdAt: string
  updatedAt: string
}

export interface MedicineRequest {
  medicineName: string
  specification?: string
  manufacturer?: string
  unit: string
  dosageForm?: string
  remark?: string
}

export interface MedicineQueryRequest {
  medicineName?: string
  manufacturer?: string
  dosageForm?: string
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

export const getMedicineList = (params: MedicineQueryRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: PageResponse<MedicineVO>
}>> => {
  return request.get('/medicines', { params })
}

export const getMedicineById = (id: number): Promise<AxiosResponse<{
  code: number
  message: string
  data: MedicineVO
}>> => {
  return request.get(`/medicines/${id}`)
}

export const createMedicine = (data: MedicineRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: MedicineVO
}>> => {
  return request.post('/medicines', data)
}

export const updateMedicine = (id: number, data: MedicineRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: MedicineVO
}>> => {
  return request.put(`/medicines/${id}`, data)
}

export const deleteMedicine = (id: number): Promise<AxiosResponse<{
  code: number
  message: string
}>> => {
  return request.delete(`/medicines/${id}`)
}