package com.xzzj.medmanager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustRequest {

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    private String remark;
}