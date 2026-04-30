<template>
  <el-tag :type="tagType" size="small">
    {{ tagText }}
  </el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  days: number
}

const props = defineProps<Props>()

const tagType = computed(() => {
  if (props.days < 0) return 'danger'
  if (props.days < 30) return 'warning'
  return 'success'
})

const tagText = computed(() => {
  if (props.days < 0) return '已过期'
  if (props.days < 30) return '即将过期'
  return '正常'
})
</script>

