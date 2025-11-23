<template>
  <!-- 面包    面包屑导航组件，使用Element UI的el-breadcrumb
    class="app-breadcrumb"：自定义样式类
    separator="/"：设置分隔符为"/"
  -->
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <!--
      过渡动画组，用于面包屑项的切换动画
      name="breadcrumb"：指定过渡动画的名称，对应CSS中的过渡类
    -->
    <transition-group name="breadcrumb">
      <!--
        遍历面包屑层级列表，生成每个面包屑项
        :key="item.path"：使用路径作为唯一标识
      -->
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <!--
          如果是最后一项或设置了noRedirect，则显示为不可点击的文本
          no-redirect类用于设置不可点击样式
        -->
        <span v-if="item.redirect === 'noRedirect' || index == levelList.length - 1" class="no-redirect">{{
          item.meta.title }}</span>
        <!-- 其他情况显示为可点击的链接，点击时触发handleLink方法 -->
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup lang="ts">
// 导入权限存储模块，用于获取路由信息
import { usePermissionStore } from '@/stores/permission'
// 导入路由相关的 composable
import { useRoute, useRouter } from 'vue-router'
// 导入ref和watchEffect用于响应式处理
import { ref, watchEffect } from 'vue'

// 获取当前路由实例
const route = useRoute()
// 获取路由导航实例
const router = useRouter()
// 获取权限存储实例
const permissionStore = usePermissionStore()
// 响应式变量，存储面包屑层级列表
const levelList = ref<any[]>([])

/**
 * 获取并设置面包屑导航数据
 */
function getBreadcrumb() {
  // 存储匹配到的路由
  let matched: any[] = []
  // 计算当前路径中"/"的数量，用于判断路由层级
  const pathNum = findPathNum(route.path)

  // 处理多级菜单（路径层级大于2）
  if (pathNum > 2) {
    // 正则匹配路径中的每一级
    const reg = /\/\w+/gi
    // 解析路径为层级列表
    const pathList = route.path.match(reg)!.map((item, index) => {
      // 除了第一项外，移除开头的"/"
      if (index !== 0) item = item.slice(1)
      return item
    })
    // 递归查找匹配的路由记录
    getMatched(pathList, permissionStore.defaultRoutes, matched)
  } else {
    // 层级较少时，直接从当前路由匹配中筛选（只保留有标题的路由）
    matched = route.matched.filter((item) => item.meta && item.meta.title)
  }

  // 如果第一个匹配项不是首页，则在前面添加首页
  if (!isDashboard(matched[0])) {
    matched = [{ path: "/index", meta: { title: "首页" } }].concat(matched)
  }

  // 过滤掉不需要显示在面包屑中的路由（没有标题或breadcrumb为false）
  levelList.value = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
}

/**
 * 计算字符串中指定字符的出现次数
 * @param {string} str - 要检查的字符串
 * @param {string} char - 要查找的字符，默认为"/"
 * @returns {number} 字符出现的次数
 */
function findPathNum(str: string, char = "/") {
  let index = str.indexOf(char)
  let num = 0
  // 循环查找所有出现的位置
  while (index !== -1) {
    num++
    index = str.indexOf(char, index + 1)
  }
  return num
}

/**
 * 递归查找与路径列表匹配的路由记录
 * @param {array} pathList - 路径层级列表
 * @param {array} routeList - 路由配置列表
 * @param {array} matched - 存储匹配到的路由
 */
function getMatched(pathList: any[], routeList: any[], matched: any[]) {
  // 查找与当前路径项匹配的路由（路径或名称匹配）
  let data = routeList.find((item: any) =>
    item.path == pathList[0] ||
    (item.name && item.name.toLowerCase() == pathList[0])
  )

  if (data) {
    // 找到匹配项，添加到匹配列表
    matched.push(data)
    // 如果有子路由且还有路径项需要匹配，则继续递归
    if (data.children && pathList.length) {
      pathList.shift() // 移除已匹配的路径项
      getMatched(pathList, data.children, matched)
    }
  }
}

/**
 * 判断路由是否为首页
 * @param {object} route - 路由对象
 * @returns {boolean} 是否为首页
 */
function isDashboard(route: any) {
  const name = route && route.name
  if (!name) {
    return false
  }
  // 首页路由名称为'Index'
  return name.trim() === 'Index'
}

/**
 * 处理面包屑项的点击事件
 * @param {object} item - 面包屑项对应的路由对象
 */
function handleLink(item: any) {
  const { redirect, path } = item
  // 如果有重定向地址，则跳转到重定向地址
  if (redirect) {
    router.push(redirect)
    return
  }
  // 否则跳转到路由路径
  router.push(path)
}

/**
 * 监听路由变化，更新面包屑
 * 当路由路径以'/redirect/'开头时不更新（重定向页面）
 */
watchEffect(() => {
  if (route.path.startsWith('/redirect/')) {
    return
  }
  getBreadcrumb()
})

// 不需要手动初始化，watchEffect会在组件挂载时自动执行一次
</script>

<style lang='scss' scoped>
// 面包屑容器样式
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  // 不可点击的面包屑项样式
  .no-redirect {
    color: #409EFF;
    font-weight: 600;
    cursor: text;
  }
}
</style>
