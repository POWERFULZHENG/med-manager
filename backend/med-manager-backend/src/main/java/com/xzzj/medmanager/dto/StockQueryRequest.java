package com.xzzj.medmanager.dto;

import lombok.Data;

@Data
public class StockQueryRequest {

    private String medicineName;

    private Boolean isExpiring;

    private Boolean isExpired;

    private String storageLocation;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}