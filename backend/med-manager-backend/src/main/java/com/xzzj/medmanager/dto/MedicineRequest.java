package com.xzzj.medmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MedicineRequest {

    @NotBlank(message = "Drug name cannot be empty")
    @Size(max = 100, message = "Drug name length cannot exceed 100 characters")
    private String medicineName;

    @Size(max = 100, message = "Specification length cannot exceed 100 characters")
    private String specification;

    @Size(max = 100, message = "Manufacturer length cannot exceed 100 characters")
    private String manufacturer;

    @NotBlank(message = "Unit cannot be empty")
    @Size(max = 20, message = "Unit length cannot exceed 20 characters")
    private String unit;

    @Size(max = 50, message = "Dosage form length cannot exceed 50 characters")
    private String dosageForm;

    @Size(max = 200, message = "Remark length cannot exceed 200 characters")
    private String remark;
}