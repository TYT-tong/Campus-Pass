<template>
  <!--
    Element UI的菜单组件，用于实现顶部导航菜单
    :default-active="activeMenu"：设置默认激活的菜单项
    mode="horizontal"：水平方向展示（顶部菜单模式）
    @select="handleSelect"：菜单项选中时的回调
    :ellipsis="false"：不自动省略菜单文本（通过自定义逻辑处理溢出）
  -->
  <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :ellipsis="false">
    <!-- 遍历顶部菜单，渲染可见的菜单项 -->
    <template v-for="(item, index) in topMenus">
      <!--
        只渲染索引小于可见数量的菜单项
        :style="{'--theme': theme}"：动态绑定主题颜色变量
        :index="item.path"：菜单项唯一标识（使用路由路径）
      -->
      <el-menu-item :style="{ '--theme': theme }" :index="item.path" :key="index" v-if="visibleNumber !== null && index < visibleNumber">
        <!-- 显示菜单图标（如果有且不为#） -->
        <svg-icon v-if="item.meta && item.meta.icon && item.meta.icon !== '#'" :icon-class="item.meta.icon" />
        <!-- 显示菜单标题 -->
        {{ item.meta.title }}
      </el-menu-item>
    </template>

    <!-- 顶部菜单超出可见数量时，显示"更多菜单"下拉框 -->
    <el-sub-menu :style="{ '--theme': theme }" index="more" v-if="visibleNumber !== null && topMenus.length > visibleNumber">
      <template #title>更多菜单</template>
      <!-- 渲染超出可见数量的菜单项 -->
      <template v-for="(item, index) in topMenus">
        <el-menu-item :index="item.path" :key="index" v-if="visibleNumber !== null && index >= visibleNumber">
          <!-- 显示菜单图标 -->
          <svg-icon v-if="item.meta && item.meta.icon && item.meta.icon !== '#'" :icon-class="item.meta.icon" />
          {{ item.meta.title }}
        </el-menu-item>
      </template>
    </el-sub-menu>
  </el-menu>
</template>

<script setup lang="ts">
import { constantRoutes } from "@/router"  // 导入常量路由配置
import { isHttp } from '@/utils/validate'  // 导入HTTP链接判断工具
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
// 导入状态管理模块
import { useAppStore } from '@/stores/app'
import useSettingsStore from '@/stores/settings'
import { usePermissionStore } from '@/stores/permission'

// 响应式变量：顶部可见菜单数量
const visibleNumber = ref<number | null>(null)
// 响应式变量：当前选中菜单的索引
const currentIndex = ref<string | null>(null)
// 需要隐藏侧边栏的路由列表
const hideList = ['/index', '/user/profile']

// 获取状态管理实例
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()
// 获取路由实例
const route = useRoute()
const router = useRouter()

// 计算属性：当前主题颜色（从设置中获取）
const theme = computed(() => settingsStore.theme)
// 计算属性：所有有权限的顶部路由（从权限存储中获取）
const routers = computed(() => permissionStore.topbarRouters)

// 计算属性：处理后的顶部显示菜单列表
const topMenus = computed(() => {
  let topMenus: any[] = []
  routers.value.map((menu: any) => {
    // 过滤隐藏的菜单
    if (menu.hidden !== true) {
      // 兼容处理：如果是根路径且有子路由，取第一个子路由作为顶部菜单
      if (menu.path === '/' && menu.children) {
        topMenus.push(menu.children[0])
      } else {
        topMenus.push(menu)
      }
    }
  })
  return topMenus
})

// 计算属性：处理后的子路由列表（用于菜单联动）
const childrenMenus = computed(() => {
  let childrenMenus: any[] = []
  routers.value.map((router: any) => {
    // 遍历子路由，处理路径格式
    for (let item in router.children) {
      // 如果没有父路径，自动补全路径
      if (router.children[item].parentPath === undefined) {
        if (router.path === "/") {
          router.children[item].path = "/" + router.children[item].path
        } else {
          // 非HTTP链接才拼接父路径
          if (!isHttp(router.children[item].path)) {
            router.children[item].path = router.path + "/" + router.children[item].path
          }
        }
        // 记录父路径，用于后续匹配
        router.children[item].parentPath = router.path
      }
      childrenMenus.push(router.children[item])
    }
  })
  // 合并常量路由和处理后的子路由
  return constantRoutes.concat(childrenMenus)
})

// 计算属性：默认激活的菜单路径
const activeMenu = computed(() => {
  const path = route.path
  let activePath = path
  // 处理非隐藏列表中的多级路径
  if (path !== undefined && path.lastIndexOf("/") > 0 && hideList.indexOf(path) === -1) {
    const tmpPath = path.substring(1, path.length)
    if (!route.meta.link) {
      // 取一级路径作为激活项
      activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"))
      // 显示侧边栏
      appStore.toggleSidebar(false)
    }
  } else if (!(route as any).children) {
    // 没有子路由时，激活当前路径
    activePath = path
    // 隐藏侧边栏
    appStore.toggleSidebar(true)
  }
  // 设置激活的路由
  activeRoutes(activePath)
  return activePath
})

/**
 * 根据屏幕宽度计算顶部可见菜单数量
 * 逻辑：屏幕宽度的1/3 除以 每个菜单约85px宽度，取整数
 */
function setVisibleNumber() {
  const width = document.body.getBoundingClientRect().width / 3
  visibleNumber.value = parseInt(String(width / 85))
}

/**
 * 菜单选中事件处理
 * @param {string} key - 选中菜单的index（路由路径）
 * @param {array} keyPath - 选中菜单的路径数组
 */
function handleSelect(key: string, _keyPath: string[]) {
  currentIndex.value = key
  // 查找选中的路由配置
  const route = routers.value.find((item: any) => item.path === key)

  if (isHttp(key)) {
    // 外部链接：新窗口打开
    window.open(key, "_blank")
  } else if (!route || !route.children) {
    // 没有子路由：内部跳转
    const routeMenu = childrenMenus.value.find(item => item.path === key)
    if (routeMenu && (routeMenu as any).query) {
      // 带查询参数的跳转
      let query = JSON.parse((routeMenu as any).query)
      router.push({ path: key, query: query })
    } else {
      // 普通跳转
      router.push({ path: key })
    }
    // 隐藏侧边栏
    appStore.toggleSidebar(true)
  } else {
    // 有子路由：显示侧边栏并联动
    activeRoutes(key)
    appStore.toggleSidebar(false)
  }
}

/**
 * 设置激活的路由，用于联动侧边栏
 * @param {string} key - 选中的路由路径
 * @returns {array} 匹配的子路由列表
 */
function activeRoutes(key: string) {
  let routes: any[] = []
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    // 筛选出父路径匹配的子路由
    childrenMenus.value.map((item: any) => {
      if (key == item.parentPath || (key == "index" && "" == item.path)) {
        routes.push(item)
      }
    })
  }
  // 设置侧边栏路由
  if (routes.length > 0) {
    permissionStore.setSidebarRouters(routes)
  } else {
    // 没有子路由时隐藏侧边栏
    appStore.toggleSidebar(true)
  }
  return routes
}

// 组件挂载时注册窗口大小变化事件
onMounted(() => {
  window.addEventListener('resize', setVisibleNumber)
})

// 组件卸载前移除窗口大小变化事件
onBeforeUnmount(() => {
  window.removeEventListener('resize', setVisibleNumber)
})

// 组件挂载时初始化可见菜单数量
onMounted(() => {
  setVisibleNumber()
})
</script>

<style lang="scss">
// 顶部菜单项基础样式
.topmenu-container.el-menu--horizontal>.el-menu-item {
  float: left;
  height: 50px !important; // 固定高度
  line-height: 50px !important; // 垂直居中
  color: #999093 !important; // 默认文字颜色
  padding: 0 5px !important; // 内边距
  margin: 0 10px !important; // 外边距
}

// 激活状态菜单项样式
.topmenu-container.el-menu--horizontal>.el-menu-item.is-active,
.el-menu--horizontal>.el-sub-menu.is-active .el-submenu__title {
  border-bottom: 2px solid #{'var(--theme)'} !important; // 底部主题色边框
  color: #303133; // 激活状态文字颜色
}

// 子菜单标题样式
.topmenu-container.el-menu--horizontal>.el-sub-menu .el-submenu__title {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #999093 !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
}

// 取消hover和focus时的背景色
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):focus,
.topmenu-container.el-menu--horizontal>.el-menu-item:not(.is-disabled):hover,
.topmenu-container.el-menu--horizontal>.el-submenu .el-submenu__title:hover {
  background-color: #ffffff;
}

// 图标右间距
.topmenu-container .svg-icon {
  margin-right: 4px;
}

// 调整"更多菜单"下拉箭头位置
.topmenu-container .el-sub-menu .el-submenu__icon-arrow {
  position: static;
  vertical-align: middle;
  margin-left: 8px;
  margin-top: 0px;
}
</style>
