---
alwaysApply: true
scene: frontend
---

# Vue 3 + TypeScript + Element Plus 前端开发规范

## 一、Composition API 优先

### ? 推荐写法
```vue
<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'

// 响应式数据
const loading = ref(false)
const formData = reactive({ name: '', password: '' })

// 计算属性
const isDisabled = computed(() => !formData.name)

// 生命周期
onMounted(() => {
  console.log('mounted')
})
</script>
```

### ? 禁止写法
```vue
<script lang="ts">
import { defineComponent } from 'vue'
export default defineComponent({
  setup() { /* Options API 混合写法 */ }
})
</script>
```

---

## 二、命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| **组件名** | PascalCase + 多单词 | `MedStatusTag.vue` `MedicineForm.vue` |
| **组合式函数** | use + PascalCase | `useMedicine.ts`, `useAuth.ts` |
| **变量** | camelCase | `const tableData = ref([])` |
| **常量** | UPPER_SNAKE_CASE | `const MAX_COUNT = 100` |
| **Props** | camelCase (define) | `defineProps<{ isVisible: boolean }>()` |
| **Emits** | camelCase | `defineEmits<{ submit: [data: any] }>()` |

---

## 三、TypeScript 严格模式

### ? 类型定义
```typescript
// src/types/medicine.ts
export interface Medicine {
  id: number
  name: string
  spec: string
  stock: number
  expireDate: string
}

// 使用
const tableData = ref<Medicine[]>([])
```

### ? 禁止 any
```typescript
const data: any = {}      // ?
const data: unknown = {}  // ? 先用 unknown
```

---

## 四、Element Plus 规范

### 1. 自动导入
- ? 使用 unplugin-vue-components 自动导入
- ? 禁止手动 import ElButton 等组件

### 2. 表格表单标准写法
```vue
<el-table :data="tableData" v-loading="loading">
  <el-table-column prop="name" label="药品名称" width="180" />
  <el-table-column label="操作" fixed="right">
    <template #default="{ row }">
      <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
    </template>
  </el-table-column>
</el-table>
```

---

## 五、样式规范

| 方案 | 使用场景 |
|------|---------|
| `<style scoped>` | 组件私有样式（90% 场景） |
| `:deep()` | 修改 Element Plus 内部样式 |
| `src/styles/variables.css` | 全局 CSS 变量 |

? 正确穿透写法：
```css
:deep(.el-table__body-wrapper) {
  overflow-x: auto;
}
```

---

## 六、目录新增规范

| 新增类型 | 存放位置 |
|---------|---------|
| 全局组件 | `src/components/{Name}.vue` |
| 业务页面 | `src/views/{module}/index.vue` |
| 接口请求 | `src/api/{module}.ts` |
| 组合式函数 | `src/composables/use{Name}.ts` |
| 工具函数 | `src/utils/{name}.ts` |
| 类型定义 | `src/types/{module}.ts` |

---

## 七、代码提交时自动应用的检查

1. 未使用的变量 → 前缀 `_` 标记
2. TypeScript 类型错误 → 必须修复，不可忽略
3. 相对路径 → 优先使用 `@/` 别名
4. props 默认值 → 使用 `withDefaults`
