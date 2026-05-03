---
alwaysApply: true
scene: backend
---

# Spring Boot 3.x + Java 17 后端开发规范

## 一、项目技术栈固定

| 技术 | 版本 | 说明 |
|------|------|------|
| **JDK** | 17 LTS | ? 禁止 20+/25+ 高版本 |
| **Spring Boot** | 3.2.x | 稳定版，禁止 SNAPSHOT |
| **ORM** | MyBatis-Plus 3.5.7 | ? 禁止混用原生 MyBatis |
| **鉴权** | JWT + Interceptor | ? 禁止 Spring Security |
| **数据库** | MySQL 8.0 | ? UTF8mb4 字符集 |
| **缓存** | Redis 7.x | |

---

## 二、分层架构规范

```
com.xzzj.medmanager/
├── config/              # 配置类（@Configuration）
│   ├── CorsConfig.java
│   ├── MybatisPlusConfig.java
│   └── WebMvcConfig.java
├── controller/          # Controller 层 (@RestController)
│   ├── AuthController.java
│   └── MedicineController.java
├── service/             # Service 层
│   ├── impl/            # 实现类
│   ├── IMailService.java
│   └── IOcrService.java
├── mapper/              # Mapper 接口（继承 BaseMapper）
├── entity/              # 实体类 (@TableName)
├── interceptor/         # 拦截器
├── common/
│   ├── result/          # 统一响应 R<T>
│   ├── exception/       # 全局异常
│   └── utils/           # 工具类 JwtUtils
└── MedManagerBackendApplication.java
```

---

## 三、命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| **实体类** | 大驼峰 | `Medicine`, `User` |
| **Controller** | 模块名 + Controller | `MedicineController` |
| **Service 接口** | I + 业务名 + Service | `IMedicineService` |
| **Service 实现** | 业务名 + ServiceImpl | `MedicineServiceImpl` |
| **Mapper** | 业务名 + Mapper | `MedicineMapper` |
| **配置类** | 功能 + Config | `CorsConfig`, `OcrConfig` |
| **拦截器** | 功能 + Interceptor | `JwtInterceptor` |
| **工具类** | 功能 + Utils | `JwtUtils`, `FileUtils` |

---

## 四、MyBatis-Plus 规范

### ? 正确写法（CRUD 零 XML）
```java
@Service
public class MedicineServiceImpl implements IMedicineService {
    
    @Autowired
    private MedicineMapper medicineMapper;
    
    // 条件查询
    public List<Medicine> listByName(String name) {
        LambdaQueryWrapper<Medicine> wrapper = Wrappers.lambdaQuery();
        wrapper.like(Medicine::getName, name)
               .orderByDesc(Medicine::getCreateTime);
        return medicineMapper.selectList(wrapper);
    }
}
```

### ? 禁止
- 禁止写简单 CRUD 的 XML
- 禁止手写单表查询 SQL
- 禁止 ResultType = Map

---

## 五、RESTful API 设计

| 操作 | 方法 | 路径 |
|------|------|------|
| 列表 | GET | `/api/medicines` |
| 详情 | GET | `/api/medicines/{id}` |
| 新增 | POST | `/api/medicines` |
| 更新 | PUT | `/api/medicines/{id}` |
| 删除 | DELETE | `/api/medicines/{id}` |

### ? 统一响应格式
```java
@GetMapping("/{id}")
public R<Medicine> getById(@PathVariable Long id) {
    Medicine medicine = medicineService.getById(id);
    return R.ok(medicine);
}
```

---

## 六、JDK 17 语法规范

### ? 允许使用
```java
// 文本块（Text Blocks）
String sql = """
    SELECT * FROM medicine 
    WHERE deleted = 0
    """;

// var 局部变量
var list = new ArrayList<String>();

// instanceof 模式匹配
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

### ? 禁止
- 禁止反射访问 `sun.misc.Unsafe`
- 禁止非法访问 JDK 内部 API
- 禁止使用预览特性（Preview）

---

## 七、配置文件规范

| Profile | Git | 职责 |
|---------|-----|------|
| `application.yaml` | ? | **公共基础配置**，所有环境共用 |
| `application-dev.yaml` | ? | **开发环境专属**（带默认值开箱即用） |
| `application-prod.yaml` | ? | **生产环境专属**（全环境变量） |
| `application-{profile}-local.yaml` | ? | **私密覆盖**（Git 忽略） |

---

## 八、代码质量红线

1. ? 禁止 `@SneakyThrows` 吞异常
2. ? 禁止 System.out 打印日志
3. ? 禁止硬编码密码/密钥
4. ? 所有外部配置通过 `@ConfigurationProperties` 绑定
5. ? Service 层参数校验使用 `@Validated`
