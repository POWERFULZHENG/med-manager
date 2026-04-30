-- ==============================================
-- 【严格按外键顺序插入数据】核心修复点
-- 1. 角色 → 2. 用户 → 3. 药品 → 4. 库存 → 5. 出入库 → 6. OCR
-- ==============================================

-- 1. 插入角色
INSERT INTO sys_role (role_code, role_name, remark)
VALUES ('ADMIN','管理员','系统管理员'),('USER','普通用户','普通操作用户');

-- 2. 插入用户 (密码：123456)
INSERT INTO sys_user (username, password, nickname, phone, role_id, status)
VALUES 
('admin','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','系统管理员','13800000000',1,1),
('user01','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','测试用户','13911111111',2,1);

-- 3. 插入药品
INSERT INTO medicine (medicine_name, specification, manufacturer, unit, dosage_form, remark)
VALUES 
('布洛芬缓释胶囊','0.3g*24粒','中美史克','盒','胶囊','解热镇痛'),
('感冒灵颗粒','10g*9袋','华润三九','盒','颗粒','感冒用药'),
('阿莫西林胶囊','0.5g*36粒','联邦制药','盒','胶囊','抗生素'),
('蒙脱石散','3g*10袋','益普生','盒','散剂','止泻');

-- 4. 插入库存
INSERT INTO medicine_stock (medicine_id, batch_no, quantity, expire_date, storage_location, warning_days, status)
VALUES 
(1,'B2025001',50,'2026-12-31','药柜A',30,'NORMAL'),
(1,'B2025002',10,'2025-08-15','药柜A',30,'EXPIRING'),
(2,'B2025003',30,'2027-01-20','药柜B',30,'NORMAL');

-- 5. 插入出入库记录 (此时用户、库存已存在，外键合法)
INSERT INTO inventory_record (stock_id, medicine_id, operator_id, record_type, quantity_change, before_quantity, after_quantity, remark)
VALUES 
(1,1,1,'IN',50,0,50,'采购入库'),
(2,1,1,'OUT',5,15,10,'日常领用');

-- 6. 插入OCR记录
INSERT INTO ocr_record (user_id, image_url, ocr_result_json, status)
VALUES 
(1,'/upload/1.jpg','{"药品":"布洛芬"}',1),
(2,'/upload/2.jpg','{"药品":"感冒灵"}',1);

-- 恢复外键校验
SET FOREIGN_KEY_CHECKS = 1;