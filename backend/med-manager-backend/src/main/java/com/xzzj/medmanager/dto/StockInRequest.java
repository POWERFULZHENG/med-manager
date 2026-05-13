package com.xzzj.medmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StockInRequest {

    @NotNull(message = "Medicine ID cannot be null")
    private Long medicineId;

    @NotNull(message = "Batch number cannot be null")
    private String batchNo;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Expire date cannot be null")
    private LocalDate expireDate;

    private String storageLocation;

    private String remark;
}