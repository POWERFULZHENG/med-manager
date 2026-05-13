import request from '@/utils/request'
import type { AxiosResponse } from 'axios'

export interface StockVO {
  id: number
  medicineId: number
  medicineName: string
  specification: string
  manufacturer: string
  unit: string
  batchNo: string
  quantity: number
  expireDate: string
  storageLocation: string
  remark: string
  createdAt: string
  updatedAt: string
  expireDays: number
}

export interface StockInRequest {
  medicineId: number
  batchNo: string
  quantity: number
  expireDate: string
  storageLocation?: string
  remark?: string
}

export interface StockOutRequest {
  stockId: number
  quantity: number
  remark?: string
}

export interface StockAdjustRequest {
  quantity: number
  remark?: string
}

export interface StockQueryRequest {
  medicineName?: string
  isExpiring?: boolean
  isExpired?: boolean
  storageLocation?: string
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

export const getStockList = (params: StockQueryRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: PageResponse<StockVO>
}>> => {
  return request.get('/stocks', { params })
}

export const getStockById = (id: number): Promise<AxiosResponse<{
  code: number
  message: string
  data: StockVO
}>> => {
  return request.get(`/stocks/${id}`)
}

export const stockIn = (data: StockInRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: StockVO
}>> => {
  return request.post('/stocks/in', data)
}

export const stockOut = (data: StockOutRequest): Promise<AxiosResponse<{
  code: number
  message: string
}>> => {
  return request.post('/stocks/out', data)
}

export const adjustStock = (id: number, data: StockAdjustRequest): Promise<AxiosResponse<{
  code: number
  message: string
}>> => {
  return request.put(`/stocks/${id}/adjust`, data)
}

export const getExpiringStocks = (): Promise<AxiosResponse<{
  code: number
  message: string
  data: StockVO[]
}>> => {
  return request.get('/stocks/expiring')
}

export const getExpiredStocks = (): Promise<AxiosResponse<{
  code: number
  message: string
  data: StockVO[]
}>> => {
  return request.get('/stocks/expired')
}