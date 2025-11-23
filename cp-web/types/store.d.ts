import type { UserInfo } from './api'

export interface UserState {
  token: string
  id: string
  name: string
  nickName: string
  avatar: string
  roles: string[]
  permissions: string[]
  expires_in: string
}

export interface AppState {
  sidebar: {
    opened: boolean
    withoutAnimation: boolean
  }
  device: 'desktop' | 'mobile'
  size: 'large' | 'default' | 'small'
}

export interface PermissionState {
  routes: any[]
  addRoutes: any[]
  defaultRoutes: any[]
  topbarRouters: any[]
  sidebarRouters: any[]
}

export interface SettingsState {
  title: string
  theme: string
  sideTheme: string
  showSettings: boolean
  topNav: boolean
  tagsView: boolean
  fixedHeader: boolean
  sidebarLogo: boolean
  dynamicTitle: boolean
  errorLog: string[]
  showFooter: boolean
  footerTxt: string
  caseNumber: string
}