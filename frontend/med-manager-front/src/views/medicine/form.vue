<template>
  <div class="medicine-form">
    <el-card title="添加药品">
      <el-form label-width="100px" style="max-width: 600px">
        <el-form-item label="药品名称">
          <el-input placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="药品分类">
          <el-select placeholder="请选择分类" style="width: 100%">
            <el-option label="感冒药" value="1" />
            <el-option label="退烧药" value="2" />
            <el-option label="止痛药" value="3" />
            <el-option label="抗生素" value="4" />
            <el-option label="外用药" value="5" />
            <el-option label="保健品" value="6" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格">
          <el-input placeholder="如：0.5g*24粒" />
        </el-form-item>
        <el-form-item label="生产厂家">
          <el-input placeholder="请输入生产厂家" />
        </el-form-item>
        <el-form-item label="有效期">
          <el-date-picker type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input-number :min="0" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="$router.back()">保存</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>

