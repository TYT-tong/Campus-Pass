import request from '@/utils/request'
import type {
  ApiResponse,
  PageResponse,

} from '@/types/api'
import type { ParentType, ParentQuery } from '#/personnel'

export function getParentList(params: ParentQuery): Promise<ApiResponse<PageResponse<ParentType>>> {
  return request({
    url: '/personnel/parent/list',
    method: 'get',
    params,
  })
}
