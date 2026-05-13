---
alwaysApply: true
scene: global
---

# med-manager 项目全局架构规则

## 一、目录职责边界（绝对不可突破）

| 目录 | 职责 | 禁止放入 |
|------|------|---------|
| **frontend/med-manager-front/** | ? 纯前端源码 | ? 任何部署文件（Dockerfile/nginx.conf） |
| **backend/med-manager-backend/** | ? 纯后端源码 | ? 任何部署文件（Dockerfile） |
| **deploy/** | ? 所有部署配置 | ? 业务源码 |
| **scripts/** | ? SQL/自动化脚本 | ? 业务代码 |
| **docs/** | ? 所有文档 | ? 可执行文件 |
| **.github/** | ? CI/CD 工作流 | ? 非 workflow 配置 |

> ? 所有部署相关文件 **必须** 放在 `deploy/` 目录，源码目录零侵入！

---

## 二、版本一致性红线

### ? 统一版本矩阵

| 环境 | 版本 | 禁止修改 |
|------|------|---------|
| 后端 JDK | **17 LTS** | ? 18/19/20/21/22/23/24/25 |
| 前端 Node.js | **24 LTS** | ? 16/18/20 |
| MySQL | **8.0** | ? 5.7/5.x |
| Redis | **7.x** | |
| Docker Compose | v2 | ? v1 `docker-compose` (横杠) |

> ?? CI/CD / Docker / 本地开发 / 生产环境 必须完全一致！

---

## 三、环境配置分层

| 文件名 | Git | 优先级 | 说明 |
|--------|-----|--------|------|
| `application.yaml` | ? | 低 | 所有环境公共配置 |
| `application-{profile}.yaml` | ? | 中 | 环境专属配置（带合理默认） |
| `application-{profile}-local.yaml` | ? | 高 | 私密覆盖，团队成员本地差异 |

---

## 四、Git 提交规范

严格遵循 Angular 约定式提交：

```
<type>(<scope>): <subject>
```

| type | 说明 |
|------|------|
| feat | 新功能 |
| fix | 修复 bug |
| docs | 文档 |
| style | 格式 |
| refactor | 重构 |
| perf | 性能 |
| build | 构建/依赖 |
| ci | CI/CD |
| chore | 辅助工具 |

**scope 必须是本项目定义的模块：** `frontend` / `backend` / `deploy` / `ci` / `docs` / `arch`

---

## 五、敏感信息规则

| 信息类型 | 存放位置 |
|----------|---------|
| 数据库密码 | `.env` + `*-local.yaml` |
| JWT 密钥 | `.env` + `*-local.yaml` |
| OCR 密钥 | `.env` + `*-local.yaml` |
| 邮件密码 | `.env` + `*-local.yaml` |

? **所有敏感配置必须支持环境变量注入**
? **所有 *-local.yaml 必须在 .gitignore 中**
? **禁止硬编码任何密钥到 Git 提交的文件中**

---

## 六、新增文件目录审批

任何新增顶级目录必须符合架构设计，禁止随意创建：

| 新增场景 | 推荐位置 |
|----------|---------|
| 新前端页面 | `src/views/{module}/index.vue` |
| 新后端接口 | `controller/{Module}Controller.java` |
| 新 SQL 脚本 | `scripts/sql/V{版本号}_{描述}.sql` |
| 新部署配置 | `deploy/{category}/` |
| 新设计文档 | `docs/{category}/` |

---

## 七、架构红线（违反即重构）

1. ? 禁止部署配置分散到各个子模块
2. ? 禁止源码目录混入非源码文件
3. ? 禁止版本号不一致
4. ? 禁止敏感信息硬编码提交
5. ? 禁止无意义的 Git 提交信息
6. ? 禁止突破目录职责边界

> ? 架构先行，代码后行！本文件是所有开发的最高指导原则
