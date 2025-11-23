/**
 * 缓存监控相关API接口（TypeScript版本）
 */

import request from '@/utils/request'
import type { ApiResponse } from '@/types/api'

/**
 * 查询缓存列表
 */
export function getCacheList(name: string): Promise<ApiResponse<any>> {
  return request({
    url: `/system/cache/list/${name}`,
    method: 'get',
  })
}

/**
 * 查询缓存键值
 */
export function getCacheKey(name: string): Promise<ApiResponse<any>> {
  return request({
    url: `/system/cache/key/${name}`,
    method: 'get',
  })
}