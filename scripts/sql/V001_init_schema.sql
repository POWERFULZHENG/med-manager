SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- 1. 角色表
-- =========================
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    role_code    VARCHAR(20)  NOT NULL COMMENT '角色编码：ADMIN/USER',
    role_name    VARCHAR(50)  NOT NULL COMMENT '角色名称',
    remark       VARCHAR(200) DEFAULT NULL COMMENT '备注',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_sys_role_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- =========================
-- 2. 用户表
-- =========================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username     VARCHAR(50)  NOT NULL COMMENT '用户名',
    password     VARCHAR(100) NOT NULL COMMENT '密码哈希',
    nickname     VARCHAR(50)  NOT NULL COMMENT '昵称',
    phone        VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    role_id      BIGINT       NOT NULL COMMENT '角色ID',
    status       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_sys_user_username (username),
    KEY idx_sys_user_role_id (role_id),
    CONSTRAINT fk_sys_user_role_id
        FOREIGN KEY (role_id) REFERENCES sys_role(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =========================
-- 3. 药品基础表
-- =========================
DROP TABLE IF EXISTS medicine;
CREATE TABLE medicine (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    medicine_name   VARCHAR(100) NOT NULL COMMENT '药品名称',
    specification   VARCHAR(100) DEFAULT NULL COMMENT '规格',
    manufacturer    VARCHAR(100) DEFAULT NULL COMMENT '生产厂家',
    unit            VARCHAR(20)  NOT NULL COMMENT '单位',
    dosage_form     VARCHAR(50)  DEFAULT NULL COMMENT '剂型',
    remark          VARCHAR(200) DEFAULT NULL COMMENT '备注',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_medicine_name (medicine_name),
    KEY idx_medicine_manufacturer (manufacturer)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品基础表';

-- =========================
-- 4. 库存批次表
-- =========================
DROP TABLE IF EXISTS medicine_stock;
CREATE TABLE medicine_stock (
    id               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    medicine_id      BIGINT       NOT NULL COMMENT '药品ID',
    batch_no         VARCHAR(50)  NOT NULL COMMENT '批次号',
    quantity         INT          NOT NULL DEFAULT 0 COMMENT '当前库存数量',
    expire_date      DATE         NOT NULL COMMENT '失效日期',
    storage_location VARCHAR(100) DEFAULT NULL COMMENT '存放位置',
    warning_days     INT          NOT NULL DEFAULT 30 COMMENT '预警天数',
    status           VARCHAR(20)  NOT NULL DEFAULT 'NORMAL' COMMENT '库存状态：NORMAL/EXPIRING/EXPIRED',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_stock_medicine_id (medicine_id),
    KEY idx_stock_batch_no (batch_no),
    KEY idx_stock_expire_date (expire_date),
    KEY idx_stock_status (status),
    CONSTRAINT fk_stock_medicine_id
        FOREIGN KEY (medicine_id) REFERENCES medicine(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存批次表';

-- =========================
-- 5. 出入库记录表
-- =========================
DROP TABLE IF EXISTS inventory_record;
CREATE TABLE inventory_record (
    id               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    stock_id         BIGINT       NOT NULL COMMENT '库存批次ID',
    medicine_id      BIGINT       NOT NULL COMMENT '药品ID',
    operator_id      BIGINT       NOT NULL COMMENT '操作人ID',
    record_type      VARCHAR(20)  NOT NULL COMMENT '记录类型：IN/OUT/ADJUST',
    quantity_change  INT          NOT NULL COMMENT '变更数量',
    before_quantity  INT          NOT NULL COMMENT '变更前数量',
    after_quantity   INT          NOT NULL COMMENT '变更后数量',
    remark           VARCHAR(200) DEFAULT NULL COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    KEY idx_record_stock_id (stock_id),
    KEY idx_record_medicine_id (medicine_id),
    KEY idx_record_operator_id (operator_id),
    KEY idx_record_type (record_type),
    KEY idx_record_created_at (created_at),
    CONSTRAINT fk_record_stock_id
        FOREIGN KEY (stock_id) REFERENCES medicine_stock(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_record_medicine_id
        FOREIGN KEY (medicine_id) REFERENCES medicine(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_record_operator_id
        FOREIGN KEY (operator_id) REFERENCES sys_user(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出入库记录表';

-- =========================
-- 6. OCR识别记录表
-- =========================
DROP TABLE IF EXISTS ocr_record;
CREATE TABLE ocr_record (
    id               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id          BIGINT       NOT NULL COMMENT '操作人ID',
    image_url        VARCHAR(255) NOT NULL COMMENT '图片地址',
    ocr_result_json  LONGTEXT     DEFAULT NULL COMMENT 'OCR原始识别结果',
    status           TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1成功，0失败',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '识别时间',
    KEY idx_ocr_user_id (user_id),
    KEY idx_ocr_created_at (created_at),
    CONSTRAINT fk_ocr_user_id
        FOREIGN KEY (user_id) REFERENCES sys_user(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OCR识别记录表';

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO sys_role (role_code, role_name, remark)
VALUES
('ADMIN', '管理员', '系统管理员'),
('USER', '普通用户', '家庭成员普通用户');

-- 注意：password 字段这里必须放加密后的密码哈希，不要存明文
INSERT INTO sys_user (username, password, nickname, phone, role_id, status)
VALUES
('admin', '$2a$10$replace_with_bcrypt_hash', '管理员', '13800000000', 1, 1);