<template>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-card>
        <template #header>
          <span>个人信息</span>
        </template>
        <div class="user-info">
          <el-avatar :size="80">
            {{ userStore.userInfo?.username?.charAt(0)?.toUpperCase() }}
          </el-avatar>
          <h2>{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</h2>
          <p class="user-id">ID: {{ userStore.userInfo?.id }}</p>
          <el-divider />
          <div class="user-detail">
            <p><span>用户名：</span>{{ userStore.userInfo?.username }}</p>
            <p><span>角色：</span>管理员</p>
            <p><span>登录状态：</span><el-tag type="success">已登录</el-tag></p>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="16">
      <el-card>
        <template #header>
          <span>修改密码</span>
        </template>
        <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px" style="max-width: 500px">
          <el-form-item label="原密码" prop="oldPassword">
            <el-input v-model="formData.oldPassword" type="password" placeholder="请输入原密码" show-password />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="formData.newPassword" type="password" placeholder="请输入新密码" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="formData.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">保存修改</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()
const loading = ref(false)
const formRef = ref<FormInstance>()

const formData = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: any, callback: any) => {
        if (value !== formData.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async valid => {
    if (!valid) return
    
    loading.value = true
    setTimeout(() => {
      ElMessage.success('密码修改成功')
      loading.value = false
      handleReset()
    }, 1000)
  })
}

const handleReset = () => {
  formData.oldPassword = ''
  formData.newPassword = ''
  formData.confirmPassword = ''
  formRef.value?.resetFields()
}
</script>

<style scoped>
.user-info {
  text-align: center;
  padding: 20px 0;
}

.user-info h2 {
  margin: 16px 0 8px;
  font-size: 20px;
}

.user-id {
  color: #666;
  margin: 0;
}

.user-detail {
  text-align: left;
  padding: 0 20px;
}

.user-detail p {
  margin: 12px 0;
  display: flex;
  align-items: center;
}

.user-detail p span {
  color: #666;
  width: 80px;
}
</style>

