<template>
  <!-- 当菜单项不隐藏时才渲染 -->
  <div v-if="!item.hidden">
    <!-- 单一菜单项渲染：当只有一个可见子菜单时，直接渲染该子菜单（不显示父菜单） -->
    <template v-if="hasOneShowingChild(item.children, item) &&
      (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&
      !item.alwaysShow">
      <!-- 应用链接组件，处理内部路由和外部链接 -->
      <AppLink v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <!-- 菜单项组件，index为路由路径 -->
        <el-menu-item class="menu-item-color" :index="resolvePathForIndex(onlyOneChild.path)"
          :class="{ 'submenu-title-noDropdown': !isNest }">
          <!-- 菜单图标 -->
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)" />
          <!-- 菜单项标题 -->
          <template #title>
            <span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">
              {{ onlyOneChild.meta.title }}
            </span>
          </template>
        </el-menu-item>
      </AppLink>
    </template>

    <!-- 子菜单组件：当有多个子菜单或需要显示父菜单时使用 -->
    <el-sub-menu v-else ref="subMenu" :index="resolvePathForIndex(item.path)" teleported>
      <!-- 子菜单标题部分 -->
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta && item.meta.icon" />
        <span class="menu-title" :title="hasTitle(item.meta.title)">{{ item.meta.title }}</span>
      </template>

      <!-- 递归渲染子菜单 -->
      <SidebarItem v-for="(child, index) in item.children" :key="child.path + index" :is-nest="true" :item="child"
        :base-path="resolvePathForIndex(item.path)" style="getMenuBackground" />
    </el-sub-menu>
  </div>
</template>

<script setup lang="ts">
import AppLink from './AppLink.vue'  // 引入链接处理组件
import { ref } from 'vue'        // 引入Vue的ref响应式函数
import { isExternal } from '@/utils/validate'  // 引入外部链接判断工具
import { getNormalPath } from '@/utils/ruoyi'  // 引入路径规范化工具
import SidebarItem from './SidebarItem.vue';   // 引入自身组件用于递归渲染

// 定义组件属性
const props = defineProps({
  // 路由对象，包含菜单的配置信息
  item: {
    type: Object,
    required: true
  },
  // 是否为嵌套菜单
  isNest: {
    type: Boolean,
    default: false
  },
  // 基础路径，用于拼接完整路由
  basePath: {
    type: String,
    default: ''
  }
})

// 存储唯一可见子菜单的响应式变量
const onlyOneChild = ref<any>({})

/**
 * 判断是否只有一个可见子菜单
 * @param {Array} children - 子菜单列表
 * @param {Object} parent - 父菜单对象
 * @returns {Boolean} 是否只有一个可见子菜单
 */
function hasOneShowingChild(children = [], parent: any) {
  if (!children) {
    children = []
  }
  // 筛选出可见的子菜单（排除hidden为true的项）
  const showingChildren = children.filter((item: any) => {
    if (item.hidden) {
      return false
    }
    // 记录当前可见子菜单
    onlyOneChild.value = item
    return true
  })

  // 当只有一个可见子菜单时，默认显示该子菜单
  if (showingChildren.length === 1) {
    return true
  }

  // 当没有可见子菜单时，显示父菜单
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: parent.path, noShowingChildren: true }
    return true
  }

  // 多个可见子菜单时返回false
  return false
}

/**
 * 解析路由路径，处理外部链接和路径拼接
 * @param {String} routePath - 路由路径
 * @param {Object} routeQuery - 路由参数
 * @returns {String|Object} 解析后的路径或包含路径和参数的对象
 */
function resolvePath(routePath: string, routeQuery?: string) {

  // 外部链接直接返回
  if (isExternal(routePath)) {
    return routePath
  }
  // 基础路径是外部链接时直接返回
  if (isExternal(props.basePath)) {
    return props.basePath
  }

  // 如果路由路径已经是绝对路径（以/开头），直接使用
  if (routePath && routePath.startsWith('/')) {
    if (routeQuery) {
      let query = JSON.parse(routeQuery)
      return {
        path: getNormalPath(routePath),
        query: query
      }
    }
    return getNormalPath(routePath)
  }

  // 处理带参数的路由
  if (routeQuery) {
    let query = JSON.parse(routeQuery)
    return {
      path: getNormalPath(props.basePath + '/' + routePath),
      query: query
    }
  }
  // 拼接并规范化路径
  return getNormalPath(props.basePath + '/' + routePath)
}

// 重载函数签名，确保返回字符串
function resolvePathForIndex(routePath: string, routeQuery?: string): string {
  const result = resolvePath(routePath, routeQuery)
  return typeof result === 'string' ? result : result.path
}

/**
 * 处理标题显示，超过5个字符时显示完整标题提示
 * @param {String} title - 菜单标题
 * @returns {String} 标题提示文本
 */
function hasTitle(title: string) {
  if (!title || typeof title !== 'string') {
    return ""  // 如果标题不存在或不是字符串，不显示提示
  }
  if (title.length > 5) {
    return title  // 超过5个字符时显示完整标题作为提示
  } else {
    return ""     // 否则不显示提示
  }
}
</script>

<style scoped></style>
