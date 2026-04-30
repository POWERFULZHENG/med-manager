<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="layout-aside">
      <div class="logo">
        <el-icon size="24"><FirstAidKit /></el-icon>
        <h2>药箱管理</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="menu"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409EFF"
        @select="handleMenuSelect"
      >
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-badge :value="5" class="header-badge">
            <el-icon class="header-icon"><Bell /></el-icon>
          </el-badge>
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" style="background-color: #409EFF">
                {{ userStore.userInfo?.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <span class="username">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { HomeFilled, Box, List, Warning, User, FirstAidKit, Bell, ArrowDown, SwitchButton } from '@element-plus/icons-vue'
import { logout } from '../api/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const loading = ref(false)

const menuItems = [
  { index: '/dashboard', title: '首页', icon: HomeFilled },
  { index: '/medicine', title: '药品管理', icon: Box },
  { index: '/inventory', title: '库存管理', icon: List },
  { index: '/expire', title: '过期提醒', icon: Warning },
  { index: '/user', title: '个人中心', icon: User }
]

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title as string || '首页')

const handleMenuSelect = (index: string) => {
  router.push(index)
}

const handleCommand = async (command: string) => {
  if (command === 'profile') {
    router.push('/user')
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      loading.value = true
      await logout()
      
      userStore.logout()
      ElMessage.success('退出登录成功')
      router.push('/login')
    } catch {
    } finally {
      loading.value = false
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #001529;
  overflow: hidden;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo h2 {
  margin: 0;
  font-size: 18px;
  color: #fff;
}

.menu {
  border-right: none;
}

.menu :deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

.layout-header {
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.header-badge {
  cursor: pointer;
}

.header-icon {
  font-size: 20px;
  color: #666;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  color: #333;
}

.layout-main {
  background: #f0f2f5;
  padding: 24px;
  overflow: auto;
}
</style>
