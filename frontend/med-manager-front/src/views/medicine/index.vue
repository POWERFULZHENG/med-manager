<template>
  <div class="medicine-management">
    <div class="search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="药品名称">
          <el-input v-model="searchForm.medicineName" placeholder="请输入药品名称" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="生产厂家">
          <el-input v-model="searchForm.manufacturer" placeholder="请输入生产厂家" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="剂型">
          <el-select v-model="searchForm.dosageForm" placeholder="请选择剂型" style="width: 150px" clearable>
            <el-option label="片剂" value="片剂" />
            <el-option label="胶囊" value="胶囊" />
            <el-option label="颗粒" value="颗粒" />
            <el-option label="口服液" value="口服液" />
            <el-option label="注射剂" value="注射剂" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">新增药品</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="id" label="药品ID" width="80" />
      <el-table-column prop="medicineName" label="药品名称" min-width="150" />
      <el-table-column prop="specification" label="规格" width="120" />
      <el-table-column prop="manufacturer" label="生产厂家" min-width="150" show-overflow-tooltip />
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="dosageForm" label="剂型" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog :title="formTitle" v-model="dialogVisible" width="600px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="药品名称" prop="medicineName">
          <el-input v-model="formData.medicineName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="规格" prop="specification">
          <el-input v-model="formData.specification" placeholder="如：0.5g*24粒" />
        </el-form-item>
        <el-form-item label="生产厂家" prop="manufacturer">
          <el-input v-model="formData.manufacturer" placeholder="请输入生产厂家" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="formData.unit" placeholder="请选择单位" style="width: 100%">
            <el-option label="盒" value="盒" />
            <el-option label="瓶" value="瓶" />
            <el-option label="片" value="片" />
            <el-option label="支" value="支" />
            <el-option label="袋" value="袋" />
          </el-select>
        </el-form-item>
        <el-form-item label="剂型" prop="dosageForm">
          <el-select v-model="formData.dosageForm" placeholder="请选择剂型" style="width: 100%">
            <el-option label="片剂" value="片剂" />
            <el-option label="胶囊" value="胶囊" />
            <el-option label="颗粒" value="颗粒" />
            <el-option label="口服液" value="口服液" />
            <el-option label="注射剂" value="注射剂" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
  getMedicineList,
  createMedicine,
  updateMedicine,
  deleteMedicine,
  type MedicineVO,
  type MedicineRequest,
  type MedicineQueryRequest
} from '@/api/medicine'

const loading = ref(false)
const dialogVisible = ref(false)
const formLoading = ref(false)
const formRef = ref<FormInstance>()

const searchForm = reactive<MedicineQueryRequest>({
  medicineName: '',
  manufacturer: '',
  dosageForm: '',
  pageNum: 1,
  pageSize: 10
})

const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<MedicineVO[]>([])

const editMode = ref(false)
const formTitle = computed(() => editMode.value ? '编辑药品' : '新增药品')
const editingMedicineId = ref<number | null>(null)

const formData = reactive<MedicineRequest>({
  medicineName: '',
  specification: '',
  manufacturer: '',
  unit: '',
  dosageForm: '',
  remark: ''
})

const rules = {
  medicineName: [
    { required: true, message: '请输入药品名称', trigger: 'blur' },
    { max: 100, message: '药品名称长度不能超过100个字符', trigger: 'blur' }
  ],
  unit: [
    { required: true, message: '请选择单位', trigger: 'change' }
  ]
}

const loadMedicineList = async () => {
  loading.value = true
  try {
    const response = await getMedicineList({
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
    ElMessage.error('获取药品列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.value.current = 1
  loadMedicineList()
}

const handleReset = () => {
  searchForm.medicineName = ''
  searchForm.manufacturer = ''
  searchForm.dosageForm = ''
  pagination.value.current = 1
  loadMedicineList()
}

const handleAdd = () => {
  editMode.value = false
  formData.medicineName = ''
  formData.specification = ''
  formData.manufacturer = ''
  formData.unit = ''
  formData.dosageForm = ''
  formData.remark = ''
  dialogVisible.value = true
}

const handleEdit = (row: MedicineVO) => {
  editMode.value = true
  editingMedicineId.value = row.id
  formData.medicineName = row.medicineName
  formData.specification = row.specification || ''
  formData.manufacturer = row.manufacturer || ''
  formData.unit = row.unit
  formData.dosageForm = row.dosageForm || ''
  formData.remark = row.remark || ''
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async valid => {
    if (!valid) return

    formLoading.value = true
    try {
      let response
      if (editMode.value && editingMedicineId.value) {
        response = await updateMedicine(editingMedicineId.value, formData)
      } else {
        response = await createMedicine(formData)
      }

      if (response.code === 200) {
        ElMessage.success(editMode.value ? '编辑成功' : '新增成功')
        dialogVisible.value = false
        loadMedicineList()
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

const handleDelete = async (row: MedicineVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除药品 "${row.medicineName}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteMedicine(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadMedicineList()
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
  loadMedicineList()
}

const handleCurrentChange = (current: number) => {
  pagination.value.current = current
  loadMedicineList()
}

onMounted(() => {
  loadMedicineList()
})
</script>

<style scoped>
.medicine-management {
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