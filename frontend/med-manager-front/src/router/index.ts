import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

import Login from '../views/login/index.vue'
import Layout from '../layout/index.vue'
import Dashboard from '../views/dashboard/index.vue'
import Medicine from '../views/medicine/index.vue'
import MedicineForm from '../views/medicine/form.vue'
import Inventory from '../views/inventory/index.vue'
import Expire from '../views/expire/index.vue'
import User from '../views/user/index.vue'
import NotFound from '../views/error/404.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { title: '首页', requiresAuth: true }
      },
      {
        path: 'medicine',
        name: 'Medicine',
        component: Medicine,
        meta: { title: '药品管理', requiresAuth: true }
      },
      {
        path: 'medicine/add',
        name: 'MedicineAdd',
        component: MedicineForm,
        meta: { title: '添加药品', requiresAuth: true }
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: Inventory,
        meta: { title: '库存管理', requiresAuth: true }
      },
      {
        path: 'expire',
        name: 'Expire',
        component: Expire,
        meta: { title: '过期提醒', requiresAuth: true }
      },
      {
        path: 'user',
        name: 'User',
        component: User,
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound,
    meta: { title: '页面不存在' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from) => {
  document.title = `${to.meta.title || '家庭常用药管理系统'}`
  
  const token = localStorage.getItem('med_manager_token')
  
  if (to.path === '/') {
    return token ? '/dashboard' : '/login'
  }
  
  if (to.meta.requiresAuth && !token) {
    return '/login'
  }
  
  if (to.path === '/login' && token) {
    return '/dashboard'
  }
  
  return true
})

export default router

