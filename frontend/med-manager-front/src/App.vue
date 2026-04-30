<script setup lang="ts">
import { ref } from 'vue'
// 无需手动导入组件，直接使用
const drawerVisible = ref(false)
const dialogVisible = ref(false)
</script>

<template>
  <div style="padding: 20px;">
    <h1>Element Plus 引入成功 ✅</h1>
    <!-- Element Plus 按钮 -->
    <el-button type="primary">主要按钮</el-button>
    <el-button type="success">成功按钮</el-button>
    <!-- 测试你要用的 Drawer/Dialog -->
    <el-button @click="drawerVisible = true">打开抽屉</el-button>
    <el-button @click="dialogVisible = true">打开弹窗</el-button>

    <el-drawer v-model="drawerVisible" title="抽屉测试"></el-drawer>
    <el-dialog v-model="dialogVisible" title="弹窗测试"></el-dialog>
  </div>
</template>