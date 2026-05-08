package com.xzzj.medmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordQueryRequest {

    private String medicineName;

    private String operationType;

    private String operator;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}