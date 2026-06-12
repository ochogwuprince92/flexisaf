package com.prince.flexisaf.entity;

import com.prince.flexisaf.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "shipments",
        indexes = {
                @Index(name = "idx_shipment_status",   columnList = "status"),
                @Index(name = "idx_shipment_tracking", columnList = "tracking_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "tracking_number", nullable = false,
            unique = true, length = 20)
    private String trackingNumber;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String origin;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShipmentStatus status;

    @Column(name = "weight_kg", precision = 10, scale = 2,
            nullable = false)
    private BigDecimal weightKg;

    @Column(name = "declared_value", precision = 12, scale = 2)
    private BigDecimal declaredValue;

    @Column(name = "delivery_notes", columnDefinition = "TEXT")
    private String deliveryNotes;

    @Column(name = "requires_signature", nullable = false)
    @Builder.Default
    private Boolean requiresSignature = false;

//    To be implemented later
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id", nullable = false)
//    private Customer customer;
//
//    @OneToMany(mappedBy = "shipment",
//            cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<Parcel> parcels = new ArrayList<>();
//
//    @OneToMany(mappedBy = "shipment",
//            cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<TrackingEvent> trackingEvents = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}