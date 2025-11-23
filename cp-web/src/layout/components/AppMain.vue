<template>
  <!-- 主内容区域容器 -->
  <section class="app-main">
    <!-- 路由视图，用于渲染匹配当前路由的组件 -->
    <router-view v-slot="{ Component, route }">
      <!-- 页面切换过渡动画：淡入淡出+变换效果，先出后进模式 -->
      <transition name="fade-transform" mode="out-in">
        <!-- 页面缓存组件：只缓存tagsViewStore中记录的页面 -->
        <!-- 渲染路由组件：非外部链接页面才通过component渲染 -->
        <!-- 动态渲染路由匹配的组件 -->
        <!-- 用路由路径作为key，确保路由变化时重新渲染 -->
        <!-- 不是外部链接时才渲染组件 -->
        <keep-alive :include="tagsViewStore.cachedViews">
          <component v-if="!route.meta.link" :is="Component" :key="route.path" />
        </keep-alive>
      </transition>
    </router-view>

    <!-- iframe页面切换组件：用于管理外部链接的iframe页面 -->
    <iframe-toggle />

    <!-- 版权信息组件 -->
    <copyright />
  </section>
</template>

<script setup lang="ts">
// 引入版权信息组件
import copyright from "./Copyright/index.vue"
// 引入iframe页面切换组件
import iframeToggle from "./IframeToggle/index.vue"
// 引入tagsView的状态管理模块
import { useTagsViewStore } from '@/stores/tagsView'
import { useRoute } from "vue-router"
import { watchEffect, onMounted } from 'vue'
// 获取当前路由信息
const route = useRoute()
// 获取tagsView的状态实例
const tagsViewStore = useTagsViewStore()

// 组件挂载时执行
onMounted(() => {
  addIframe()
})

// 监听路由变化，当路由信息改变时执行
watchEffect(() => {
  addIframe()
})

/**
 * 处理外部链接页面：如果当前路由是外部链接，添加到iframe视图管理中
 */
function addIframe() {
  // 判断路由元信息中是否包含link（外部链接）
  if (route.meta.link) {
    // 调用tagsViewStore的方法添加iframe视图
    useTagsViewStore().addIframeView(route)
  }
}
</script>

<!-- 组件样式 -->
<style lang="scss" scoped>
.app-main {
  /* 计算高度：视口高度减去导航栏高度(50px) */
  min-height: calc(100vh - 100px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

/* 当有固定头部时的样式调整 */
.fixed-header+.app-main {
  overflow-y: auto;
  /* 允许垂直滚动 */
  scrollbar-gutter: auto;
  /* 滚动条预留空间，避免内容跳动 */
  height: calc(100vh - 50px);
  min-height: 0px;
}

/* 包含版权信息时添加底部内边距，避免版权信息被遮挡 */
.app-main:has(.copyright) {
  padding-bottom: 36px;
}

/* 固定头部与主内容区域的间距调整 */
.fixed-header+.app-main {
  margin-top: 50px;
}

/* 当存在标签视图(tagsView)时的样式调整 */
.hasTagsView {
  .app-main {
    /* 计算高度：视口高度减去导航栏(50px)和标签视图(34px)高度 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header+.app-main {
    margin-top: 84px;
    /* 导航栏+标签视图的总高度 */
    height: calc(100vh - 84px);
    min-height: 0px;
  }
}
</style>

<!-- 全局滚动条样式 -->
<style lang="scss">
/* 自定义滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  /* 垂直滚动条宽度 */
  height: 6px;
  /* 水平滚动条高度 */
}

/* 滚动条轨道样式 */
::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

/* 滚动条滑块样式 */
::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
  /* 滑块圆角 */
}
</style>
