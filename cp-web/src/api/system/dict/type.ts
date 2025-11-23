import request from '@/utils/request'
import type { ApiResponse, PageResponse } from '@/types/api'

// 字典类型查询参数
export interface DictTypeQuery {
  pageNum?: number
  pageSize?: number
  dictName?: string
  dictType?: string
  status?: string
  beginTime?: string
  endTime?: string
}

// 字典类型项接口
export interface DictTypeItem {
  dictId: number
  dictName: string
  dictType: string
  status: string
  createBy: string
  createTime: string
  updateBy: string
  updateTime: string
  remark: string
}

// 查询字典类型列表
export function listType(query: DictTypeQuery): Promise<ApiResponse<PageResponse<DictTypeItem>>> {
  return request({
    url: '/system/dict/type/list',
    method: 'get',
    params: query
  })
}

// 查询字典类型详细
export function getType(dictId: number): Promise<ApiResponse<DictTypeItem>> {
  return request({
    url: '/system/dict/type/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addType(data: DictTypeItem): Promise<ApiResponse<any>> {
  return request({
    url: '/system/dict/type',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function updateType(data: DictTypeItem): Promise<ApiResponse<any>> {
  return request({
    url: '/system/dict/type',
    method: 'put',
    data: data
  })
}

// 删除字典类型
export function delType(dictId: number): Promise<ApiResponse<any>> {
  return request({
    url: '/system/dict/type/' + dictId,
    method: 'delete'
  })
}

// 刷新字典缓存
export function refreshCache(): Promise<ApiResponse<any>> {
  return request({
    url: '/system/dict/type/refreshCache',
    method: 'delete'
  })
}

// 获取字典选择框列表
export function optionselect(): Promise<ApiResponse<DictTypeItem[]>> {
  return request({
    url: '/system/dict/type/optionselect',
    method: 'get'
  })
}