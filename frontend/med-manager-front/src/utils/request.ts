import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import router from '../router'
import env from './env'

const request = axios.create({
  baseURL: env.api.baseUrl,
  timeout: 10000
})

request.interceptors.request.use(config => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})

request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
      ElMessage.error('��¼�ѹ��ڣ������µ�¼')
    } else {
      ElMessage.error(error.response?.data?.message || '����ʧ��')
    }
    return Promise.reject(error)
  }
)

export default request

