package com.xzzj.medmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("medicine")
public class Medicine {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("medicine_name")
    private String medicineName;

    @TableField("specification")
    private String specification;

    @TableField("manufacturer")
    private String manufacturer;

    @TableField("unit")
    private String unit;

    @TableField("dosage_form")
    private String dosageForm;

    @TableField("remark")
    private String remark;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}