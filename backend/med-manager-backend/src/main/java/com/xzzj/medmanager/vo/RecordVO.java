package com.xzzj.medmanager.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordVO {

    private Long id;

    private Long stockId;

    private Long medicineId;

    private String medicineName;

    private String specification;

    private String unit;

    private String operationType;

    private Integer quantity;

    private String operator;

    private String remark;

    private LocalDateTime createdAt;
}