import request from '../utils/request'

export interface LoginData {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  userInfo: {
    id: number
    username: string
    nickname: string
    avatar: string
  }
}

export const login = (data: LoginData): Promise<LoginResponse> => {
  return new Promise((resolve, reject) => {
    console.log('[Mock API] ��¼����:', data)
    
    setTimeout(() => {
      if (data.username && data.password) {
        const response: LoginResponse = {
          token: 'mock_token_' + Date.now(),
          userInfo: {
            id: 1,
            username: data.username,
            nickname: '����Ա',
            avatar: ''
          }
        }
        console.log('[Mock API] ��¼�ɹ�:', response)
        resolve(response)
      } else {
        reject(new Error('�û��������벻��Ϊ��'))
      }
    }, 800)
  })
}

export const logout = () => {
  return new Promise<void>(resolve => {
    setTimeout(() => {
      console.log('[Mock API] �˳���¼')
      resolve()
    }, 300)
  })
}

