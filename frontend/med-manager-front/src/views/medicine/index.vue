<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>药品管理</span>
        <el-button type="primary" @click="handleAdd">
          + 新增药品
        </el-button>
      </div>
    </template>

    <el-form :model="searchForm" inline class="search-form">
      <el-form-item label="药品名称">
        <el-input v-model="searchForm.name" placeholder="请输入药品名称" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="searchForm.category" placeholder="请选择" clearable style="width: 150px">
          <el-option label="感冒药" value="感冒药" />
          <el-option label="抗生素" value="抗生素" />
          <el-option label="止痛药" value="止痛药" />
          <el-option label="保健品" value="保健品" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch" :loading="loading">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="name" label="药品名称" min-width="120" />
      <el-table-column prop="spec" label="规格" min-width="120" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="manufacturer" label="厂家" min-width="150" show-overflow-tooltip class="hide-mobile" />
      <el-table-column prop="unit" label="单位" width="80" class="hide-mobile" />
      <el-table-column prop="stock" label="库存数量" width="100">
        <template #default="{ row }">
          <span :class="{ 'text-danger': row.stock < 10 }">{{ row.stock }}</span>
        </template>
      </el-table-column>
      <el-table-column label="有效期状态" width="100">
        <template #default="{ row }">
          <MedStatusTag :days="row.expireDays" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" @click="handleStock(row)">库存批次</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      class="pagination"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSearch"
      @current-change="handleSearch"
    />
  </el-card>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="药品名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入药品名称" />
      </el-form-item>
      <el-form-item label="规格" prop="spec">
        <el-input v-model="formData.spec" placeholder="如：0.5g*24粒" />
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-select v-model="formData.category" placeholder="请选择" style="width: 100%">
          <el-option label="感冒药" value="感冒药" />
          <el-option label="抗生素" value="抗生素" />
          <el-option label="止痛药" value="止痛药" />
          <el-option label="保健品" value="保健品" />
        </el-select>
      </el-form-item>
      <el-form-item label="生产厂家">
        <el-input v-model="formData.manufacturer" placeholder="请输入生产厂家" />
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-select v-model="formData.unit" placeholder="请选择" style="width: 100%">
          <el-option label="盒" value="盒" />
          <el-option label="瓶" value="瓶" />
          <el-option label="片" value="片" />
          <el-option label="支" value="支" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import MedStatusTag from '../../components/MedStatusTag.vue'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()
const isEdit = ref(false)

const searchForm = reactive({
  name: '',
  category: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  id: 0,
  name: '',
  spec: '',
  category: '',
  manufacturer: '',
  unit: '',
  remark: ''
})

const rules = {
  name: [{ required: true, message: '请输入药品名称', trigger: 'blur' }],
  spec: [{ required: true, message: '请输入规格', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  unit: [{ required: true, message: '请选择单位', trigger: 'change' }]
}

const tableData = ref([
  { id: 1, name: '感冒灵颗粒', spec: '10g*10袋', category: '感冒药', manufacturer: '三九医药', unit: '盒', stock: 15, expireDays: 256 },
  { id: 2, name: '阿莫西林胶囊', spec: '0.5g*24粒', category: '抗生素', manufacturer: '华北制药', unit: '盒', stock: 5, expireDays: 15 },
  { id: 3, name: '维生素C片', spec: '100mg*100片', category: '保健品', manufacturer: '汤臣倍健', unit: '瓶', stock: 80, expireDays: -3 },
  { id: 4, name: '布洛芬缓释胶囊', spec: '0.3g*20粒', category: '止痛药', manufacturer: '中美史克', unit: '盒', stock: 20, expireDays: 7 }
])

pagination.total = tableData.value.length

const dialogTitle = computed(() => isEdit.value ? '编辑药品' : '新增药品')

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('查询成功')
  }, 500)
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.category = ''
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    name: '',
    spec: '',
    category: '',
    manufacturer: '',
    unit: '',
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleStock = (row: any) => {
  ElMessage.info(`查看【${row.name}】的库存批次`)
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除药品【${row.name}】吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = tableData.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      tableData.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async valid => {
    if (!valid) return
    
    submitLoading.value = true
    
    setTimeout(() => {
      if (isEdit.value) {
        const index = tableData.value.findIndex(item => item.id === formData.id)
        if (index > -1) {
          Object.assign(tableData.value[index], formData)
        }
        ElMessage.success('编辑成功')
      } else {
        tableData.value.push({
          ...formData,
          id: Date.now(),
          stock: 0,
          expireDays: 365
        })
        pagination.total++
        ElMessage.success('新增成功')
      }
      submitLoading.value = false
      dialogVisible.value = false
    }, 800)
  })
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.text-danger {
  color: #f56c6c;
  font-weight: bold;
}

@media (max-width: 768px) {
  .hide-mobile {
    display: none;
  }
  
  .search-form .el-form-item {
    width: 100%;
  }
}
</style>

