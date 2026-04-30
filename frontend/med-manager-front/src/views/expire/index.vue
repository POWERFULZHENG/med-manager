<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>过期提醒</span>
        <el-tag type="danger" size="large">共 {{ statistics.expired }} 种药品已过期</el-tag>
      </div>
    </template>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="8">
        <el-statistic title="已过期药品" :value="statistics.expired" value-style="color: #f56c6c">
          <template #prefix>
            <el-icon><Warning /></el-icon>
          </template>
        </el-statistic>
      </el-col>
      <el-col :span="8">
        <el-statistic title="7天内过期" :value="statistics.week" value-style="color: #e6a23c">
          <template #prefix>
            <el-icon><Clock /></el-icon>
          </template>
        </el-statistic>
      </el-col>
      <el-col :span="8">
        <el-statistic title="30天内过期" :value="statistics.month" value-style="color: #67c23a">
          <template #prefix>
            <el-icon><Calendar /></el-icon>
          </template>
        </el-statistic>
      </el-col>
    </el-row>

    <el-alert
      title="以下药品即将过期，请及时处理！"
      type="warning"
      show-icon
      :closable="false"
      style="margin-bottom: 20px"
    />

    <el-table :data="tableData" stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="name" label="药品名称" min-width="120" />
      <el-table-column prop="batchNo" label="批次号" width="140" class="hide-mobile" />
      <el-table-column prop="spec" label="规格" min-width="120" />
      <el-table-column prop="expireDate" label="过期日期" width="120" />
      <el-table-column prop="quantity" label="剩余数量" width="100" />
      <el-table-column prop="location" label="存放位置" width="120" class="hide-mobile" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <MedStatusTag :days="row.days" />
        </template>
      </el-table-column>
      <el-table-column label="剩余天数" width="120">
        <template #default="{ row }">
          <el-tag :type="row.days <= 0 ? 'danger' : row.days <= 7 ? 'warning' : 'info'" size="large">
            {{ row.days <= 0 ? '已过期' : `${row.days} 天` }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.days <= 0" size="small" type="danger" @click="handleDestroy(row)">报废</el-button>
          <el-button v-else size="small" @click="handleUse(row)">优先使用</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Warning, Clock, Calendar } from '@element-plus/icons-vue'
import MedStatusTag from '../../components/MedStatusTag.vue'

const loading = ref(false)

const statistics = reactive({
  expired: 1,
  week: 2,
  month: 3
})

const tableData = ref([
  { id: 1, name: '布洛芬缓释胶囊', batchNo: 'B202601004', spec: '0.3g*20粒', expireDate: '2026-05-07', quantity: 2, location: '药柜B-02', days: 7 },
  { id: 2, name: '创可贴', batchNo: 'B202601005', spec: '100片/盒', expireDate: '2026-05-14', quantity: 1, location: '急救包', days: 14 },
  { id: 3, name: '碘伏消毒液', batchNo: 'B202601006', spec: '100ml', expireDate: '2026-04-27', quantity: 1, location: '药柜B-03', days: -3 }
])

const handleDestroy = (row: any) => {
  ElMessageBox.confirm(`确定要报废过期药品【${row.name}】吗？`, '提示', {
    confirmButtonText: '确定报废',
    cancelButtonText: '取消',
    type: 'warning',
    confirmButtonClass: 'el-button--danger'
  }).then(() => {
    const index = tableData.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      tableData.value.splice(index, 1)
      statistics.expired--
      ElMessage.success('报废成功')
    }
  }).catch(() => {})
}

const handleUse = (row: any) => {
  ElMessage.info(`请尽快使用【${row.name}】`)
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

@media (max-width: 768px) {
  .hide-mobile {
    display: none;
  }
  
  .el-col {
    margin-bottom: 20px;
  }
}
</style>

