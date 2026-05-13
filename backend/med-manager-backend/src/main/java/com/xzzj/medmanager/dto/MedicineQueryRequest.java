package com.xzzj.medmanager.dto;

import lombok.Data;

@Data
public class MedicineQueryRequest {

    private String medicineName;

    private String manufacturer;

    private String dosageForm;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}