package com.xzzj.medmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("medicine_stock")
public class MedicineStock {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("medicine_id")
    private Long medicineId;

    @TableField("batch_no")
    private String batchNo;

    @TableField("quantity")
    private Integer quantity;

    @TableField("expire_date")
    private LocalDate expireDate;

    @TableField("storage_location")
    private String storageLocation;

    @TableField("warning_days")
    private String warningDays;

    @TableField("status")
    private String Status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}