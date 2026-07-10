package com.prince.flexisaf.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShipmentRequest {

    @NotBlank(message = "Tracking number is required")
    @Size(max = 20, message = "Tracking number must not exceed 20 characters")
    private String trackingNumber;

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
    private BigDecimal weightKg;

    @DecimalMin(value = "0.00", message = "Declared value cannot be negative")
    private BigDecimal declaredValue;

    private String deliveryNotes;

    private Boolean requiresSignature = false;
}