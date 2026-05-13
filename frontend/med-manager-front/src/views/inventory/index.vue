<template>
  <div class="inventory-management">
    <div class="search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="药品名称">
          <el-input v-model="searchForm.medicineName" placeholder="请输入药品名称" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="存放位置">
          <el-input v-model="searchForm.storageLocation" placeholder="请输入存放位置" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.isExpiring" placeholder="临期" style="width: 120px" clearable>
            <el-option label="临期药品" :value="true" />
          </el-select>
          <el-select v-model="searchForm.isExpired" placeholder="过期" style="width: 120px; margin-left: 8px" clearable>
            <el-option label="过期药品" :value="true" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="action-bar">
      <el-button type="primary" @click="handleInStock">药品入库</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="medicineName" label="药品名称" min-width="150" />
      <el-table-column prop="batchNo" label="批次号" width="140" />
      <el-table-column prop="specification" label="规格" width="120" />
      <el-table-column prop="quantity" label="当前库存" width="100">
        <template #default="{ row }">
          <el-tag :type="row.quantity < 10 ? 'danger' : 'success'">
            {{ row.quantity }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="expireDate" label="过期日期" width="120" />
      <el-table-column label="有效期状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getExpireTagType(row.expireDays)">
            {{ getExpireText(row.expireDays) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="storageLocation" label="存放位置" width="120" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleIn(row)">入库</el-button>
          <el-button link type="primary" @click="handleOut(row)" :disabled="row.quantity === 0">出库</el-button>
          <el-button link type="primary" @click="handleAdjust(row)">调整</el-button>
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

    <el-dialog title="药品入库" v-model="inStockDialogVisible" width="600px">
      <el-form ref="inStockFormRef" :model="inStockFormData" :rules="inStockRules" label-width="100px">
        <el-form-item label="药品" prop="medicineId">
          <el-select v-model="inStockFormData.medicineId" placeholder="请选择药品" style="width: 100%" filterable>
            <el-option
              v-for="item in medicineList"
              :key="item.id"
              :label="item.medicineName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="批次号" prop="batchNo">
          <el-input v-model="inStockFormData.batchNo" placeholder="请输入批次号" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="inStockFormData.quantity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="过期日期" prop="expireDate">
          <el-date-picker v-model="inStockFormData.expireDate" type="date" placeholder="请选择过期日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="存放位置">
          <el-input v-model="inStockFormData.storageLocation" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="inStockFormData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="inStockDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="formLoading" @click="handleInStockSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog :title="operationTitle" v-model="operationDialogVisible" width="500px">
      <el-form ref="operationFormRef" :model="operationFormData" :rules="operationRules" label-width="100px">
        <el-form-item label="药品">
          <el-input :value="currentStock?.medicineName" disabled />
        </el-form-item>
        <el-form-item label="批次号">
          <el-input :value="currentStock?.batchNo" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input :value="`${currentStock?.quantity} ${currentStock?.unit}`" disabled />
        </el-form-item>
        <el-form-item label="操作数量" prop="quantity">
          <el-input-number
            v-model="operationFormData.quantity"
            :min="1"
            :max="operationType === 'out' ? currentStock?.quantity : 9999"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="operationFormData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="operationDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="formLoading" @click="handleOperationSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import {
  getStockList,
  stockIn,
  stockOut,
  adjustStock,
  type StockVO,
  type StockInRequest,
  type StockOutRequest,
  type StockAdjustRequest,
  type StockQueryRequest
} from '@/api/stock'
import { getMedicineList, type MedicineVO } from '@/api/medicine'

const loading = ref(false)
const formLoading = ref(false)
const inStockDialogVisible = ref(false)
const operationDialogVisible = ref(false)
const inStockFormRef = ref<FormInstance>()
const operationFormRef = ref<FormInstance>()

const searchForm = reactive<StockQueryRequest>({
  medicineName: '',
  isExpiring: undefined,
  isExpired: undefined,
  storageLocation: '',
  pageNum: 1,
  pageSize: 10
})

const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<StockVO[]>([])
const medicineList = ref<MedicineVO[]>([])

const operationType = ref<'in' | 'out' | 'adjust'>('in')
const currentStock = ref<StockVO | null>(null)

const inStockFormData = reactive<StockInRequest>({
  medicineId: 0,
  batchNo: '',
  quantity: 1,
  expireDate: '',
  storageLocation: '',
  remark: ''
})

const operationFormData = reactive({
  quantity: 1,
  remark: ''
})

const inStockRules = {
  medicineId: [{ required: true, message: '请选择药品', trigger: 'change' }],
  batchNo: [{ required: true, message: '请输入批次号', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  expireDate: [{ required: true, message: '请选择过期日期', trigger: 'change' }]
}

const operationRules = {
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
}

const operationTitle = computed(() => {
  if (operationType.value === 'in') return '药品入库'
  if (operationType.value === 'out') return '药品出库'
  return '库存调整'
})

const getExpireTagType = (days: number) => {
  if (days < 0) return 'danger'
  if (days <= 7) return 'danger'
  if (days <= 30) return 'warning'
  return 'success'
}

const getExpireText = (days: number) => {
  if (days < 0) return `已过期${Math.abs(days)}天`
  if (days === 0) return '今日过期'
  if (days <= 7) return `${days}天后过期`
  if (days <= 30) return `${days}天后过期`
  return '正常'
}

const loadStockList = async () => {
  loading.value = true
  try {
    const response = await getStockList({
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
    ElMessage.error('获取库存列表失败')
  } finally {
    loading.value = false
  }
}

const loadMedicineList = async () => {
  try {
    const response = await getMedicineList({ pageNum: 1, pageSize: 1000 })
    if (response.code === 200) {
      medicineList.value = response.data.records
    }
  } catch (error) {
    console.error('获取药品列表失败', error)
  }
}

const handleSearch = () => {
  pagination.value.current = 1
  loadStockList()
}

const handleReset = () => {
  searchForm.medicineName = ''
  searchForm.isExpiring = undefined
  searchForm.isExpired = undefined
  searchForm.storageLocation = ''
  pagination.value.current = 1
  loadStockList()
}

const handleInStock = () => {
  inStockFormData.medicineId = 0
  inStockFormData.batchNo = ''
  inStockFormData.quantity = 1
  inStockFormData.expireDate = ''
  inStockFormData.storageLocation = ''
  inStockFormData.remark = ''
  inStockDialogVisible.value = true
}

const handleIn = (row: StockVO) => {
  operationType.value = 'in'
  currentStock.value = row
  operationFormData.quantity = 1
  operationFormData.remark = ''
  operationDialogVisible.value = true
}

const handleOut = async (row: StockVO) => {
  if (row.expireDays < 0) {
    try {
      await ElMessageBox.confirm(
        '该药品已过期，建议销毁，请确认是否继续领取？',
        '过期药品警告',
        {
          confirmButtonText: '确认领取',
          cancelButtonText: '取消',
          type: 'error'
        }
      )
    } catch {
      return
    }
  }

  operationType.value = 'out'
  currentStock.value = row
  operationFormData.quantity = 1
  operationFormData.remark = ''
  operationDialogVisible.value = true
}

const handleAdjust = (row: StockVO) => {
  operationType.value = 'adjust'
  currentStock.value = row
  operationFormData.quantity = row.quantity
  operationFormData.remark = ''
  operationDialogVisible.value = true
}

const handleInStockSubmit = async () => {
  if (!inStockFormRef.value) return

  await inStockFormRef.value.validate(async valid => {
    if (!valid) return

    formLoading.value = true
    try {
      const response = await stockIn({
        ...inStockFormData,
        expireDate: new Date(inStockFormData.expireDate).toISOString().split('T')[0]
      })
      if (response.code === 200) {
        ElMessage.success('入库成功')
        inStockDialogVisible.value = false
        loadStockList()
      } else {
        ElMessage.error(response.message)
      }
    } catch (error) {
      ElMessage.error('入库失败')
    } finally {
      formLoading.value = false
    }
  })
}

const handleOperationSubmit = async () => {
  if (!operationFormRef.value) return

  await operationFormRef.value.validate(async valid => {
    if (!valid) return

    formLoading.value = true
    try {
      let response
      if (operationType.value === 'out' && currentStock.value) {
        response = await stockOut({
          stockId: currentStock.value.id,
          quantity: operationFormData.quantity,
          remark: operationFormData.remark
        })
      } else if (operationType.value === 'adjust' && currentStock.value) {
        response = await adjustStock(currentStock.value.id, {
          quantity: operationFormData.quantity,
          remark: operationFormData.remark
        })
      }

      if (response && response.code === 200) {
        ElMessage.success(operationType.value === 'out' ? '出库成功' : '调整成功')
        operationDialogVisible.value = false
        loadStockList()
      } else if (response) {
        ElMessage.error(response.message)
      }
    } catch (error) {
      ElMessage.error(operationType.value === 'out' ? '出库失败' : '调整失败')
    } finally {
      formLoading.value = false
    }
  })
}

const handleSizeChange = (size: number) => {
  pagination.value.size = size
  loadStockList()
}

const handleCurrentChange = (current: number) => {
  pagination.value.current = current
  loadStockList()
}

onMounted(() => {
  loadStockList()
  loadMedicineList()
})
</script>

<style scoped>
.inventory-management {
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