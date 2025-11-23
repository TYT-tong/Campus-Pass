<template>
  <!--
    根容器：通过动态 class 控制整体布局样式
    - classObj：根据 sidebar 状态生成的动态类（如侧边栏展开/隐藏、是否禁用动画）
    - app-wrapper：固定根类，用于统一控制布局基础样式
  -->
  <div :class="classObj" class="app-wrapper">
    <!-- Element Plus 布局容器：用于快速实现垂直/水平布局（el-container 嵌套实现复杂布局） -->
    <el-container>
      <!--
        侧边栏容器：
        - AppLayout：侧边栏组件（封装了侧边栏菜单、Logo 等）
        - v-if="!sidebar.hide"：控制侧边栏是否完全隐藏（sidebar.hide 为 true 时不渲染侧边栏）
        - class="sidebar-container"：侧边栏专属样式类
      -->
      <AppLayout v-if="!(sidebar as any).hide" class="sidebar-container" />

      <!--
        主内容区容器（包含顶部导航和主页面）：
        - :class="{ sidebarHide: (sidebar as any).hide }"：侧边栏完全隐藏时，添加特殊类调整主内容区宽度
        - class="main-container"：主内容区基础样式类
      -->
      <el-container :class="{ sidebarHide: (sidebar as any).hide }" class="main-container">
        <!--
          顶部导航栏容器（Element Plus 头部组件）：
          - :class="{ 'fixed-header': fixedHeader }"：根据 fixedHeader 状态控制顶部导航是否固定（滚动时不消失）
          - el-header：Element Plus 自带的头部布局类，默认垂直排列
        -->
        <el-header class="pre-header" :class="{ 'fixed-header': hasFixedHeader }">
          <!--
            顶部导航组件：
            - @setLayout：监听导航栏触发的“布局调整事件”（如切换侧边栏状态、修改固定头部配置）
          -->
          <Navbar @setLayout="setLayout" />
        </el-header>

        <!--
          主内容区（Element Plus 主体组件）：
          - el-main：Element Plus 自带的主体布局类，默认有内边距，用于包裹页面核心内容
          - class="wrapper"：主内容区自定义样式类（控制背景、内边距等）
        -->
        <el-main class="wrapper">
          <!-- AppMain：主内容渲染组件（通常包含路由出口 <router-view />，用于展示不同页面内容） -->
          <AppMain class="main-page" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// 1. 导入子组件：布局的核心组成部分
import AppLayout from './components/Sidebar/AppLayout.vue'; // 侧边栏组件（含菜单、Logo）
import Navbar from './components/LayoutNavbar.vue'; // 顶部导航栏组件（含用户信息、布局切换按钮）
import AppMain from './components/AppMain.vue'; // 主内容容器（含路由出口）

// 2. 导入 Vue 工具与状态管理：
import { computed } from 'vue' // Vue 计算属性（用于响应式处理状态，依赖变化时自动更新）
import { useAppStore } from '@/stores/app' // 导入 appStore（管理全局布局状态，如侧边栏、设备类型）

// 3. 响应式获取布局状态（通过计算属性关联 Store，确保状态变化时 UI 自动更新）
// - 侧边栏状态：获取 sidebar 对象（包含 opened/withoutAnimation/hide 等属性）
const sidebar = computed(() => useAppStore().sidebar)
// - 顶部导航是否固定：从 appStore 中获取 fixedHeader 状态（控制头部是否固定）
// const fixedHeader = computed(() => useAppStore().fixedHeader || false)
// 由于 fixedHeader 可能不存在，添加类型断言
const hasFixedHeader = computed(() => !!(useAppStore() as any).fixedHeader)

// 4. 动态生成根容器的 class（根据 sidebar 状态调整布局样式）
const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened, // 侧边栏收起时，添加“隐藏侧边栏”相关样式
  openSidebar: sidebar.value.opened, // 侧边栏展开时，添加“展开侧边栏”相关样式
  withoutAnimation: sidebar.value.withoutAnimation // 侧边栏切换时禁用动画，添加对应样式
}))

// 5. 处理顶部导航栏触发的“布局调整事件”（如切换侧边栏、修改固定头部）
// - 事件由 Navbar 组件触发，参数为布局调整的配置（如 { key: 'fixedHeader', value: true }）
// - 调用 appStore 的 changeSetting 方法（需确保 appStore 中有该方法）更新全局状态
const setLayout = (data: any) => {
  // 处理布局设置，暂时留空实现
  console.log('Layout setting:', data)
}
</script>


<style lang="scss" scoped>
/* 导入 SCSS 工具文件：mixin（混合器，复用样式逻辑）、variables（全局变量，如颜色、尺寸） */
@use "@/assets/styles/mixin.scss" as mix;
@use "@/assets/styles/variables.scss" as vars;

/* 根容器样式：控制整个布局的基础尺寸和定位 */
.app-wrapper {
  @include mix.clearfix; // 调用 mixin 中的清除浮动样式（避免子元素浮动导致父容器高度塌陷）
  position: relative; // 相对定位，用于子元素绝对定位的参考
  height: 100%; // 占满父容器高度（通常父容器为 body，实现全屏布局）
  width: 100%; // 占满父容器宽度

  /* 移动端适配：侧边栏展开时，根容器固定定位（避免滚动时布局错乱） */
  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.pre-header {
  box-shadow: none;
  border-bottom: 1px solid #EBEEF5;
  background-color: vars.$base-sidebar-background;
}

/* 主内容区（AppMain）样式：控制页面内容的背景和尺寸 */
.main-page {
  border-radius: 16px;

  background: #fff; // 白色背景（区分主内容与外围灰色背景）
  height: 100%; // 占满父容器（el-main）高度
}

/* 主内容容器（main-container）样式：顶部导航固定时的特殊处理 */
.main-container:has(.fixed-header) {


  height: 100vh; // 占满视口高度（避免内容溢出时出现双重滚动条）
  overflow: hidden; // 隐藏溢出内容（滚动由内部主内容区控制）

  ::v-deep .el-header {
    background-color: vars.$base-sidebar-background;
    width: 100%;
    z-index: 1; // 提升头部层级，确保在主内容上方显示
    border-bottom: 1px solid #EBEEF5;
  }

}

/* 主内容区外层（el-main）样式：控制内边距和背景 */
.wrapper {
  margin: 0; // 清除默认外边距
  padding: 20px; // 内边距（让页面内容与容器边缘有间距）
  background: #eef0f3; // 浅灰色背景（区分主内容区与顶部导航）
}

/* 侧边栏遮罩层样式（可能用于移动端侧边栏展开时的背景遮罩，原代码未在模板中使用，预留扩展） */
.drawer-bg {
  background: #000; // 黑色背景
  opacity: 0.3; // 30% 透明度（半透明遮罩）
  width: 100%; // 占满宽度
  top: 0; // 定位到顶部
  height: 100%; // 占满高度
  position: absolute; // 绝对定位（基于 app-wrapper 的相对定位）
  z-index: 999; // 层级较高（确保遮罩在其他元素之上，除了侧边栏）
}

/* 顶部导航固定样式：控制固定时的宽度和过渡效果 */
.fixed-header {
  position: fixed; // 固定定位（滚动时保持在顶部）
  top: 0; // 定位到顶部
  right: 0; // 定位到右侧
  z-index: 9; // 层级适中（高于主内容，低于遮罩和侧边栏）
  /* 宽度计算：100% 减去侧边栏宽度（vars.$base-sidebar-width 是侧边栏默认宽度变量） */
  width: calc(100% - #{vars.$base-sidebar-width});
  transition: width 0.28s; // 宽度变化时添加过渡动画（0.28秒，保持与 Element Plus 组件动画一致）
}

/* 侧边栏收起时：调整固定顶部导航的宽度（减去收起后的侧边栏宽度 54px） */
.hideSidebar .fixed-header {
  width: calc(100% - 30px);
}

/* 侧边栏完全隐藏时：固定顶部导航占满宽度（100%） */
.sidebarHide .fixed-header {
  width: 100%;
}

/* 移动端适配：固定顶部导航占满宽度（100%） */
.mobile .fixed-header {
  width: 100%;
}
</style>
