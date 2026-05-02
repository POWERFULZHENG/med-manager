<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>库存管理</span>
        <el-button type="primary" @click="handleInStock">
          + 药品入库
        </el-button>
      </div>
    </template>

    <el-alert
      title="库存预警：以下药品库存不足 10 件，请及时补充！"
      type="warning"
      :closable="false"
      style="margin-bottom: 20px"
      v-if="warningCount > 0"
    />

    <el-table :data="tableData" stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="name" label="药品名称" min-width="120" />
      <el-table-column prop="batchNo" label="批次号" width="140" class="hide-mobile" />
      <el-table-column prop="spec" label="规格" min-width="120" />
      <el-table-column prop="quantity" label="当前库存" width="120">
        <template #default="{ row }">
          <el-tag :type="row.quantity < 10 ? 'danger' : 'success'">
            {{ row.quantity }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="expireDate" label="过期日期" width="120" class="hide-mobile" />
      <el-table-column label="有效期状态" width="100">
        <template #default="{ row }">
          <MedStatusTag :days="row.expireDays" />
        </template>
      </el-table-column>
      <el-table-column prop="location" label="存放位置" width="120" class="hide-mobile" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="handleIn(row)">入库</el-button>
          <el-button size="small" @click="handleOut(row)" :disabled="row.quantity === 0">出库</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      class="pagination"
      layout="total, sizes, prev, pager, next"
    />
  </el-card>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="药品">
        <el-input :value="currentMedicine" disabled />
      </el-form-item>
      <el-form-item label="操作数量" prop="quantity">
        <el-input-number
          v-model="formData.quantity"
          :min="1"
          :max="maxQuantity"
          style="width: 100%"
        />
        <span class="form-tip">单位：({{ currentUnit }})</span>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="formData.remark" type="textarea" :rows="3" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import MedStatusTag from '../../components/MedStatusTag.vue'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const operationType = ref<'in' | 'out'>('in')
const currentMedicine = ref('')
const currentUnit = ref('盒')

const tableData = ref([
  { id: 1, name: '感冒灵颗粒', batchNo: 'B202601001', spec: '10g*10袋', quantity: 15, unit: '盒', expireDate: '2026-12-31', expireDays: 256, location: '药柜A-01' },
  { id: 2, name: '阿莫西林胶囊', batchNo: 'B202601002', spec: '0.5g*24粒', quantity: 5, unit: '盒', expireDate: '2027-06-30', expireDays: 15, location: '药柜A-02' },
  { id: 3, name: '维生素C片', batchNo: 'B202601003', spec: '100mg*100片', quantity: 80, unit: '瓶', expireDate: '2026-04-27', expireDays: -3, location: '药柜B-01' },
  { id: 4, name: '布洛芬缓释胶囊', batchNo: 'B202601004', spec: '0.3g*20粒', quantity: 20, unit: '盒', expireDate: '2026-05-07', expireDays: 7, location: '药柜B-02' }
])

const pagination = reactive({
  page: 1,
  size: 10,
  total: tableData.value.length
})

const formData = reactive({
  quantity: 1,
  remark: ''
})

const rules = {
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
}

const warningCount = computed(() => tableData.value.filter(item => item.quantity < 10).length)

const dialogTitle = computed(() => operationType.value === 'in' ? '药品入库' : '药品出库')

const maxQuantity = computed(() => operationType.value === 'out' ? 9999 : 9999)

const handleInStock = () => {
  ElMessage.info('请先选择药品')
}

const handleIn = (row: any) => {
  operationType.value = 'in'
  currentMedicine.value = row.name
  currentUnit.value = row.unit
  formData.quantity = 1
  formData.remark = ''
  dialogVisible.value = true
}

const handleOut = async (row: any) => {
  if (row.expireDays < 0) {
    try {
      await ElMessageBox.confirm(
        '该药品已过期，建议销毁，请确认是否继续领取？',
        '过期药品警告',
        {
          confirmButtonText: '确认领取',
          cancelButtonText: '取消',
          type: 'error',
          confirmButtonClass: 'el-button--danger'
        }
      )
    } catch {
      return
    }
  } else if (row.quantity === 1) {
    try {
      await ElMessageBox.confirm(
        '此操作将导致该批次库存清零，请确认！',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
    } catch {
      return
    }
  }
  
  operationType.value = 'out'
  currentMedicine.value = row.name
  currentUnit.value = row.unit
  formData.quantity = 1
  formData.remark = ''
  dialogVisible.value = true
}

const handleSubmit = () => {
  submitLoading.value = true
  setTimeout(() => {
    ElMessage.success(operationType.value === 'in' ? '入库成功' : '出库成功')
    submitLoading.value = false
    dialogVisible.value = false
  }, 800)
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-left: 8px;
}

@media (max-width: 768px) {
  .hide-mobile {
    display: none;
  }
}
</style>

