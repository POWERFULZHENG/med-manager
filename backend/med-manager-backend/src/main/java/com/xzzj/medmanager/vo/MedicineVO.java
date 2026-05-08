package com.xzzj.medmanager.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicineVO {

    private Long id;

    private String medicineName;

    private String specification;

    private String manufacturer;

    private String unit;

    private String dosageForm;

    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}