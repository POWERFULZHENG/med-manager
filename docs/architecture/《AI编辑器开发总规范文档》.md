下面是一份可直接复制给 AI 智能体使用的**《项目开发总规范文档》，已按你的“家庭常用药管理系统”场景定制，重点约束架构、目录、命名、接口、数据模型、开发流程**，用于保证 AI 全程开发时的统一性与规范性。

AI 项目开发总规范文档

家庭常用药管理系统

1. 文档目标

本规范用于约束 AI 智能体在开发“家庭常用药管理系统”时的行为、技术选型、目录结构、代码风格、接口设计、数据建模和交付流程，确保项目具备以下特征：

架构统一，不随意变更技术栈。

模块清晰，不交叉污染。

命名一致，不出现多套风格并存。

接口规范，前后端字段一致。

数据可追踪，库存变化可审计。

可持续扩展，后期便于增加新功能。

2. 项目基本约束

2.1 项目定位

本项目为家庭场景下的常用药管理系统，核心功能包括：

用户登录与鉴权

管理员与普通用户角色区分

药品基础信息管理

库存入库与出库

药品批次与失效时间管理

出入库记录追踪

OCR 拍照识别辅助录入

临期与过期预警

2.2 技术栈约束

AI 智能体只允许在以下技术栈内进行开发，不得擅自替换或新增未批准技术：

前端

Vue 3

Vite

TypeScript

Pinia

Vue Router

Element Plus

Axios

后端

Spring Boot

Spring Security

JWT

MyBatis-Plus 或 JPA（二选一，全项目统一）

MySQL

部署与辅助

Docker

Nginx

第三方 OCR 接口

2.3 禁止擅自引入的技术

未经明确授权，不得引入以下技术：

微服务拆分

Redis

消息队列

GraphQL

gRPC

WebSocket

NestJS

Serverless

多数据库方案

自研 OCR 模型训练

3. 架构总原则

3.1 架构形态

本项目采用前后端分离 + 模块化单体架构。

含义如下：

前端独立部署，负责页面、交互和状态管理。

后端独立部署，提供 REST API。

后端保持单体结构，不拆分为微服务。

功能按业务模块拆分，不按技术碎片化堆叠。

3.2 设计原则

AI 智能体必须遵循以下原则：

先规范后开发：先确认目录、接口、数据结构，再写代码。

先闭环后扩展：优先完成登录、药品、库存、记录的最小闭环。

先业务后优化：先实现功能，再做样式、性能和体验优化。

先统一再复用：公共能力必须沉淀到通用层。

先小步提交再合并：一次只完成一个明确任务。

4. 目录结构规范

4.1 前端目录规范

标准目录结构

frontend/

├── public/

├── src/

│   ├── assets/

│   ├── components/

│   ├── layouts/

│   ├── router/

│   │   ├── index.ts

│   │   └── modules/

│   ├── store/

│   │   └── modules/

│   ├── api/

│   │   └── modules/

│   ├── views/

│   │   ├── auth/

│   │   ├── dashboard/

│   │   ├── medicine/

│   │   ├── stock/

│   │   ├── record/

│   │   ├── ocr/

│   │   └── user/

│   ├── hooks/

│   ├── utils/

│   ├── permissions/

│   ├── styles/

│   ├── types/

│   ├── App.vue

│   └── main.ts

├── .env.development

├── .env.production

├── index.html

├── package.json

└── vite.config.ts

各目录职责

assets/：图片、图标、静态资源。

components/：全局通用组件。

layouts/：页面整体布局。

router/：路由配置与路由守卫。

store/：Pinia 状态管理。

api/：接口请求封装。

views/：页面级文件。

hooks/：组合式逻辑。

utils/：工具函数。

permissions/：权限控制逻辑。

styles/：全局样式。

types/：类型定义。

前端模块划分

页面必须按业务模块拆分，至少包括：

auth：登录

dashboard：首页

medicine：药品管理

stock：库存管理

record：出入库记录

ocr：OCR 识别

user：用户管理

4.2 后端目录规范

标准目录结构

backend/

├── src/main/java/com/xxx/medmanager/

│   ├── MedManagerApplication.java

│   ├── common/

│   ├── config/

│   ├── security/

│   ├── controller/

│   ├── service/

│   ├── service/impl/

│   ├── mapper/

│   ├── entity/

│   ├── dto/

│   ├── vo/

│   ├── enums/

│   ├── utils/

│   └── component/

├── src/main/resources/

│   ├── application.yml

│   ├── mapper/

│   └── static/

├── pom.xml

└── README.md

各目录职责

common/：统一返回体、异常、分页、常量。

config/：Spring 相关配置。

security/：JWT、过滤器、鉴权配置。

controller/：接口入口。

service/：业务接口。

service/impl/：业务实现。

mapper/：数据库访问层。

entity/：数据库实体类。

dto/：请求参数对象。

vo/：响应对象。

enums/：枚举定义。

utils/：工具类。

component/：外部能力封装，如 OCR 客户端、文件上传组件。

5. 包名与命名规范

5.1 包名规范

后端包名统一使用全小写英文，推荐形式：

com.xxx.medmanager

要求如下：

不使用中文。

不使用随意缩写。

不随意改包根路径。

模块命名要稳定、清晰。

5.2 类名规范

前端

页面组件：MedicineList.vue

公共组件：BaseTable.vue

布局组件：DefaultLayout.vue

API 文件：medicine.ts

后端

控制器：MedicineController

服务接口：MedicineService

服务实现：MedicineServiceImpl

数据访问：MedicineMapper

实体类：MedicineEntity

请求对象：MedicineCreateDTO

响应对象：MedicineVO

5.3 变量命名规范

变量和函数统一使用有意义的英文命名。

禁止拼音命名。

禁止无语义缩写。

布尔值用 is、has、can 等前缀更清晰。

时间字段统一使用 createdAt、updatedAt、expireDate 等风格。

6. 数据模型规范

6.1 核心数据表必须分离

库存体系不得只用一个总数量字段完成，必须拆分为以下逻辑对象：

药品基础信息

库存批次信息

出入库记录

OCR 识别记录

用户与角色信息

6.2 强制约束

药品基础信息与库存批次信息必须分开。

库存必须支持批次号与失效日期。

出库必须可追踪到具体批次。

所有库存变化必须写记录表。

OCR 识别结果必须经过人工确认后才能写入库存。

6.3 实体与 DTO/VO 的边界

Entity 只负责数据库映射。

DTO 只负责接收请求参数。

VO 只负责返回前端展示数据。

不允许直接把 Entity 暴露给前端。

7. 接口开发规范

7.1 接口风格

所有接口必须使用 REST 风格，路径统一、简洁、可理解。

示例：

POST /api/auth/login

GET /api/medicines

POST /api/stocks/in

POST /api/stocks/out

GET /api/records

POST /api/ocr/upload

7.2 返回格式统一

所有接口必须使用统一响应体，不得各写各的返回结构。

推荐格式：

{

  "code": 0,

  "msg": "success",

  "data": {}

}

7.3 状态码规范

0：成功

非 0：业务错误或系统错误

错误信息必须可读、明确，不可只返回“失败”。

7.4 分页规范

所有列表类接口必须支持分页和条件筛选。

统一返回格式：

total

pageNum

pageSize

records

7.5 参数规范

请求参数必须使用 DTO 封装。

复杂对象不能直接放在 controller 参数里。

请求字段名必须与前端保持一致。

日期格式统一约定，不可前后端混用格式。

8. 前端开发规范

8.1 页面职责边界

views/ 只放页面。

components/ 只放可复用组件。

api/ 只放请求封装。

store/ 只放共享状态。

utils/ 只放工具函数。

8.2 页面编写原则

每个页面只能承担一个明确职责：

列表页

详情页

编辑页

录入页

识别确认页

禁止一个页面塞入过多职责。

8.3 状态管理原则

仅以下内容允许放入全局状态：

token

用户信息

角色信息

菜单权限

全局字典数据

局部表单状态不得放入全局状态。

8.4 请求封装原则

所有请求统一走 request.ts

统一处理 token

统一处理错误提示

统一处理超时和拦截器

不允许页面直接写 fetch/axios 原始调用

9. 后端开发规范

9.1 分层职责

Controller

接收请求

参数校验

调用 service

返回结果

禁止：

写业务逻辑

写 SQL

写复杂转换逻辑

Service

处理业务流程

协调多个数据对象

执行业务规则

处理库存扣减、批次管理、OCR 转换等逻辑

Mapper

只负责数据库访问

不写业务判断

不写控制逻辑

Entity

只负责表结构映射

不承担接口返回职责

9.2 异常处理

必须统一使用全局异常处理机制：

参数错误

业务异常

权限异常

系统异常

禁止 controller 中直接 try-catch 后吞掉异常。

9.3 安全规范

所有接口必须经过鉴权控制。

登录态统一使用 JWT。

权限控制至少分为管理员与普通用户。

不允许明文保存密码，必须加密存储。

10. OCR 功能开发规范

10.1 OCR 定位

OCR 只作为辅助录入工具，不是主流程的唯一来源。

10.2 业务规则

图片上传后先识别。

识别结果必须展示给用户确认。

用户确认后才允许入库。

OCR 结果不得直接覆盖库存主数据。

10.3 OCR 代码规范

OCR 相关逻辑必须集中在：

前端：views/ocr/、api/modules/ocr.ts

后端：controller/OcrController

后端：service/OcrService

后端：component/OcrClientComponent

11. 开发顺序规范

AI 智能体必须按以下顺序推进，禁止跳步：

确认需求边界

确认目录结构

确认数据库表

确认接口清单

搭建前端框架

搭建后端框架

实现登录鉴权

实现药品管理

实现库存入库出库

实现记录查询

实现 OCR 识别

实现临期预警

补充测试与文档

最后再做 UI 优化

12. 变更控制规范

12.1 小步开发原则

每次开发只能聚焦一个模块或一个小功能，不得一次性重构多个层级。

例如：

只改登录模块

只改库存入库接口

只改药品列表页

只改 OCR 上传流程

12.2 变更前必须说明

每次生成代码前，AI 必须先输出：

修改范围

涉及文件

影响模块

是否需要同步修改接口或数据结构

12.3 变更后必须自检

每次完成后，AI 必须进行以下自检：

是否符合目录规范

是否符合命名规范

是否破坏了已有结构

是否引入重复逻辑

是否与接口契约一致

是否存在硬编码

是否需要补测

13. 测试与验收规范

13.1 最低验收标准

每个功能模块至少满足：

能运行

能联调

能保存数据

能正常返回结果

出错时有明确提示

13.2 建议测试范围

登录测试

权限测试

入库测试

出库测试

临期查询测试

OCR 识别测试

分页和筛选测试

异常处理测试

13.3 验收要求

AI 输出代码后，应同时给出：

使用说明

接口说明

关键文件说明

风险提示

后续建议

14. 禁止事项清单

AI 智能体在开发过程中禁止：

随意更换技术栈。

未经允许新增复杂中间件。

在 controller 中编写业务逻辑。

在页面中直接调用后端地址而不封装 API。

将 Entity 直接返回前端。

将库存做成单一总量字段而忽略批次。

让 OCR 结果直接写入库存而不校验。

生成命名混乱、风格不一致的代码。

在不同模块里重复实现相同功能。

一次性大改全项目而不做分步确认。

15. AI 总控执行指令模板

你可以直接把下面这段作为 AI 智能体的总控提示词：

你正在开发“家庭常用药管理系统”，必须严格遵守以下规则：

1. 只允许使用以下技术栈：Vue3 + Vite + TypeScript + Pinia + Vue Router + Element Plus；Spring Boot + Spring Security + JWT + MySQL。

2. 项目采用前后端分离、模块化单体架构，不允许擅自引入微服务、Redis、MQ、GraphQL 等未批准技术。

3. 前端必须按业务模块组织：auth、dashboard、medicine、stock、record、ocr、user。

4. 后端必须遵循 controller / service / mapper / entity / dto / vo / common / security 分层。

5. controller 只能接收参数和返回结果，不允许写业务逻辑。

6. 所有库存必须按批次管理，药品基础信息与库存批次必须分离。

7. OCR 只能做辅助录入，识别结果必须人工确认后才能写入库存。

8. 所有接口必须使用统一响应体 Result，统一错误处理，统一分页格式。

9. 每次改动只能聚焦一个功能模块，输出前必须列出修改文件、影响范围和自检结果。

10. 若发现已有架构或接口规范冲突，优先维护原规范，不得自行改动整体架构。

在开始编写代码前，请先输出：

- 本次任务目标

- 涉及文件

- 接口变更

- 数据结构变更

- 自检清单

然后再开始生成代码。

16. 推荐使用方式

这份规范文档适合作为 AI 智能体的“最高约束层”。实际执行时建议再配套三份子文档：

目录结构文档

接口契约文档

数据库设计文档

下面把这三份文档按可直接交给 AI 智能体执行的形式整理出来。内容基于你这个家庭常用药管理系统的核心需求：登录、管理员/普通用户、药品入库出库、手动录入或拍照 OCR、库存管理、药品名称/数量/失效时间记录。

17. 环境划分策略

为确保系统的稳定性和安全性，建议至少划分以下三套环境：

17.1 开发环境（Development）
- **目的**：用于开发人员日常开发和调试
- **配置**：本地或开发服务器，数据库使用开发库
- **特点**：配置简单，便于调试，数据可以随时重置
- **访问地址**：http://localhost:3000（前端）/ http://localhost:8080（后端）

17.2 测试环境（Testing）
- **目的**：用于功能测试、回归测试和集成测试
- **配置**：独立的测试服务器，数据库使用测试库
- **特点**：配置接近生产环境，数据为测试数据，定期清理
- **访问地址**：http://test-med-manager.example.com

17.3 生产环境（Production）
- **目的**：正式上线运行的环境
- **配置**：高可用服务器，数据库使用生产库，定期备份
- **特点**：配置严格，安全措施完备，数据不可随意修改
- **访问地址**：http://med-manager.example.com

17.4 环境部署规范
- **代码管理**：使用 Git 分支管理，开发分支 -> 测试分支 -> 主分支
- **配置管理**：使用环境变量或配置文件区分不同环境的配置
- **部署流程**：开发完成 -> 提交代码 -> 自动化测试 -> 部署测试环境 -> 验收测试 -> 部署生产环境
- **监控与日志**：生产环境需配置监控和日志系统，及时发现和处理问题
- **环境隔离**：不同环境之间数据和配置严格隔离，确保测试不影响生产，生产数据安全

1. 目录结构文档

1.1 目录设计目标

便于 AI 按模块生成代码。

便于后期扩展，不容易乱。

前后端职责分离，边界清晰。

公共能力统一沉淀，减少重复代码。

1.2 前端目录结构

frontend/

├── public/

├── src/

│   ├── assets/                      # 静态资源：图片、图标、字体

│   ├── components/                  # 全局通用组件

│   │   ├── BaseTable.vue

│   │   ├── BaseForm.vue

│   │   ├── ConfirmDialog.vue

│   │   └── UploadImage.vue

│   ├── layouts/                     # 主布局

│   │   ├── DefaultLayout.vue

│   │   ├── Sidebar.vue

│   │   ├── HeaderBar.vue

│   │   └── ContentWrap.vue

│   ├── router/                      # 路由

│   │   ├── index.ts

│   │   ├── guards.ts

│   │   └── modules/

│   │       ├── auth.ts

│   │       ├── dashboard.ts

│   │       ├── medicine.ts

│   │       ├── stock.ts

│   │       ├── record.ts

│   │       ├── ocr.ts

│   │       └── user.ts

│   ├── store/                       # Pinia 状态管理

│   │   ├── index.ts

│   │   └── modules/

│   │       ├── auth.ts

│   │       ├── user.ts

│   │       └── app.ts

│   ├── api/                         # 接口封装

│   │   ├── request.ts

│   │   └── modules/

│   │       ├── auth.ts

│   │       ├── medicine.ts

│   │       ├── stock.ts

│   │       ├── record.ts

│   │       ├── ocr.ts

│   │       └── user.ts

│   ├── views/                       # 页面级组件

│   │   ├── auth/

│   │   │   └── Login.vue

│   │   ├── dashboard/

│   │   │   └── Index.vue

│   │   ├── medicine/

│   │   │   ├── Index.vue

│   │   │   ├── MedicineForm.vue

│   │   │   └── MedicineDetail.vue

│   │   ├── stock/

│   │   │   ├── Index.vue

│   │   │   ├── StockIn.vue

│   │   │   ├── StockOut.vue

│   │   │   └── ExpiringList.vue

│   │   ├── record/

│   │   │   └── Index.vue

│   │   ├── ocr/

│   │   │   ├── Index.vue

│   │   │   └── OcrResult.vue

│   │   └── user/

│   │       └── Index.vue

│   ├── hooks/                       # 组合式函数

│   │   ├── useAuth.ts

│   │   ├── useTable.ts

│   │   └── useUpload.ts

│   ├── utils/                       # 工具方法

│   │   ├── date.ts

│   │   ├── token.ts

│   │   ├── download.ts

│   │   └── validate.ts

│   ├── permissions/                 # 权限控制

│   │   ├── index.ts

│   │   └── auth.ts

│   ├── styles/                      # 全局样式

│   │   ├── global.scss

│   │   └── variables.scss

│   ├── types/                       # TypeScript 类型定义

│   │   ├── api.ts

│   │   ├── user.ts

│   │   ├── medicine.ts

│   │   └── stock.ts

│   ├── App.vue

│   └── main.ts

├── .env.development

├── .env.production

├── index.html

├── package.json

└── vite.config.ts

前端目录职责说明

views/：只放页面，不放通用组件。

components/：只放复用组件。

api/：只放接口请求封装。

store/：只放跨页面共享状态。

utils/：只放通用工具函数。

router/：只放路由和守卫。

layouts/：只放页面框架。

1.3 后端目录结构

backend/

├── src/

│   ├── main/

│   │   ├── java/com/xxx/medmanager/

│   │   │   ├── MedManagerApplication.java

│   │   │   ├── common/

│   │   │   │   ├── Result.java

│   │   │   │   ├── PageResult.java

│   │   │   │   ├── BusinessException.java

│   │   │   │   ├── GlobalExceptionHandler.java

│   │   │   │   ├── ResultCode.java

│   │   │   │   └── Constants.java

│   │   │   ├── config/

│   │   │   │   ├── CorsConfig.java

│   │   │   │   ├── WebMvcConfig.java

│   │   │   │   └── MybatisConfig.java

│   │   │   ├── security/

│   │   │   │   ├── JwtUtil.java

│   │   │   │   ├── JwtFilter.java

│   │   │   │   ├── SecurityConfig.java

│   │   │   │   └── CustomUserDetailsService.java

│   │   │   ├── controller/

│   │   │   │   ├── AuthController.java

│   │   │   │   ├── UserController.java

│   │   │   │   ├── MedicineController.java

│   │   │   │   ├── StockController.java

│   │   │   │   ├── RecordController.java

│   │   │   │   ├── OcrController.java

│   │   │   │   └── DashboardController.java

│   │   │   ├── service/

│   │   │   │   ├── AuthService.java

│   │   │   │   ├── UserService.java

│   │   │   │   ├── MedicineService.java

│   │   │   │   ├── StockService.java

│   │   │   │   ├── RecordService.java

│   │   │   │   ├── OcrService.java

│   │   │   │   └── DashboardService.java

│   │   │   ├── service/impl/

│   │   │   │   ├── AuthServiceImpl.java

│   │   │   │   ├── UserServiceImpl.java

│   │   │   │   ├── MedicineServiceImpl.java

│   │   │   │   ├── StockServiceImpl.java

│   │   │   │   ├── RecordServiceImpl.java

│   │   │   │   ├── OcrServiceImpl.java

│   │   │   │   └── DashboardServiceImpl.java

│   │   │   ├── mapper/

│   │   │   │   ├── UserMapper.java

│   │   │   │   ├── RoleMapper.java

│   │   │   │   ├── MedicineMapper.java

│   │   │   │   ├── StockMapper.java

│   │   │   │   ├── RecordMapper.java

│   │   │   │   └── OcrRecordMapper.java

│   │   │   ├── entity/

│   │   │   │   ├── UserEntity.java

│   │   │   │   ├── RoleEntity.java

│   │   │   │   ├── MedicineEntity.java

│   │   │   │   ├── StockEntity.java

│   │   │   │   ├── RecordEntity.java

│   │   │   │   └── OcrRecordEntity.java

│   │   │   ├── dto/

│   │   │   │   ├── LoginDTO.java

│   │   │   │   ├── UserCreateDTO.java

│   │   │   │   ├── MedicineCreateDTO.java

│   │   │   │   ├── StockInDTO.java

│   │   │   │   ├── StockOutDTO.java

│   │   │   │   └── OcrConfirmDTO.java

│   │   │   ├── vo/

│   │   │   │   ├── LoginVO.java

│   │   │   │   ├── UserVO.java

│   │   │   │   ├── MedicineVO.java

│   │   │   │   ├── StockVO.java

│   │   │   │   ├── RecordVO.java

│   │   │   │   └── DashboardVO.java

│   │   │   ├── enums/

│   │   │   │   ├── RoleCodeEnum.java

│   │   │   │   ├── RecordTypeEnum.java

│   │   │   │   └── StockStatusEnum.java

│   │   │   ├── utils/

│   │   │   │   ├── DateUtil.java

│   │   │   │   ├── BeanCopyUtil.java

│   │   │   │   └── OcrUtil.java

│   │   │   └── component/

│   │   │       ├── FileUploadComponent.java

│   │   │       └── OcrClientComponent.java

│   │   └── resources/

│   │       ├── application.yml

│   │       ├── mapper/

│   │       │   ├── UserMapper.xml

│   │       │   ├── MedicineMapper.xml

│   │       │   ├── StockMapper.xml

│   │       │   ├── RecordMapper.xml

│   │       │   └── OcrRecordMapper.xml

│   │       └── static/

│   └── test/

├── pom.xml

└── README.md

后端目录职责说明

controller/：只接收参数、调用 service、返回结果。

service/：只写业务逻辑。

mapper/：只负责数据库访问。

entity/：只做表映射。

dto/：只接收请求数据。

vo/：只负责返回前端展示数据。

common/：统一返回、异常、分页、常量。

security/：统一鉴权与权限控制。

2. 接口契约文档

2.1 接口设计原则

统一 REST 风格。

统一返回格式。

统一分页格式。

统一错误码。

统一鉴权方式。

2.2 通用约定

2.2.1 请求头

Authorization: Bearer <token>

Content-Type: application/json

2.2.2 统一返回格式

{

  "code": 0,

  "msg": "success",

  "data": {}

}

2.2.3 分页返回格式

{

  "code": 0,

  "msg": "success",

  "data": {

    "pageNum": 1,

    "pageSize": 10,

    "total": 100,

    "records": []

  }

}

2.2.4 统一错误码

| code | 含义 |
| --- | --- |
| 0 | 成功 |
| 40001 | 参数错误 |
| 40002 | 登录失败 |
| 40003 | 未登录或 token 无效 |
| 40004 | 权限不足 |
| 50000 | 系统错误 |
| 50001 | 业务失败 |
| 50002 | 数据不存在 |

2.3 登录与认证接口

2.3.1 用户登录

POST /api/auth/login

请求：

{

  "username": "admin",

  "password": "123456"

}

响应：

{

  "code": 0,

  "msg": "success",

  "data": {

    "token": "jwt-token",

    "user": {

      "id": 1,

      "username": "admin",

      "nickname": "管理员",

      "roleCode": "ADMIN"

    }

  }

}

2.3.2 获取当前用户信息

GET /api/auth/me

响应：

{

  "code": 0,

  "msg": "success",

  "data": {

    "id": 1,

    "username": "admin",

    "nickname": "管理员",

    "roleCode": "ADMIN"

  }

}

2.3.3 退出登录

POST /api/auth/logout

2.4 用户管理接口

2.4.1 用户列表

GET /api/users

查询参数：

pageNum

pageSize

keyword

status

roleCode

2.4.2 新增用户

POST /api/users

请求：

{

  "username": "user01",

  "password": "123456",

  "nickname": "张三",

  "phone": "13800000000",

  "roleCode": "USER"

}

2.4.3 修改用户

PUT /api/users/{id}

2.4.4 删除用户

DELETE /api/users/{id}

2.4.5 启用/禁用用户

PUT /api/users/{id}/status

请求：

{

  "status": 1

}

2.5 药品管理接口

2.5.1 药品列表

GET /api/medicines

查询参数：

pageNum

pageSize

keyword

manufacturer

dosageForm

2.5.2 新增药品

POST /api/medicines

请求：

{

  "medicineName": "阿莫西林胶囊",

  "specification": "0.5g*24粒",

  "manufacturer": "某某药业",

  "unit": "盒",

  "dosageForm": "胶囊",

  "remark": "感冒常用药"

}

2.5.3 修改药品

PUT /api/medicines/{id}

2.5.4 删除药品

DELETE /api/medicines/{id}

2.5.5 药品详情

GET /api/medicines/{id}

2.6 库存管理接口

2.6.1 库存列表

GET /api/stocks

查询参数：

pageNum

pageSize

keyword

expireStatus

storageLocation

expireStatus 取值：

NORMAL

EXPIRING

EXPIRED

2.6.2 入库

POST /api/stocks/in

请求：

{

  "medicineId": 1,

  "batchNo": "B20260401",

  "quantity": 10,

  "expireDate": "2026-12-31",

  "storageLocation": "药箱A",

  "warningDays": 30,

  "remark": "购药入库"

}

2.6.3 出库

POST /api/stocks/out

请求：

{

  "stockId": 1001,

  "quantity": 2,

  "remark": "服用两粒"

}

2.6.4 库存调整

PUT /api/stocks/{id}/adjust

请求：

{

  "quantity": 5,

  "remark": "盘点调整"

}

2.6.5 临期药品列表

GET /api/stocks/expiring

2.6.6 过期药品列表

GET /api/stocks/expired

2.7 出入库记录接口

2.7.1 记录列表

GET /api/records

查询参数：

pageNum

pageSize

keyword

recordType

operatorId

startTime

endTime

recordType 取值：

IN

OUT

ADJUST

2.8 OCR 识别接口

2.8.1 上传并识别

POST /api/ocr/upload

请求方式：multipart/form-data

返回：

{

  "code": 0,

  "msg": "success",

  "data": {

    "imageUrl": "/upload/xxx.jpg",

    "medicineName": "阿莫西林胶囊",

    "expireDate": "2026-12-31",

    "quantity": 1,

    "rawText": "..."

  }

}

2.8.2 确认识别结果并入库

POST /api/ocr/confirm

请求：

{

  "imageUrl": "/upload/xxx.jpg",

  "medicineName": "阿莫西林胶囊",

  "specification": "0.5g*24粒",

  "quantity": 1,

  "expireDate": "2026-12-31",

  "storageLocation": "药箱A"

}

2.9 首页统计接口

2.9.1 首页汇总数据

GET /api/dashboard/summary

返回建议包含：

药品总数

库存总数量

临期数量

过期数量

今日入库次数

今日出库次数

3. 数据库设计文档

3.1 设计原则

药品基础信息与库存批次分离。

库存必须按批次和失效时间管理。

每次库存变更必须留痕。

用户、角色、药品、库存、记录解耦。

方便统计、预警、追踪。

3.2 核心表结构

3.2.1 角色表 sys_role

用于管理员和普通用户。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | bigint | PK | 主键 |
| role_code | varchar(20) | NOT NULL, UNIQUE | 角色编码 |
| role_name | varchar(50) | NOT NULL | 角色名称 |
| remark | varchar(200) | NULL | 备注 |
| created_at | datetime | NOT NULL | 创建时间 |
| updated_at | datetime | NOT NULL | 更新时间 |

角色示例：

ADMIN

USER

3.2.2 用户表 sys_user

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | bigint | PK | 主键 |
| username | varchar(50) | NOT NULL, UNIQUE | 用户名 |
| password | varchar(100) | NOT NULL | 密码哈希 |
| nickname | varchar(50) | NOT NULL | 昵称 |
| phone | varchar(20) | NULL | 手机号 |
| role_id | bigint | NOT NULL, FK | 角色ID |
| status | tinyint | NOT NULL | 1启用，0禁用 |
| created_at | datetime | NOT NULL | 创建时间 |
| updated_at | datetime | NOT NULL | 更新时间 |

索引建议：

idx_user_username

idx_user_role_id

3.2.3 药品基础表 medicine

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | bigint | PK | 主键 |
| medicine_name | varchar(100) | NOT NULL | 药品名称 |
| specification | varchar(100) | NULL | 规格 |
| manufacturer | varchar(100) | NULL | 生产厂家 |
| unit | varchar(20) | NOT NULL | 单位 |
| dosage_form | varchar(50) | NULL | 剂型 |
| remark | varchar(200) | NULL | 备注 |
| created_at | datetime | NOT NULL | 创建时间 |
| updated_at | datetime | NOT NULL | 更新时间 |

索引建议：

idx_medicine_name

idx_medicine_manufacturer

3.2.4 库存批次表 medicine_stock

这是系统最关键的表。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | bigint | PK | 主键 |
| medicine_id | bigint | NOT NULL, FK | 药品ID |
| batch_no | varchar(50) | NOT NULL | 批次号 |
| quantity | int | NOT NULL | 当前库存数量 |
| expire_date | date | NOT NULL | 失效日期 |
| storage_location | varchar(100) | NULL | 存放位置 |
| warning_days | int | NOT NULL | 预警天数 |
| status | varchar(20) | NOT NULL | NORMAL / EXPIRING / EXPIRED |
| created_at | datetime | NOT NULL | 创建时间 |
| updated_at | datetime | NOT NULL | 更新时间 |

索引建议：

idx_stock_medicine_id

idx_stock_expire_date

idx_stock_batch_no

idx_stock_status

说明：

同一种药可以有多个批次。

每个批次有独立的失效日期。

出库时应优先扣减临期或先入库批次。

3.2.5 出入库记录表 inventory_record

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | bigint | PK | 主键 |
| stock_id | bigint | NOT NULL, FK | 库存批次ID |
| medicine_id | bigint | NOT NULL, FK | 药品ID |
| operator_id | bigint | NOT NULL, FK | 操作人ID |
| record_type | varchar(20) | NOT NULL | IN / OUT / ADJUST |
| quantity_change | int | NOT NULL | 变更数量 |
| before_quantity | int | NOT NULL | 变更前数量 |
| after_quantity | int | NOT NULL | 变更后数量 |
| remark | varchar(200) | NULL | 备注 |
| created_at | datetime | NOT NULL | 操作时间 |

索引建议：

idx_record_stock_id

idx_record_medicine_id

idx_record_operator_id

idx_record_record_type

idx_record_created_at

3.2.6 OCR 识别记录表 ocr_record

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | bigint | PK | 主键 |
| user_id | bigint | NOT NULL, FK | 操作人 |
| image_url | varchar(255) | NOT NULL | 图片地址 |
| ocr_result_json | text | NULL | OCR原始识别结果 |
| status | tinyint | NOT NULL | 1成功，0失败 |
| created_at | datetime | NOT NULL | 识别时间 |

索引建议：

idx_ocr_user_id

idx_ocr_created_at

3.3 表之间的关系

关系说明

sys_role (1) -> (N) sys_user

medicine (1) -> (N) medicine_stock

medicine_stock (1) -> (N) inventory_record

sys_user (1) -> (N) inventory_record

sys_user (1) -> (N) ocr_record

业务约束

一个用户只属于一个角色。

一个药品可以对应多个库存批次。

一个库存批次可以有多条操作记录。

OCR 记录只做审计，不直接影响库存主数据。

3.4 枚举值建议

3.4.1 角色编码 role_code

ADMIN

USER

3.4.2 记录类型 record_type

IN

OUT

ADJUST

3.4.3 库存状态 status

NORMAL

EXPIRING

EXPIRED

3.4.4 用户状态 status

1：启用

0：禁用

3.5 建表执行顺序建议

建议按以下顺序建表：

sys_role

sys_user

medicine

medicine_stock

inventory_record

ocr_record

这个顺序能保证外键依赖关系清晰。

3.6 核心设计提醒

不要把药品表和库存表混成一张表。

不要只保留总库存数量，必须保留批次和失效日期。

不要省略出入库记录表，否则后期无法追踪。

不要让 OCR 结果直接写库存，必须人工确认。
