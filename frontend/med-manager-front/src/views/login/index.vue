<template>
  <div class="login-container">
    <div class="login-box">
      <div v-if="showLogoutTip" class="logout-tip">
        <div class="logout-icon">
          <span class="check-mark">✓</span>
        </div>
        <p class="logout-title">已安全退出</p>
        <p class="logout-desc">期待您再次使用系统</p>
      </div>
      
      <h2 class="login-title">家庭常用药管理系统</h2>
      <el-form ref="formRef" :model="loginForm" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
            :disabled="loading"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
            :disabled="loading"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-tip">
        <p>提示：输入任意用户名和密码即可登录测试</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { ElMessage, type FormInstance } from 'element-plus'
import { login, type LoginData } from '../../api/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)
const showLogoutTip = ref(false)

const loginForm = reactive<LoginData>({
  username: 'admin',
  password: '123456'
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async valid => {
    if (!valid) return

    loading.value = true

    try {
      const result = await login(loginForm)

      if (result.code === 200) {
        userStore.setToken(result.data.token)
        userStore.setUserInfo({
          id: result.data.user.userId,
          username: result.data.user.username,
          nickname: result.data.user.nickname,
          roleCode: result.data.user.roleCode
        })

        ElMessage.success('登录成功')
        router.push('/dashboard')
      } else {
        ElMessage.error(result.message || '登录失败')
      }
    } catch (error: any) {
      ElMessage.error(error.response?.data?.message || '登录失败')
    } finally {
      loading.value = false
    }
  })
}

onMounted(() => {
  if (route.query.logout) {
    showLogoutTip.value = true
    setTimeout(() => {
      showLogoutTip.value = false
    }, 3000)
  }
})
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.logout-tip {
  text-align: center;
  padding: 20px;
  background: #f6ffed;
  border-radius: 8px;
  margin-bottom: 20px;
  animation: fadeIn 0.5s ease;
}

.logout-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: #67c23a;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
}

.check-mark {
  font-size: 36px;
  color: #fff;
  font-weight: bold;
}

.logout-title {
  font-size: 18px;
  font-weight: 600;
  color: #67c23a;
  margin: 0 0 8px 0;
}

.logout-desc {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.login-tip {
  text-align: center;
  margin-top: 20px;
  color: #999;
  font-size: 12px;
}

.login-tip p {
  margin: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

