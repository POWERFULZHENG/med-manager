import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import env from './utils/env'

if (env.isDebug) {
  console.log('? 应用启动，当前环境:', env.isDev ? '开发环境' : '生产环境')
  console.log('? 应用名称:', env.title)
  console.log('? API地址:', env.apiBaseUrl)
}

const app = createApp(App)
app.mount('#app')
