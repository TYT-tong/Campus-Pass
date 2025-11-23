import request from '@/utils/request'
import type { ApiResponse, ServerInfo } from '@/types/api'

/**
 * 获取服务器信息
 */
export function getServerInfo(): Promise<ApiResponse<ServerInfo>> {
  return request({
    url: '/monitor/server',
    method: 'get',
  })
}

/**
 * 获取缓存信息
 */
export function getCacheInfo(): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/cache',
    method: 'get',
  })
}

/**
 * 获取在线用户列表
 */
export function getOnlineUsers(params: any): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params,
  })
}

/**
 * 强退用户
 */
export function forceLogout(tokenId: string): Promise<ApiResponse<any>> {
  return request({
    url: `/monitor/online/${tokenId}`,
    method: 'delete',
  })
}

/**
 * 获取定时任务列表
 */
export function getJobList(params: any): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/job/list',
    method: 'get',
    params,
  })
}

/**
 * 获取定时任务详情
 */
export function getJobDetail(jobId: number): Promise<ApiResponse<any>> {
  return request({
    url: `/monitor/job/${jobId}`,
    method: 'get',
  })
}

/**
 * 新增定时任务
 */
export function addJob(data: any): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/job',
    method: 'post',
    data,
  })
}

/**
 * 修改定时任务
 */
export function updateJob(data: any): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/job',
    method: 'put',
    data,
  })
}

/**
 * 删除定时任务
 */
export function deleteJob(jobIds: number[]): Promise<ApiResponse<any>> {
  return request({
    url: `/monitor/job/${jobIds.join(',')}`,
    method: 'delete',
  })
}

/**
 * 修改定时任务状态
 */
export function changeJobStatus(jobId: number, status: string): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/job/changeStatus',
    method: 'put',
    data: { jobId, status },
  })
}

/**
 * 立即执行定时任务
 */
export function runJob(jobId: number, jobGroup: string): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/job/run',
    method: 'put',
    data: { jobId, jobGroup },
  })
}

/**
 * 获取定时任务日志列表
 */
export function getJobLogList(params: any): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/jobLog/list',
    method: 'get',
    params,
  })
}

/**
 * 删除定时任务日志
 */
export function deleteJobLog(logIds: number[]): Promise<ApiResponse<any>> {
  return request({
    url: `/monitor/jobLog/${logIds.join(',')}`,
    method: 'delete',
  })
}

/**
 * 清空定时任务日志
 */
export function cleanJobLog(): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/jobLog/clean',
    method: 'delete',
  })
}