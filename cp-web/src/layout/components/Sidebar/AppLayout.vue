<template>
  <div class="sidebar-container">
    <SidebarLogo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu :default-active="activeMenu" :collapse="isCollapse" :background-color="getMenuBackground"
        :text-color="getMenuTextColor" :unique-opened="true" :collapse-transition="false" mode="vertical">
        <SidebarItem v-for="(route, index) in sidebarRouters" :key="route.path + index" :item="route"
          :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>
<script setup lang="ts">
import SidebarItem from './SidebarItem.vue';
import SidebarLogo from './SidebarLogo.vue';
import { usePermissionStore } from '@/stores/permission'
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import useSettingsStore from '@/stores/settings'
import { useAppStore } from '@/stores/app'
import vars from '@/assets/styles/variables.module.scss';


const settingsStore = useSettingsStore()
const appStore = useAppStore()
// 添加缺失的计算属性
const isCollapse = computed(() => !appStore.sidebar.opened)// 或连接到全局状态
const getMenuBackground = computed(() => (vars as any).menuBackground)
const getMenuTextColor = computed(() => (vars as any).menuText)
const permissionStore = usePermissionStore()
const sidebarRouters = computed(() => permissionStore.sidebarRouters)
const showLogo = computed(() => settingsStore.sidebarLogo)

const route = useRoute()

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta && (meta as any).activeMenu) {
    return (meta as any).activeMenu
  }
  return path
})

</script>



<style lang="scss" scoped>
@use "@/assets/styles/variables.scss" as vars;

.sidebar-container {
  background-color: vars.$base-sidebar-background;
  --menu-hover: #F2F3F5;
  --menu-active-bg: #E6F1FF;
  --menu-active-text: #409EFF;

  .scrollbar-wrapper {
    background-color: vars.$base-sidebar-background;
  }

  .el-menu {
    border: none;
    height: 100%;
    margin-left: 3px;
    width: 100% !important;

    .el-menu-item,
    .el-sub-menu__title {

      &:hover {
        background-color: var(--menu-hover, #F2F3F5) !important;
      }
    }

    .el-menu-item {
      color: vars.$base-menu-color;

      &.is-active {
        color: var(--menu-active-text, #409EFF);
        background-color: var(--menu-active-bg, #E6F1FF) !important;
        border-left: 2px solid var(--menu-active-text, #409EFF);
      }
    }

    .el-sub-menu__title {
      color: vars.$base-menu-color;
      &.is-active {
        color: var(--menu-active-text, #409EFF);
        background-color: var(--menu-active-bg, #E6F1FF) !important;
        border-left: 2px solid var(--menu-active-text, #409EFF);
      }
    }
  }
}
</style>
