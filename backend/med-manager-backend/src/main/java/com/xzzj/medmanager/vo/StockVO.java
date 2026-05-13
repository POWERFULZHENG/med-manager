package com.xzzj.medmanager.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StockVO {

    private Long id;

    private Long medicineId;

    private String medicineName;

    private String specification;

    private String manufacturer;

    private String unit;

    private String batchNo;

    private Integer quantity;

    private LocalDate expireDate;

    private String storageLocation;

    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer expireDays;
}