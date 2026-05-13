<template>
  <div class="user-management">
    <div class="search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" style="width: 200px" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="searchForm.nickname" placeholder="请输入昵称" style="width: 200px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 120px" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">新增用户</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="id" label="用户ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="roleName" label="角色" width="100" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="success" v-if="row.status === 1" @click="handleToggleStatus(row, 0)">禁用</el-button>
          <el-button link type="warning" v-else @click="handleToggleStatus(row, 1)">启用</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="pagination.current"
      :page-size="pagination.size"
      :total="pagination.total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <el-dialog :title="formTitle" v-model="dialogVisible" width="500px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="formData.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="formData.roleId" placeholder="请选择角色">
            <el-option label="管理员" :value="1" />
            <el-option label="普通用户" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="!editMode" label="密码" prop="password">
          <el-input v-model="formData.password" type="password" placeholder="请输入密码（不填默认为123456）" show-password />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="formLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import {
  getUserList,
  createUser,
  updateUser,
  deleteUser,
  updateUserStatus,
  type UserVO,
  type UserRequest,
  type UserQueryRequest,
  type PageResponse
} from '@/api/user'

const loading = ref(false)
const dialogVisible = ref(false)
const formLoading = ref(false)
const formRef = ref<FormInstance>()

const searchForm = reactive<UserQueryRequest>({
  username: '',
  nickname: '',
  status: undefined,
  pageNum: 1,
  pageSize: 10
})

const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<UserVO[]>([])

const editMode = ref(false)
const formTitle = computed(() => editMode.value ? '编辑用户' : '新增用户')
const editingUserId = ref<number | null>(null)

const formData = reactive<UserRequest>({
  username: '',
  password: '',
  nickname: '',
  phone: '',
  roleId: 2,
  status: 1
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名长度不能少于3位', trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

const loadUserList = async () => {
  loading.value = true
  try {
    const response = await getUserList({
      ...searchForm,
      pageNum: pagination.value.current,
      pageSize: pagination.value.size
    })
    if (response.code === 200) {
      tableData.value = response.data.records
      pagination.value.total = response.data.total
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.value.current = 1
  loadUserList()
}

const handleReset = () => {
  searchForm.username = ''
  searchForm.nickname = ''
  searchForm.status = undefined
  pagination.value.current = 1
  loadUserList()
}

const handleAdd = () => {
  editMode.value = false
  formData.username = ''
  formData.password = ''
  formData.nickname = ''
  formData.phone = ''
  formData.roleId = 2
  formData.status = 1
  dialogVisible.value = true
}

const handleEdit = (row: UserVO) => {
  editMode.value = true
  editingUserId.value = row.id
  formData.username = row.username
  formData.password = ''
  formData.nickname = row.nickname
  formData.phone = row.phone || ''
  formData.roleId = row.roleId
  formData.status = row.status
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async valid => {
    if (!valid) return

    formLoading.value = true
    try {
      const requestData: UserRequest = {
        username: formData.username,
        nickname: formData.nickname,
        phone: formData.phone || undefined,
        roleId: formData.roleId,
        status: formData.status
      }
      if (!editMode.value && formData.password) {
        requestData.password = formData.password
      }

      let response
      if (editMode.value && editingUserId.value) {
        response = await updateUser(editingUserId.value, requestData)
      } else {
        response = await createUser(requestData)
      }

      if (response.code === 200) {
        ElMessage.success(editMode.value ? '编辑成功' : '新增成功')
        dialogVisible.value = false
        loadUserList()
      } else {
        ElMessage.error(response.message)
      }
    } catch (error) {
      ElMessage.error(editMode.value ? '编辑失败' : '新增失败')
    } finally {
      formLoading.value = false
    }
  })
}

const handleToggleStatus = async (row: UserVO, status: number) => {
  try {
    const response = await updateUserStatus(row.id, status)
    if (response.code === 200) {
      ElMessage.success(status === 1 ? '已启用' : '已禁用')
      loadUserList()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(status === 1 ? '启用失败' : '禁用失败')
  }
}

const handleDelete = async (row: UserVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${row.nickname}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteUser(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadUserList()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (size: number) => {
  pagination.value.size = size
  loadUserList()
}

const handleCurrentChange = (current: number) => {
  pagination.value.current = current
  loadUserList()
}

onMounted(() => {
  loadUserList()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
}

.action-bar {
  margin-bottom: 16px;
}

:deep(.el-pagination) {
  margin-top: 20px;
  text-align: right;
}
</style>
