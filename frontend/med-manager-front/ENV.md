# 环境变量配置说明

## ? 文件优先级（从高到低）

```
.env.local                 # 最高优先级 - 所有环境
.env.development.local     # 开发环境本地
.env.production.local      # 生产环境本地
.env.development           # 开发环境公共
.env.production            # 生产环境公共
.env                       # 全局公共 - 最低优先级
```

## ? 禁止上传的私密文件

以下文件已添加到 `.gitignore`，**永远不会提交到代码库**：

| 文件名 | 说明 | 使用场景 |
|--------|------|----------|
| `.env.local` | 本地全局私密 | 覆盖所有环境的本地配置 |
| `.env.development.local` | 开发环境本地私密 | 个人开发机的特殊配置 |
| `.env.production.local` | 生产环境本地私密 | 正式构建时的私密配置 |

## ? 可以上传的公共配置文件

| 文件名 | 说明 |
|--------|------|
| `.env` | 全局公共配置 |
| `.env.development` | 开发环境默认配置 |
| `.env.production` | 生产环境默认配置 |

## ? 环境加载方式

```bash
# 开发环境 - 加载顺序
# .env.local → .env.development.local → .env.development → .env
npm run dev

# 生产构建 - 加载顺序
# .env.local → .env.production.local → .env.production → .env
npm run build
```

## ? 使用建议

1. **团队统一配置** → 写在 `.env` / `.env.development` / `.env.production`
2. **个人特殊配置** → 写在 `*.local` 文件中
3. **密码/密钥等敏感数据** → 必须写在 `*.local` 文件中！
