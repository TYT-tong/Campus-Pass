import request from '@/utils/request'
import type {
  ApiResponse,
  PageResponse,

} from '@/types/api'
import type { StudentType, StudentQuery } from '#/personnel'

export function getStudentList(params: StudentQuery): Promise<ApiResponse<PageResponse<StudentType>>> {
  return request({
    url: '/personnel/student/list',
    method: 'get',
    params,
  })
}
