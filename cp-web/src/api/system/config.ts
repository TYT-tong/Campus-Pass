import request from "@/utils/request";
import type { ApiResponse, PageResponse } from '@/types/api'

// 参数配置接口
export interface ConfigQuery {
  pageNum?: number
  pageSize?: number
  configName?: string
  configKey?: string
  configType?: string
  beginTime?: string
  endTime?: string
}

export interface ConfigItem {
  configId: number
  configName: string
  configKey: string
  configValue: string
  configType: string
  createBy: string
  createTime: string
  updateBy: string
  updateTime: string
  remark: string
}

// 查询参数列表
export function listConfig(query: ConfigQuery): Promise<ApiResponse<PageResponse<ConfigItem>>> {
	return request({
		url: "/system/config/list",
		method: "get",
		params: query,
	});
}

// 查询参数详细
export function getConfig(configId: number): Promise<ApiResponse<ConfigItem>> {
	return request({
		url: "/system/config/" + configId,
		method: "get",
	});
}

// 根据参数键名查询参数值
export function getConfigKey(configKey: string): Promise<ApiResponse<string>> {
	return request({
		url: "/system/config/configKey/" + configKey,
		method: "get",
	});
}

// 新增参数配置
export function addConfig(data: ConfigItem): Promise<ApiResponse<any>> {
	return request({
		url: "/system/config",
		method: "post",
		data: data,
	});
}

// 修改参数配置
export function updateConfig(data: ConfigItem): Promise<ApiResponse<any>> {
	return request({
		url: "/system/config",
		method: "put",
		data: data,
	});
}

// 删除参数配置
export function delConfig(configId: number): Promise<ApiResponse<any>> {
	return request({
		url: "/system/config/" + configId,
		method: "delete",
	});
}

// 刷新参数缓存
export function refreshCache(): Promise<ApiResponse<any>> {
	return request({
		url: "/system/config/refreshCache",
		method: "delete",
	});
}