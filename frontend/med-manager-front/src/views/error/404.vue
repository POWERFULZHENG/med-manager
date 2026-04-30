<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-code">404</div>
      <div class="error-message">
        <h2>页面不存在</h2>
        <p>抱歉，您访问的页面不存在或已被移除</p>
      </div>
      <el-button type="primary" size="large" @click="handleGoHome">
        <el-icon><House /></el-icon>
        返回首页
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { House } from '@element-plus/icons-vue'

const router = useRouter()

const handleGoHome = () => {
  router.push('/dashboard')
}
</script>

<style scoped>
.error-page {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.error-content {
  text-align: center;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1;
  margin-bottom: 20px;
}

.error-message h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 12px;
}

.error-message p {
  color: #666;
  margin-bottom: 30px;
}
</style>
