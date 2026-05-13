/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_VERSION: string
  readonly VITE_APP_DEFAULT_PAGE_SIZE: string
  readonly VITE_APP_MAX_PAGE_SIZE: string
  readonly VITE_APP_TOKEN_KEY: string
  readonly VITE_APP_USER_INFO_KEY: string
  readonly VITE_API_BASE_URL: string
  readonly VITE_APP_DEBUG: string
  readonly VITE_USE_MOCK: string
  readonly VITE_MOCK_DELAY: string
  readonly VITE_ENABLE_ROUTER_TRACE: string
  readonly VITE_DROP_CONSOLE: string
  readonly VITE_DEV_SERVER_PORT?: string
  readonly VITE_DEV_OPEN_BROWSER?: string
  readonly VITE_BUILD_SOURCEMAP?: string
  readonly VITE_SENTRY_DSN?: string
  readonly VITE_MOCK_SECRET_KEY?: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

