import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import './style.css'
import './styles/variables.css'
import App from './App.vue'
import env from './utils/env'
import { useUserStore } from './stores/user'

if (env.debug.enabled) {
  console.log('应用启动，当前环境:', env.isDev ? '开发环境' : '生产环境')
  console.log('应用名称:', env.app.title)
  console.log('API地址:', env.api.baseUrl)
}

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

router.isReady().then(() => {
  const userStore = useUserStore()
  userStore.initFromStorage()
})

app.mount('#app')

if (env.debug.enabled) {
  console.log('Vue应用挂载成功')
}

