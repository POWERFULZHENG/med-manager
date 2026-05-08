import request from '@/utils/request'
import type { AxiosResponse } from 'axios'

export interface RecordVO {
  id: number
  stockId: number
  medicineId: number
  medicineName: string
  specification: string
  unit: string
  operationType: string
  quantity: number
  operator: string
  remark: string
  createdAt: string
}

export interface RecordQueryRequest {
  medicineName?: string
  operationType?: string
  operator?: string
  startTime?: string
  endTime?: string
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

export const getRecordList = (params: RecordQueryRequest): Promise<AxiosResponse<{
  code: number
  message: string
  data: PageResponse<RecordVO>
}>> => {
  return request.get('/records', { params })
}