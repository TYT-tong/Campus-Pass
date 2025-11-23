export interface StudentType {
  studentId: number
  cardId: string
  userName: string
  studentName: string
  age: number
  sex: string
  phone: string
  parentName: string
  stuClass: string
  grade: string
  status?: string
}
export interface ParentType {
  parentId: number
  cardId: string
  userName: string
  parentName: string
  age: number
  sex: string
  phone: string
  studentName: string
  status?: string
}

export interface StudentQuery {
  pageNum?: number
  pageSize?: number
  keyword?: string
  grade?: string
  class?: string
  status?: string
}


export interface ParentQuery {
  pageNum?: number
  pageSize?: number
  parentName?: string
  phone?: string
  status?: string
}
