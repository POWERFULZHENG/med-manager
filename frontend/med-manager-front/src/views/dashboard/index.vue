<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-cards">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <el-statistic title="药品总数" :value="stats.medicineCount">
            <template #prefix>
              <el-icon class="stat-icon medicine"><FirstAidKit /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <el-statistic title="库存总数" :value="stats.inventoryCount">
            <template #prefix>
              <el-icon class="stat-icon inventory"><Grid /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card warning-card" shadow="hover">
          <el-statistic title="即将过期" :value="stats.expireCount" value-style="color: #f56c6c">
            <template #prefix>
              <el-icon class="stat-icon warning"><Warning /></el-icon>
            </template>
          </el-statistic>
          <div class="stat-tag">
            <el-tag type="danger" size="small">需要关注</el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card" shadow="hover">
          <el-statistic title="家庭成员" :value="stats.familyCount">
            <template #prefix>
              <el-icon class="stat-icon family"><User /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :xs="24" :md="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近添加药品</span>
              <el-button type="primary" size="small" @click="$router.push('/medicine/add')">
                + 添加药品
              </el-button>
            </div>
          </template>
          <el-table :data="recentMedicines" stripe style="width: 100%">
            <el-table-column prop="name" label="药品名称" min-width="120" />
            <el-table-column prop="category" label="分类" width="100" />
            <el-table-column prop="quantity" label="数量" width="80" align="center" />
            <el-table-column label="状态" width="100" align="center">
              <template #default="{ row }">
                <MedStatusTag :days="row.expireDays" />
              </template>
            </el-table-column>
            <el-table-column prop="expireDate" label="有效期" width="120" align="center" class="hide-mobile" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8" style="margin-top: 20px">
        <el-row :gutter="0">
          <el-col :span="24" style="margin-bottom: 20px">
            <el-card class="quick-entry" shadow="hover" @click.native="$router.push('/medicine/add')">
              <el-icon class="entry-icon"><Camera /></el-icon>
              <p>扫码/拍照入库</p>
              <span class="entry-desc">快速录入药品信息</span>
            </el-card>
          </el-col>
        </el-row>
        <el-card shadow="hover">
          <template #header>
            <span>过期预警</span>
            <el-tag type="danger" size="small" style="margin-left: 10px">{{ expireWarnings.length }} 条</el-tag>
          </template>
          <div class="warning-list">
            <div v-for="item in expireWarnings" :key="item.id" class="warning-item">
              <MedStatusTag :days="item.days" />
              <span class="warning-name">{{ item.name }}</span>
              <el-tag type="danger" size="small" class="warning-day">{{ item.days }}天后</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { FirstAidKit, Grid, Warning, User, Camera } from '@element-plus/icons-vue'
import MedStatusTag from '../../components/MedStatusTag.vue'

const stats = reactive({
  medicineCount: 42,
  inventoryCount: 128,
  expireCount: 5,
  familyCount: 4
})

const recentMedicines = [
  { name: '感冒灵颗粒', category: '感冒药', quantity: 10, expireDate: '2026-12-31', expireDays: 256 },
  { name: '阿莫西林胶囊', category: '抗生素', quantity: 24, expireDate: '2027-06-30', expireDays: 430 },
  { name: '维生素C片', category: '保健品', quantity: 100, expireDate: '2026-09-15', expireDays: -3 },
  { name: '布洛芬缓释胶囊', category: '止痛药', quantity: 20, expireDate: '2026-11-20', expireDays: 7 }
]

const expireWarnings = [
  { id: 1, name: '布洛芬缓释胶囊', days: 7 },
  { id: 2, name: '创可贴', days: 14 },
  { id: 3, name: '碘伏消毒液', days: 30 }
]
</script>

<style scoped>
.stat-cards {
  margin-bottom: 0;
}

.stat-card {
  position: relative;
  transition: all 0.3s;
}

.stat-card :deep(.el-statistic__head) {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.stat-card :deep(.el-statistic__content) {
  display: flex;
  align-items: center;
}

.stat-icon {
  margin-right: 12px;
  font-size: 32px;
  padding: 8px;
  border-radius: 8px;
}

.stat-icon.medicine {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.stat-icon.inventory {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: #fff;
}

.stat-icon.warning {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #fff;
}

.stat-icon.family {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #fff;
}

.warning-card .stat-tag {
  position: absolute;
  top: 20px;
  right: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quick-entry {
  text-align: center;
  cursor: pointer;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  transition: transform 0.3s;
}

.quick-entry:hover {
  transform: translateY(-5px);
}

.quick-entry .entry-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.quick-entry p {
  margin: 0 0 4px;
  font-size: 18px;
  font-weight: bold;
}

.quick-entry .entry-desc {
  font-size: 12px;
  opacity: 0.9;
}

.warning-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.warning-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.warning-item:last-child {
  border-bottom: none;
}

.warning-name {
  flex: 1;
  margin: 0 10px;
}

@media (max-width: 768px) {
  .hide-mobile {
    display: none;
  }
}
</style>
