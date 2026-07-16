package com.prince.flexisaf.service;

import com.prince.flexisaf.dto.ShipmentRequest;
import com.prince.flexisaf.dto.ShipmentResponse;
import com.prince.flexisaf.entity.Shipment;
import com.prince.flexisaf.enums.ShipmentStatus;
import com.prince.flexisaf.exception.ResourceNotFoundException;
import com.prince.flexisaf.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Transactional
    public ShipmentResponse createShipment(ShipmentRequest request) {
        Shipment shipment = Shipment.builder()
                .trackingNumber(request.getTrackingNumber())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(ShipmentStatus.PENDING)
                .weightKg(request.getWeightKg())
                .declaredValue(request.getDeclaredValue())
                .deliveryNotes(request.getDeliveryNotes())
                .requiresSignature(request.getRequiresSignature())
                .build();

        return toResponse(shipmentRepository.save(shipment));
    }

    @Transactional(readOnly = true)
    public List<ShipmentResponse> getAllShipments(ShipmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shipment> shipments;
        
        if (status != null) {
            shipments = shipmentRepository.findByStatus(status, pageable);
        } else {
            shipments = shipmentRepository.findAll(pageable);
        }
        
        return shipments.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public ShipmentResponse updateShipmentStatus(Long id, ShipmentStatus status) {
        Shipment shipment = findOrThrow(id);
        shipment.setStatus(status);
        return toResponse(shipmentRepository.save(shipment));
    }

    @Transactional
    public void deleteShipment(Long id) {
        shipmentRepository.delete(findOrThrow(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Shipment findOrThrow(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found with id: " + id));
    }

    private ShipmentResponse toResponse(Shipment s) {
        return ShipmentResponse.builder()
                .shipmentId(s.getShipmentId())
                .trackingNumber(s.getTrackingNumber())
                .origin(s.getOrigin())
                .destination(s.getDestination())
                .status(s.getStatus())
                .weightKg(s.getWeightKg())
                .declaredValue(s.getDeclaredValue())
                .deliveryNotes(s.getDeliveryNotes())
                .requiresSignature(s.getRequiresSignature())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}