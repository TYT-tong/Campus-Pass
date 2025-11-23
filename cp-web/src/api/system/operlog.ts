import request from '@/utils/request'
import type { ApiResponse, PageResponse } from '@/types/api'

// 操作日志接口
export interface OperlogQuery {
  pageNum?: number
  pageSize?: number
  title?: string
  operName?: string
  businessType?: string
  status?: string
  beginTime?: string
  endTime?: string
}

export interface OperlogItem {
  operId: number
  title: string
  businessType: string
  method: string
  requestMethod: string
  operatorType: string
  operName: string
  deptName: string
  operUrl: string
  operIp: string
  operLocation: string
  operParam: string
  jsonResult: string
  status: string
  errorMsg: string
  operTime: string
  costTime: number
}

// 查询操作日志列表
export function list(query: OperlogQuery): Promise<ApiResponse<PageResponse<OperlogItem>>> {
  return request({
    url: '/system/operlog/list',
    method: 'get',
    params: query,
  })
}

// 删除操作日志
export function delOperlog(operId: number): Promise<ApiResponse<any>> {
  return request({
    url: '/system/operlog/' + operId,
    method: 'delete',
  })
}

// 清空操作日志
export function cleanOperlog(): Promise<ApiResponse<any>> {
  return request({
    url: '/system/operlog/clean',
    method: 'delete',
  })
}