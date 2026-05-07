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

  <el-dialog 
    v-model="logoutDialogVisible" 
    title="确定退出登录吗？" 
    align-center
    :show-close="false"
    :close-on-click-modal="false"
    width="360px"
  >
    <div class="logout-dialog-content">
      <div class="logout-icon">
        <el-icon size="48" color="#E6A23C"><Warning /></el-icon>
      </div>
      <p>退出后将需要重新登录</p>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="logoutDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleLogout">确定退出</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import { HomeFilled, Box, List, Warning, User, FirstAidKit, Bell, ArrowDown, SwitchButton } from '@element-plus/icons-vue'
import { logout } from '../api/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const loading = ref(false)
const logoutDialogVisible = ref(false)

const menuItems = computed(() => {
  const items = [
    { index: '/dashboard', title: '首页', icon: HomeFilled },
    { index: '/medicine', title: '药品管理', icon: Box },
    { index: '/inventory', title: '库存管理', icon: List },
    { index: '/expire', title: '过期提醒', icon: Warning },
    { index: '/user/center', title: '个人中心', icon: User }
  ]
  if (userStore.userInfo?.roleCode === 'ADMIN') {
    items.push({ index: '/user/list', title: '用户管理', icon: User })
  }
  return items
})

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title as string || '首页')

const handleMenuSelect = (index: string) => {
  router.push(index)
}

const handleCommand = async (command: string) => {
  if (command === 'profile') {
    router.push('/user/center')
  } else if (command === 'logout') {
    logoutDialogVisible.value = true
  }
}

const handleLogout = async () => {
  try {
    loading.value = true
    await logout()
    userStore.logout()
    router.push('/login')
    ElMessage.success('退出成功')
  } catch (error) {
    ElMessage.error('退出失败')
  } finally {
    loading.value = false
    logoutDialogVisible.value = false
  }
}
</script>

<style scoped>
/* 全局重置，让html/body铺满整个视口 */
html, body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}

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

.logout-dialog-content {
  text-align: center;
  padding: 20px 0;
}

.logout-icon {
  margin-bottom: 16px;
}

.logout-dialog-content p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
}
</style>
