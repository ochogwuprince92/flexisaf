package com.prince.flexisaf.dto;

import com.prince.flexisaf.enums.ShipmentStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ShipmentResponse {
    private Long shipmentId;
    private String trackingNumber;
    private String origin;
    private String destination;
    private ShipmentStatus status;
    private BigDecimal weightKg;
    private BigDecimal declaredValue;
    private String deliveryNotes;
    private Boolean requiresSignature;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}