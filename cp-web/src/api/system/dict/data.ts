import request from '@/utils/request'
import type { ApiResponse, PageResponse } from '@/types/api'

// 字典数据查询参数
export interface DictDataQuery {
  pageNum?: number
  pageSize?: number
  dictType?: string
  dictLabel?: string
  status?: string
  beginTime?: string
  endTime?: string
}

// 字典数据项接口
export interface DictDataItem {
  dictCode: number
  dictSort: number
  dictLabel: string
  dictValue: string
  dictType: string
  cssClass: string
  listClass: string
  isDefault: string
  status: string
  createBy: string
  createTime: string
  updateBy: string
  updateTime: string
  remark: string
}

// 查询字典数据列表
export function listData(query: DictDataQuery): Promise<ApiResponse<PageResponse<DictDataItem>>> {
  return request({
    url: '/system/dict/data/list',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getData(dictCode: number): Promise<ApiResponse<DictDataItem>> {
  return request({
    url: '/system/dict/data/' + dictCode,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType: string): Promise<ApiResponse<DictDataItem[]>> {
  return request({
    url: '/system/dict/data/type/' + dictType,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data: DictDataItem): Promise<ApiResponse<any>> {
  return request({
    url: '/system/dict/data',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateData(data: DictDataItem): Promise<ApiResponse<any>> {
  return request({
    url: '/system/dict/data',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delData(dictCode: number): Promise<ApiResponse<any>> {
  return request({
    url: '/system/dict/data/' + dictCode,
    method: 'delete'
  })
}