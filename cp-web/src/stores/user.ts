import { defineStore } from 'pinia'
import { ElMessageBox } from 'element-plus'
import router from '@/router'
import { login as loginApi, logout as logoutApi, getUserInfo, refreshToken, phoneLogin } from '@/api/auth'
import { getToken, setToken, setExpiresIn, removeToken } from '@/utils/auth'
import { isEmpty } from '@/utils/validate'
import defAva from '@/assets/images/profile.jpg'
import type { UserInfo, LoginResponse } from '@/types/api'
import type { UserState } from '@/types/store'
import { usePermissionStore } from '@/stores/permission'
import { useTagsViewStore } from '@/stores/tagsView'

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken(),
    id: '',
    name: '',
    nickName: '',
    avatar: '',
    roles: [],
    permissions: [],
    expires_in: '',
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    hasRole: (state) => (role: string) => state.roles.includes(role),
    hasPermission: (state) => (permission: string) => state.permissions.includes(permission),
  },

  actions: {
    /**
     * 用户登录
     */
    async login(loginForm: { username?: string; password?: string; phone?: string; code?: string; rememberMe?: boolean; uuid?: string }) {
      try {
        const response = await loginApi(loginForm)

        const data: LoginResponse = response.data as LoginResponse


        if (response.code === 200 && data.access_token && data.expires_in) {
          setToken(data.access_token)
          this.token = data.access_token
          setExpiresIn(data.expires_in.toString())
          this.expires_in = data.expires_in.toString()
          return Promise.resolve()
        } else {
          return Promise.reject(new Error(response.msg || '登录失败'))
        }
      } catch (error) {
        console.error('Login error:', error)
        return Promise.reject(error)
      }
    },

    async phoneLogin(phoneForm: { phone: string; code: string; }) {
      try {
        console.log("手机号登录store", JSON.stringify(phoneForm));

        const response = await phoneLogin(phoneForm)

        const data: LoginResponse = response.data as LoginResponse


        if (response.code === 200 && data.access_token && data.expires_in) {
          setToken(data.access_token)
          this.token = data.access_token
          setExpiresIn(data.expires_in.toString())
          this.expires_in = data.expires_in.toString()
          return Promise.resolve()
        } else {
          return Promise.reject(new Error(response.msg || '登录失败'))
        }
      } catch (error) {
        console.error('Login error:', error)
        return Promise.reject(error)
      }
    },

    /**
     * 获取用户信息
     */
    async getUserInfo() {
      try {
        const response = await getUserInfo()
        const userData = response.data
        const user = userData.user
        const avatar = isEmpty(user.avatar) ? defAva : user.avatar

        if (userData.roles && userData.roles.length > 0) {
          this.roles = userData.roles
          this.permissions = userData.permissions
        } else {
          this.roles = ['ROLE_DEFAULT']
        }

        this.id = user.userId
        this.name = user.userName
        this.nickName = user.nickName
        this.avatar = avatar

        // 初始密码提示
        if (userData.isDefaultModifyPwd) {
          await ElMessageBox.confirm('您的密码还是初始密码，请修改密码！', '安全提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          })
          router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
        }

        // 过期密码提示
        if (!userData.isDefaultModifyPwd && userData.isPasswordExpired) {
          await ElMessageBox.confirm('您的密码已过期，请尽快修改密码！', '安全提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          })
          router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
        }

        return Promise.resolve(response)
      } catch (error) {
        console.error('Get user info error:', error)
        return Promise.reject(error)
      }
    },

    /**
     * 刷新Token
     */
    async refreshToken() {
      try {
        const response = await refreshToken()
        if (response.data && response.data.expires_in) {
          setExpiresIn(response.data.expires_in)
          this.expires_in = response.data.expires_in
        }
        return Promise.resolve()
      } catch (error) {
        console.error('Refresh token error:', error)
        return Promise.reject(error)
      }
    },

    /**
     * 用户退出
     */
    async logout() {
      try {
        await logoutApi()
        this.clearUserInfo()
        const permissionStore = usePermissionStore()
        const tagsViewStore = useTagsViewStore()
        permissionStore.resetRoutes()
        tagsViewStore.$reset()
        return Promise.resolve()
      } catch (error) {
        console.error('Logout error:', error)
        this.clearUserInfo()
        const permissionStore = usePermissionStore()
        const tagsViewStore = useTagsViewStore()
        permissionStore.resetRoutes()
        tagsViewStore.$reset()
        return Promise.reject(error)
      }
    },

    /**
     * 前端登出
     */
    fedLogOut() {
      this.clearUserInfo()
      const permissionStore = usePermissionStore()
      const tagsViewStore = useTagsViewStore()
      permissionStore.resetRoutes()
      tagsViewStore.$reset()
      return Promise.resolve()
    },

    /**
     * 清除用户信息
     */
    clearUserInfo() {
      this.token = ''
      this.roles = []
      this.permissions = []
      this.id = ''
      this.name = ''
      this.nickName = ''
      this.avatar = ''
      this.expires_in = ''
      removeToken()
    },

    /**
     * 更新用户信息
     */
    updateUserInfo(userInfo: Partial<UserInfo>) {
      if (userInfo.id) this.id = userInfo.id.toString()
      if (userInfo.username) this.name = userInfo.username
      if (userInfo.nickname) this.nickName = userInfo.nickname
      if (userInfo.avatar) this.avatar = userInfo.avatar
    },
  },

  persist: {
    key: 'user-store',
    storage: localStorage,
    paths: ['token', 'expires_in', 'roles', 'permissions'],
  } as any,
})
