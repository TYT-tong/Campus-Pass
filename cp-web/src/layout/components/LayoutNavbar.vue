<template>
  <div class="navbar">
    <Hamburger id="hamburger-container" @toggleClick="toggleSideBar" :is-active="appStore.sidebar.opened"
      class="hamburger-container" />
    <Breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    <TopNav v-if="settingsStore.topNav" id="topmenu-container" class="topmenu-container" />

    <div class="right-menu">
      <template v-if="true">
        <header-search id="header-search" class="right-menu-item" />


      </template>
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="logo" class="user-avatar" />
          <span class="user-nickname"> TYT </span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item>
              <span>布局设置</span>
            </el-dropdown-item>
            <el-dropdown-item :loading="loading">
              <span @click="logout">退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts" name="Navbar">
import logo from '@/assets/logo/logo.png'
import TopNav from '@/components/TopNav/index.vue'
import Hamburger from '@/components/Hamburger/index.vue'
import Breadcrumb from '@/components/Breadcrumb/Index.vue'
import useSettingsStore from '@/stores/settings'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
const userStore = useUserStore()
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const loading = ref(false)
const toggleSideBar = async () => {
  appStore.toggleSidebar()
}
// const route = useRoute() // 暂时不需要使用
const router = useRouter()
const logout = async () => {
  loading.value = true

  try {
    await userStore.logout();
    await router.push('/login');
    ElMessage.success('退出成功');

  } catch (error) {
    ElMessage.error((error as Error).message || '退出失败')
  } finally {
    loading.value = false
  }
}

</script>


<style lang='scss' scoped>
@use "@/assets/styles/variables.scss" as vars;

.navbar {
  background-color: vars.$base-sidebar-background;
  width: 100%;
  height: 60px;
  position: relative;
  right: 20px;



  .hamburger-container {
    line-height: 47.5px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 60px;
    display: flex;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }

      &.theme-switch-wrapper {
        display: flex;
        align-items: center;

        svg {
          transition: transform 0.3s;

          &:hover {
            transform: scale(1.15);
          }
        }
      }
    }

    .avatar-container {
      margin-right: 0px;
      padding-right: 0px;

      .avatar-wrapper {
        margin-top: 10px;
        right: 8px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          margin-right: 8px;
          border-radius: 50%;
        }

        .user-nickname {
          position: relative;
          left: 0px;
          bottom: 10px;
          font-size: 14px;
          font-weight: bold;
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
