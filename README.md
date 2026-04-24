# 家庭常用药管理系统

家庭常用药管理系统用于管理家庭场景下的常用药品，支持药品基础信息维护、库存批次管理、入库/出库记录、临期预警、OCR 拍照识别辅助录入，以及管理员与普通用户权限区分。

系统采用前后端分离架构：前端使用 Vue 3，后端使用 Spring Boot，数据库使用 MySQL。

---

## 1. 技术栈

### 前端
- Vue 3
- Vite
- TypeScript
- Pinia
- Vue Router
- Element Plus
- Axios

### 后端
- Spring Boot
- Spring Security
- JWT
- MyBatis-Plus 或 JPA（二选一，保持全项目统一）
- MySQL

### 部署与辅助
- Docker
- Nginx
- 第三方 OCR 接口

---

## 2. 功能简介

- 用户登录与鉴权
- 管理员与普通用户角色区分
- 药品基础信息管理
- 库存批次管理
- 药品入库与出库
- 出入库记录查询
- 临期与过期预警
- OCR 拍照识别辅助录入
- 首页统计概览

---

## 3. 目录结构

```text
med-manager/
├── frontend/                 # Vue3 前端项目
├── backend/                  # Spring Boot 后端项目
├── docs/                     # 架构、接口、数据库、规范文档
├── deploy/                   # 部署脚本、Nginx、Docker 配置
├── scripts/                  # 数据库脚本、初始化脚本、辅助脚本
├── .github/                  # GitHub Actions 配置
└── README.md
```

---

## 4. 快速开始

### 4.1 获取代码

```bash
git clone https://github.com/POWERFULZHENG/med-manager.git
cd med-manager
```

### 4.2 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端默认访问地址：`http://localhost:5173`

### 4.3 后端启动

```bash
cd backend
mvn clean spring-boot:run
```

后端默认接口地址：`http://localhost:8080`

---

## 5. 环境变量说明

### 5.1 前端环境变量

在 `frontend/.env.development` 或 `frontend/.env.production` 中配置：

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### 5.2 后端环境变量

建议通过环境变量或配置文件注入以下内容：

```env
SPRING_PROFILES_ACTIVE=dev
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=medmanager
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
OCR_APP_KEY=your_ocr_key
OCR_APP_SECRET=your_ocr_secret
```

### 5.3 说明

- 前端通过 `VITE_API_BASE_URL` 配置接口基础地址。
- 后端数据库连接信息、JWT 密钥、OCR 密钥等应通过环境变量或配置文件注入。
- 生产环境敏感信息不得直接写入仓库。

---

## 6. 数据库初始化

### 6.1 初始化顺序

1. 创建数据库。
2. 执行建表 SQL。
3. 导入基础角色数据。
4. 导入管理员账号。
5. 启动后端服务并验证数据库连接。

### 6.2 初始化脚本

- 建表脚本：`scripts/sql/V000_init_schema.sql`
- 初始数据脚本：`scripts/sql/V000_init_data.sql`

### 6.3 执行示例

```bash
mysql -u root -p medmanager < scripts/sql/V000_init_schema.sql
mysql -u root -p medmanager < scripts/sql/V000_init_data.sql
```

---

## 7. 接口文档入口

建议在项目中统一维护接口文档入口。

### 在线接口文档
- Swagger UI：`http://localhost:8080/swagger-ui/index.html`
- Knife4j：`http://localhost:8080/doc.html`

### 本地文档
- 接口契约文档：`docs/api/接口契约文档.md`

---

## 8. 部署说明

### 8.1 前端部署

前端先构建静态资源，再使用 Nginx 托管。

```bash
cd frontend
npm run build
```

### 8.2 后端部署

后端可通过 JAR 或 Docker 部署。

```bash
cd backend
mvn clean package -DskipTests
java -jar target/app.jar
```

### 8.3 推荐部署方式

- 前端：Nginx
- 后端：Docker 容器或 Linux 服务
- 数据库：MySQL 独立部署

### 8.4 部署前检查

- 配置文件已切换到目标环境
- 数据库迁移脚本已执行
- 后端健康检查正常
- 前端静态资源构建成功

---

## 9. 回滚说明

### 9.1 回滚范围

- 代码回滚
- 镜像回滚
- 配置回滚
- 数据库回滚（如支持）

### 9.2 回滚原则

1. 回滚前必须保留当前版本备份。
2. 回滚版本必须可追踪。
3. 数据库变更必须先评估是否可逆。
4. 回滚后必须执行健康检查。

### 9.3 回滚步骤示例

1. 恢复上一稳定版本代码。
2. 恢复上一稳定镜像。
3. 恢复配置文件。
4. 如涉及数据库迁移，执行对应回滚脚本。
5. 验证前后端核心功能是否正常。

---

## 10. CI/CD

项目建议采用持续集成与持续部署流程：

- `feature/*`：功能开发与自动检查
- `develop`：测试环境自动部署
- `main`：生产环境发布

建议在仓库中配置：

```text
.github/workflows/frontend-ci.yml
.github/workflows/backend-ci.yml
```

### 前端流水线应包含
- ESLint
- TypeScript 检查
- 单元测试
- 构建

### 后端流水线应包含
- 编译
- 单元测试
- 集成测试
- 打包

---

## 11. 开发规范

- 前端页面放在 `views/`
- 公共组件放在 `components/`
- 接口请求放在 `api/`
- 后端控制器只负责收发参数
- 业务逻辑统一放在 `service`
- 数据库变更必须走脚本
- OCR 识别结果必须人工确认后再入库
- 库存必须按批次管理，不能只保存总数量

---

## 12. 负责人联系方式

> 以下信息请由项目负责人填写，不建议将敏感个人信息公开到公共仓库。

- 项目负责人：`POWERFULZHENG`
- 邮箱：`<Email>`
- 电话：`<Phone>`
- 企业微信 / 钉钉：`<Contact>`
- 仓库维护人：`POWERFULZHENG`

---

## 13. 常见问题

### 13.1 前端无法访问后端接口怎么办？

检查 `VITE_API_BASE_URL` 是否正确，后端服务是否已启动，Nginx 或代理配置是否生效。

### 13.2 数据库连接失败怎么办？

检查数据库地址、端口、用户名、密码是否正确，数据库是否已创建，建表脚本是否已执行。

### 13.3 OCR 无法识别怎么办？

检查 OCR 服务密钥、网络连接、图片格式与大小限制，并确认接口返回是否正常。

---

## 14. AI 协作开发口令

```text
你已接入家庭常用药管理系统仓库，请严格遵守目录规范、接口契约、数据库设计、CI/CD 流程与企业级开发规范。
所有代码变更必须先确认影响范围，再进行分步修改，并同步更新必要文档与测试。
```

