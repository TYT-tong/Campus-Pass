import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw, Router, RouteLocationNormalized, NavigationGuardNext } from 'vue-router'
import Login from '@/views/Login.vue'
import Layout from '@/layout/LayoutIndex.vue'
import IndexView from '@/views/Index.vue'

import { usePermissionStore } from '@/stores/permission'
import { getToken } from '@/utils/auth'
import { ElMessageBox } from 'element-plus'

// 公共路由
export const constantRoutes: RouteRecordRaw[] = [

  {
    path: '/login',
    name: 'Login',
    component: Login,

  },

  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        name: 'Index',
        component: IndexView,
        meta: { title: '首页', icon: 'dashboard', affix: true },
      },
    ],
  },
]

const router: Router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
  scrollBehavior(_to: RouteLocationNormalized, _from: RouteLocationNormalized, savedPosition: any) {
    if (savedPosition) {
      return savedPosition
    }
    return { top: 0 }
  },
})

router.onError((err) => {
  try {
    const msg = String(err || '')
    if (msg.includes('Failed to fetch dynamically imported module') || msg.includes('Importing a module failed')) {
      router.replace('/index')
    }
  } catch {}
})

// 路由守卫
router.beforeEach(async (to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  // 如果是登录页面，直接通过
  if (to.path === '/login') {
    next()
    return
  }

  // 检查是否有Token
  const token = getToken()

  // 如果没有Token，弹出认证弹窗
  if (!token) {
    try {
      await ElMessageBox.confirm('登录状态已过期，是否重新登录？', '身份验证', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning',
      })
      // 用户确认重新登录，跳转到登录页
      next('/login')
    } catch {
      // 用户取消，也跳转到登录页
      next('/login')
    }
    return
  }

  // 获取权限store
  const permissionStore = usePermissionStore()

  // 如果还没有加载菜单数据
  if (!permissionStore.routesLoaded) {
    try {
      // 初始化权限和菜单数据
      await permissionStore.generateRoutes()
      next({ ...to, replace: true })
    } catch (error: any) {
      console.error('权限初始化失败:', error)
      // 检查是否是401错误（登录过期）
      if (error && (error.code === 401 || error.__SILENT_ERROR__)) {
        // 401错误不需要额外处理，request拦截器已经处理了
        next('/login')
      } else {
        // 其他错误也跳转到登录页
        next('/login')
      }
    }
  } else {
    next()
  }
})

export default router
