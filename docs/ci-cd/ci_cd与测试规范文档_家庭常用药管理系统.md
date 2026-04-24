# CI/CD 与测试规范文档
## 家庭常用药管理系统

## 1. 文档目标

本规范用于约束“家庭常用药管理系统”在持续集成、持续测试、持续部署过程中的工程标准，确保项目在 AI 辅助开发模式下仍然保持：

1. 代码质量稳定。
2. 测试覆盖可控。
3. 发布流程标准化。
4. 环境切换清晰。
5. 出现问题时可回滚、可追踪。

---

## 2. CI/CD 总体原则

### 2.1 流程原则
所有代码变更必须经过以下基本流程：

**开发提交 → 静态检查 → 自动测试 → 构建产物 → 部署测试环境 → 冒烟验证 → 合并主分支 → 部署生产环境**

### 2.2 适用范围
本规范适用于：
- 前端 Vue3 项目
- 后端 Spring Boot 项目
- 数据库结构变更
- OCR 接口接入
- 版本发布与回滚

### 2.3 目标环境
建议至少划分三套环境：
- **开发环境**：本地联调
- **测试环境**：CI/CD 自动部署验证
- **生产环境**：正式对外服务

---

## 3. 分支管理规范

### 3.1 分支策略
建议采用以下分支模型：

- `main`：生产稳定分支
- `develop`：集成分支
- `feature/*`：功能开发分支
- `hotfix/*`：紧急修复分支
- `release/*`：发布候选分支（可选）

### 3.2 分支使用规则
1. 所有新功能必须先在 `feature/*` 分支开发。
2. `develop` 分支用于日常集成与自动部署测试环境。
3. `main` 分支只允许稳定版本合并。
4. 紧急修复从 `main` 拉出 `hotfix/*` 分支，修复后同步回 `develop`。

---

## 4. 质量门禁规范

### 4.1 前端质量门禁
前端代码合并前必须通过：
- ESLint 检查
- TypeScript 编译检查
- 单元测试
- 构建检查
- 必要时的页面冒烟测试

### 4.2 后端质量门禁
后端代码合并前必须通过：
- 代码编译
- 单元测试
- 集成测试
- 接口测试
- 数据一致性校验

### 4.3 数据库变更门禁
任何数据库结构变更必须满足：
- 先编写迁移脚本
- 脚本可重复执行或具备版本控制
- 测试环境验证通过后再推生产
- 不能直接手工改生产库结构

---

## 5. 测试规范

## 5.1 前端测试范围
前端建议覆盖以下内容：

### 5.1.1 单元测试
- 工具函数
- 表单校验逻辑
- 状态管理逻辑
- 权限判断逻辑

### 5.1.2 组件测试
- 登录表单组件
- 药品表格组件
- 库存弹窗组件
- 上传图片组件
- 临期预警展示组件

### 5.1.3 冒烟测试
关键页面至少验证：
- 登录页可正常登录
- 药品列表页可正常加载
- 入库页可正常提交
- 出库页可正常提交
- OCR 页可正常上传并返回结果

## 5.2 后端测试范围
后端建议覆盖以下内容：

### 5.2.1 单元测试
- 登录鉴权逻辑
- 库存入库逻辑
- 库存出库逻辑
- 临期判断逻辑
- OCR 结果转换逻辑

### 5.2.2 集成测试
- 登录接口
- 药品增删改查接口
- 库存入库出库接口
- 出入库记录查询接口
- 临期与过期查询接口

### 5.2.3 数据一致性测试
重点验证：
- 入库后库存是否增加
- 出库后库存是否减少
- 库存是否允许扣成负数
- 出入库记录是否同步生成
- 过期规则是否正确

---

## 6. 发布规范

### 6.1 发布前检查
发布前必须确认：
1. 所有测试已通过。
2. 构建产物已生成。
3. 数据库迁移脚本已准备好。
4. 配置文件区分环境。
5. 回滚方案已可执行。

### 6.2 发布后检查
发布后必须确认：
1. 服务健康检查通过。
2. 前端页面可访问。
3. 登录功能正常。
4. 核心业务接口正常。
5. 数据库连接正常。

### 6.3 回滚要求
出现严重问题时应支持：
- 代码版本回滚
- 镜像版本回滚
- 数据库迁移回滚（如支持）

---

## 7. 前端流水线模板

以下模板以 **GitHub Actions** 为例，可直接放到：

```text
frontend/.github/workflows/frontend-ci.yml
```

### 7.1 前端 CI/CD 流水线
```yaml
name: Frontend CI/CD

on:
  push:
    branches:
      - develop
      - main
      - 'feature/**'
  pull_request:
    branches:
      - develop
      - main

jobs:
  build-and-test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '20'
          cache: 'npm'
          cache-dependency-path: frontend/package-lock.json

      - name: Install dependencies
        working-directory: frontend
        run: npm ci

      - name: Lint
        working-directory: frontend
        run: npm run lint

      - name: Type check
        working-directory: frontend
        run: npm run type-check

      - name: Run unit tests
        working-directory: frontend
        run: npm run test:unit -- --runInBand

      - name: Build
        working-directory: frontend
        run: npm run build

      - name: Archive build artifacts
        uses: actions/upload-artifact@v4
        with:
          name: frontend-dist
          path: frontend/dist

  deploy-test:
    if: github.ref == 'refs/heads/develop'
    needs: build-and-test
    runs-on: ubuntu-latest

    steps:
      - name: Download build artifacts
        uses: actions/download-artifact@v4
        with:
          name: frontend-dist
          path: dist

      - name: Deploy to test server
        run: echo "Deploy frontend dist to test environment here"
```

### 7.2 前端需要准备的脚本
`frontend/package.json` 中建议至少包含：

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "lint": "eslint .",
    "type-check": "vue-tsc --noEmit",
    "test:unit": "vitest"
  }
}
```

---

## 8. 后端流水线模板

以下模板以 **GitHub Actions** 为例，可直接放到：

```text
backend/.github/workflows/backend-ci.yml
```

### 8.1 后端 CI/CD 流水线
```yaml
name: Backend CI/CD

on:
  push:
    branches:
      - develop
      - main
      - 'feature/**'
  pull_request:
    branches:
      - develop
      - main

jobs:
  build-and-test:
    runs-on: ubuntu-latest

    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: medmanager_test
        ports:
          - 3306:3306
        options: >-
          --health-cmd="mysqladmin ping -h 127.0.0.1 -proot"
          --health-interval=10s
          --health-timeout=5s
          --health-retries=5

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven

      - name: Build and test
        working-directory: backend
        run: mvn clean test

      - name: Package
        working-directory: backend
        run: mvn clean package -DskipTests

      - name: Archive jar
        uses: actions/upload-artifact@v4
        with:
          name: backend-jar
          path: backend/target/*.jar

  deploy-test:
    if: github.ref == 'refs/heads/develop'
    needs: build-and-test
    runs-on: ubuntu-latest

    steps:
      - name: Download jar
        uses: actions/download-artifact@v4
        with:
          name: backend-jar
          path: target

      - name: Deploy to test server
        run: echo "Deploy backend jar or docker image to test environment here"
```

### 8.2 后端建议的 Maven 配置能力
后端 `pom.xml` 建议至少支持：
- Spring Boot Starter Web
- Spring Security
- MyBatis-Plus 或 JPA
- MySQL Driver
- Validation
- Lombok
- JUnit 5
- Mockito
- Spring Boot Starter Test

---

## 9. Docker 与部署模板

### 9.1 前端 Dockerfile
可放在：
```text
frontend/Dockerfile
```

```dockerfile
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:1.27-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### 9.2 后端 Dockerfile
可放在：
```text
backend/Dockerfile
```

```dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### 9.3 docker-compose 示例
如果你想本地或测试环境一键启动，可使用：

```yaml
version: '3.9'
services:
  mysql:
    image: mysql:8.0
    container_name: medmanager-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: medmanager
      TZ: Asia/Shanghai
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./backend
    container_name: medmanager-backend
    depends_on:
      - mysql
    environment:
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "8080:8080"

  frontend:
    build: ./frontend
    container_name: medmanager-frontend
    depends_on:
      - backend
    ports:
      - "80:80"

volumes:
  mysql_data:
```

---

## 10. 数据库迁移规范

### 10.1 迁移原则
任何数据库结构变更都必须通过脚本管理，不允许直接改生产库。

### 10.2 迁移文件命名
建议采用版本号前缀：
- `V001__init_schema.sql`
- `V002__add_stock_status.sql`
- `V003__add_ocr_record.sql`

### 10.3 迁移要求
1. 脚本必须可追踪。
2. 脚本必须先在测试库执行。
3. 如有回滚脚本，也应一并保存。

---

## 11. AI 开发时的测试要求

AI 智能体每次生成代码时，必须同步说明：

1. 本次变更影响哪些文件。
2. 需要补哪些测试。
3. 需要执行哪些构建命令。
4. 是否涉及数据库变更。
5. 是否需要更新接口契约。

### 11.1 强制测试点
- 登录鉴权通过
- 库存入库通过
- 库存出库通过
- 临期判断通过
- OCR 确认入库通过
- 出入库记录一致性通过

---

## 12. 验收清单

每次版本发布前至少检查：
- 页面是否可打开
- 登录是否正常
- 核心接口是否返回成功
- 数据是否正确落库
- 记录是否完整
- 临期提示是否正确
- OCR 功能是否可用
- 构建产物是否生成
- 部署是否成功

---

## 13. 推荐执行顺序

建议按以下顺序推进：

1. 搭建前后端项目骨架
2. 配置代码检查与测试框架
3. 配置数据库与迁移脚本
4. 实现登录与鉴权
5. 实现药品管理
6. 实现库存入库/出库
7. 实现出入库记录
8. 实现 OCR 识别
9. 实现临期预警
10. 配置前后端 CI/CD
11. 配置测试环境部署
12. 配置生产发布与回滚

---

## 14. 可直接交给 AI 的执行要求

```text
你正在开发“家庭常用药管理系统”，请严格遵守以下 CI/CD 与测试规范：
1. 所有代码必须先通过静态检查和自动测试，再允许合并。
2. 前端必须提供 lint、type-check、unit test 和 build。
3. 后端必须提供单元测试、集成测试和接口测试。
4. 任何数据库结构修改必须提供迁移脚本。
5. 所有发布必须区分开发、测试、生产三个环境。
6. 任何库存相关修改必须验证库存一致性与记录一致性。
7. OCR 识别结果必须经过人工确认后才能入库。
8. 每次改动必须附带影响文件、测试范围和回滚说明。
```

---

这份文档可以作为项目的 CI/CD 与测试总规范，后续你在此基础上再补充具体的 GitLab CI、Jenkins 或 GitHub Actions 细节都很方便。

