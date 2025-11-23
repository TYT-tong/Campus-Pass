import { defineStore } from 'pinia'
import router, { constantRoutes } from '@/router'
import { getMenuList } from '@/api/menu'
import type { RouteRecordRaw } from 'vue-router'
import { isExternal } from '@/utils/validate'
import { defineAsyncComponent } from 'vue'

const modules = import.meta.glob('/src/views/**/*.vue')

interface PermissionState {
  routes: RouteRecordRaw[]
  addRoutes: RouteRecordRaw[]
  defaultRoutes: RouteRecordRaw[]
  topbarRouters: RouteRecordRaw[]
  sidebarRouters: RouteRecordRaw[]
  routesLoaded: boolean
}

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: [],
    routesLoaded: false,
  }),

  getters: {
    getRoutes: (state) => state.routes,
    getAddRoutes: (state) => state.addRoutes,
    getDefaultRoutes: (state) => state.defaultRoutes,
    getTopbarRouters: (state) => state.topbarRouters,
    getSidebarRouters: (state) => state.sidebarRouters,
  },

  actions: {
    /**
     * 生成路由
     */
    async generateRoutes() {
      try {
        const response = await getMenuList()

        const menuList = response.data
        const dynamicRoutes = this.buildDynamicRoutes(menuList)
        this.addRoutes = dynamicRoutes
        this.routes = [...constantRoutes, ...dynamicRoutes]
        this.setSidebarRouters([...constantRoutes, ...dynamicRoutes])
        this.setDefaultRoutes([...constantRoutes, ...dynamicRoutes])

        dynamicRoutes.forEach(r => router.addRoute(r))

        this.routesLoaded = true

        return dynamicRoutes
      } catch (error) {
        console.error('生成路由失败:', error)
        throw error
      }
    },

    /**
     * 构建动态路由
     */
    buildDynamicRoutes(menuList: any[]): RouteRecordRaw[] {
      const routes: RouteRecordRaw[] = []

      menuList.forEach(menu => {
        const route = this.buildRoute(menu)
        if (route) {
          routes.push(route)
        }
      })

      return routes
    },

    /**
     * 构建单个路由
     */
    buildRoute(menu: any): RouteRecordRaw | null {
      if (menu.menuType === 'F') {
        return null
      }

      const isExt = isExternal(menu.path || '') || !!(menu.meta && menu.meta.link)
      const routePath = isExt ? `/i/${encodeURIComponent(menu.path || (menu.meta?.link || ''))}` : (menu.path || '')

      const route: RouteRecordRaw = {
        path: routePath,
        name: menu.name || `Route${menu.menuId}`,
        component: this.loadComponent(menu.component),
        meta: {
          title: menu.meta?.title || menu.menuName,
          icon: menu.meta?.icon || menu.icon,
          noCache: !!(menu.meta?.noCache),
          link: menu.meta?.link || (isExt ? (menu.path || '') : null),
        },
        children: [],
        hidden: !!menu.hidden,
      } as RouteRecordRaw

      if (menu.redirect && menu.redirect !== 'noRedirect') {
        ; (route as any).redirect = menu.redirect
      }

      if (menu.children && menu.children.length > 0) {
        menu.children.forEach((child: any) => {
          const childRoute = this.buildRoute(child)
          if (childRoute) {
            ; (route as any).children!.push(childRoute)
          }
        })
      }

      return route
    },

    /**
     * 加载组件
     */
    loadComponent(componentPath: string) {
      if (componentPath === 'Layout') {
        return () => import('@/layout/LayoutIndex.vue')
      }
      if (componentPath === 'ParentView') {
        return () => import('@/components/ParentView/index.vue')
      }
      if (!componentPath) {
        return () => import('@/views/Index.vue')
      }
      const cleanPath = componentPath.startsWith('/') ? componentPath.slice(1) : componentPath
      const fullPath = `/src/views/${cleanPath}.vue`
      const loader = modules[fullPath] || (() => import('@/views/Index.vue'))
      return defineAsyncComponent({
        loader: loader as any,
        timeout: 15000,
        onError(error, retry, fail, attempts) {
          if (attempts <= 2) retry()
          else fail()
        },
      })
    },

    /**
     * 设置侧边栏路由
     */
    setSidebarRouters(routes: RouteRecordRaw[]) {
      this.sidebarRouters = routes
    },

    /**
     * 设置默认路由
     */
    setDefaultRoutes(routes: RouteRecordRaw[]) {
      this.defaultRoutes = routes
    },

    /**
     * 设置顶部路由
     */
    setTopbarRouters(routes: RouteRecordRaw[]) {
      this.topbarRouters = routes
    },

    /**
    * 清理路由
    */
    resetRoutes() {
      if (this.addRoutes && this.addRoutes.length > 0) {
        this.addRoutes.forEach((r) => {
          const name = r.name as string | undefined
          if (name && router.hasRoute(name)) {
            router.removeRoute(name)
          }
        })
      }
      this.routes = []
      this.addRoutes = []
      this.defaultRoutes = []
      this.topbarRouters = []
      this.sidebarRouters = []
      this.routesLoaded = false
    },
  },
}) as any
