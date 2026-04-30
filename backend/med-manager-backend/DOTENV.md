# Spring Boot .env 环境变量使用指南

## ? 配置文件说明

| 文件 | 说明 | 是否提交Git |
|------|------|------------|
| `.env` | 公共环境变量 | ? 可提交 |
| `.env.local` | 本地私密环境变量 | ? 禁止提交 |

---

## ? 使用方式

### 方式1：IDEA 配置（推荐本地开发）

1. 打开 **Edit Configurations...**
2. 选择你的 Spring Boot 启动配置
3. 点击 **Modify options** → **Enable EnvFile**
4. 在 **EnvFile** 标签页添加：
   - `.env`
   - `.env.local`

### 方式2：Docker / Docker Compose

```yaml
# docker-compose.yml
services:
  app:
    image: med-manager-backend
    env_file:
      - .env
      - .env.local
```

### 方式3：Maven 插件（可选）

在 `pom.xml` 中添加：
```xml
<plugin>
  <groupId>io.github.zhou6ang</groupId>
  <artifactId>dotenv-maven-plugin</artifactId>
  <version>1.1</version>
  <executions>
    <execution>
      <goals><goal>set</goal></goals>
    </execution>
  </executions>
</plugin>
```

### 方式4：系统环境变量（生产推荐）

```bash
# Linux
export DB_PASSWORD=your_prod_password
export JWT_SECRET=your_prod_jwt_secret

# Windows PowerShell
$env:DB_PASSWORD="your_prod_password"
$env:JWT_SECRET="your_prod_jwt_secret"
```

---

## ? YAML 中引用环境变量

```yaml
spring:
  datasource:
    # ${变量名:默认值} 格式
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD}  # 无默认值，必须配置

jwt:
  secret: ${JWT_SECRET}      # 必须配置
  expiration: ${JWT_EXPIRATION:86400000}
```

> ? **最佳实践：**
> - 开发环境：使用 IDEA EnvFile 插件加载 .env 文件
> - 生产环境：使用容器平台或操作系统环境变量
