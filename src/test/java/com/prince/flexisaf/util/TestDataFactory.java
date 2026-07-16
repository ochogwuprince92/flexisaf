package com.prince.flexisaf.util;

import com.prince.flexisaf.dto.ShipmentRequest;
import com.prince.flexisaf.dto.UserRequest;
import com.prince.flexisaf.entity.Shipment;
import com.prince.flexisaf.entity.User;
import com.prince.flexisaf.enums.Role;
import com.prince.flexisaf.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestDataFactory {

    public static UserRequest createValidUserRequest() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(Role.USER);
        return request;
    }

    public static UserRequest createAdminUserRequest() {
        UserRequest request = new UserRequest();
        request.setEmail("admin@example.com");
        request.setPassword("Admin123");
        request.setFirstName("Admin");
        request.setLastName("User");
        request.setRole(Role.ADMIN);
        return request;
    }

    public static User createTestUser() {
        return User.builder()
                .userId(1L)
                .email("test@example.com")
                .password("$2a$10$encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User createTestAdmin() {
        return User.builder()
                .userId(2L)
                .email("admin@example.com")
                .password("$2a$10$encodedPassword")
                .firstName("Admin")
                .lastName("User")
                .role(Role.ADMIN)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ShipmentRequest createValidShipmentRequest() {
        ShipmentRequest request = new ShipmentRequest();
        request.setTrackingNumber("TRK123456789");
        request.setOrigin("New York, NY");
        request.setDestination("Los Angeles, CA");
        request.setWeightKg(new BigDecimal("15.5"));
        request.setDeclaredValue(new BigDecimal("500.00"));
        request.setDeliveryNotes("Handle with care");
        request.setRequiresSignature(true);
        return request;
    }

    public static Shipment createTestShipment() {
        return Shipment.builder()
                .shipmentId(1L)
                .trackingNumber("TRK123456789")
                .origin("New York, NY")
                .destination("Los Angeles, CA")
                .status(ShipmentStatus.PENDING)
                .weightKg(new BigDecimal("15.5"))
                .declaredValue(new BigDecimal("500.00"))
                .deliveryNotes("Handle with care")
                .requiresSignature(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Shipment createTestShipmentWithStatus(ShipmentStatus status) {
        return Shipment.builder()
                .shipmentId(1L)
                .trackingNumber("TRK123456789")
                .origin("New York, NY")
                .destination("Los Angeles, CA")
                .status(status)
                .weightKg(new BigDecimal("15.5"))
                .declaredValue(new BigDecimal("500.00"))
                .deliveryNotes("Handle with care")
                .requiresSignature(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
