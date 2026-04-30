const parseBoolean = (value: string | undefined): boolean => {
  return value === 'true' || value === '1'
}

const parseNumber = (value: string | undefined, defaultValue: number): number => {
  const num = Number(value)
  return isNaN(num) ? defaultValue : num
}

export const env = {
  app: {
    title: import.meta.env.VITE_APP_TITLE || '家庭药品管理系统',
    version: import.meta.env.VITE_APP_VERSION || '1.0.0',
    defaultPageSize: parseNumber(import.meta.env.VITE_APP_DEFAULT_PAGE_SIZE, 10),
    maxPageSize: parseNumber(import.meta.env.VITE_APP_MAX_PAGE_SIZE, 100),
    tokenKey: import.meta.env.VITE_APP_TOKEN_KEY || 'med_manager_token',
    userInfoKey: import.meta.env.VITE_APP_USER_INFO_KEY || 'med_manager_user',
  },
  
  api: {
    baseUrl: import.meta.env.VITE_API_BASE_URL || '/api',
    timeout: 30000,
  },
  
  debug: {
    enabled: parseBoolean(import.meta.env.VITE_APP_DEBUG),
    routerTrace: parseBoolean(import.meta.env.VITE_ENABLE_ROUTER_TRACE),
    dropConsole: parseBoolean(import.meta.env.VITE_DROP_CONSOLE),
  },
  
  mock: {
    enabled: parseBoolean(import.meta.env.VITE_USE_MOCK),
    delay: parseNumber(import.meta.env.VITE_MOCK_DELAY, 500),
    secretKey: import.meta.env.VITE_MOCK_SECRET_KEY || '',
  },
  
  isDev: import.meta.env.DEV,
  isProd: import.meta.env.PROD,
}

export default env

