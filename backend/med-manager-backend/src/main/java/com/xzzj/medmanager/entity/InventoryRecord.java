package com.xzzj.medmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inventory_record")
public class InventoryRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("stock_id")
    private Long stockId;

    @TableField("medicine_id")
    private Long medicineId;

    @TableField("operation_type")
    private String operationType;

    @TableField("quantity")
    private Integer quantity;

    @TableField("operator")
    private String operator;

    @TableField("remark")
    private String remark;

    @TableField("created_at")
    private LocalDateTime createdAt;
}