# 部署配置目录

## ? 目录结构

```
deploy/
├── docker/                          # Docker 镜像配置
│   ├── backend.Dockerfile           # 后端服务镜像 (JDK 17)
│   ├── frontend.Dockerfile          # 前端服务镜像 (Node.js 24 + Nginx)
│   └── docker-compose.yml           # Docker Compose 编排
├── nginx/
│   └── nginx.conf                   # Nginx 反向代理配置
└── README.md                        # 本文档
```

## ? 快速开始

### 方式一：项目根目录启动（推荐）

```bash
# 在项目根目录执行
cd med-manager

# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

### 方式二：deploy 目录启动

```bash
cd deploy/docker

# 启动所有服务
docker-compose up -d
```

## ? 服务访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| 前端 | http://localhost | 家庭常用药管理系统 |
| 后端 API | http://localhost:8080/api | Spring Boot 服务 |
| MySQL | localhost:3306 | 数据库服务 |
| Redis | localhost:6379 | 缓存服务 |

## ? 环境变量

在项目根目录创建 `.env` 文件：

```env
# 数据库配置
DB_ROOT_PASSWORD=root123
DB_NAME=medmanager
DB_USERNAME=medmanager
DB_PASSWORD=med123

# JWT 配置
JWT_SECRET=your-secret-key-here

# 邮件配置
MAIL_HOST=smtp.qq.com
MAIL_USERNAME=your-email@qq.com
MAIL_PASSWORD=your-email-password
```

## ? 镜像构建说明

### 后端镜像 (backend.Dockerfile)
- **基础镜像**: eclipse-temurin:17-jdk-alpine
- **JAR 包路径**: `backend/med-manager-backend/target/med-manager-backend-1.0.0.jar`
- **构建前需要先执行 Maven 打包**

```bash
cd backend/med-manager-backend
mvn clean package -DskipTests
```

### 前端镜像 (frontend.Dockerfile)
- **构建阶段**: Node.js 24 alpine
- **运行阶段**: Nginx alpine
- **上下文路径**: 项目根目录
- **自动执行**: npm install + npm run build

## ? Nginx 配置说明

| 配置项 | 说明 |
|--------|------|
| 静态资源缓存 | JS/CSS/图片 缓存 30 天 |
| 页面缓存 | HTML 缓存 1 天 |
| Gzip 压缩 | 启用文本资源压缩 |
| 前端路由 | History 模式支持 |
| 反向代理 | /api/ → backend:8080/api/ |
| 文件上传 | /upload/ → backend:8080/upload/ |

## ? 健康检查

```bash
# MySQL 健康检查
docker exec med-manager-mysql mysqladmin ping -h localhost

# Redis 健康检查
docker exec med-manager-redis redis-cli ping

# 后端服务健康检查
curl http://localhost:8080/api/actuator/health
```

## ? 数据持久化

| 卷名 | 挂载路径 | 说明 |
|------|---------|------|
| mysql-data | /var/lib/mysql | MySQL 数据 |
| redis-data | /data | Redis 数据 |
| 宿主目录 | ./upload | 文件上传目录 |
| 宿主目录 | ./logs | 应用日志目录 |
