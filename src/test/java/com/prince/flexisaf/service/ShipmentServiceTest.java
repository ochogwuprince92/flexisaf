package com.prince.flexisaf.service;

import com.prince.flexisaf.dto.ShipmentRequest;
import com.prince.flexisaf.dto.ShipmentResponse;
import com.prince.flexisaf.entity.Shipment;
import com.prince.flexisaf.enums.ShipmentStatus;
import com.prince.flexisaf.exception.ResourceNotFoundException;
import com.prince.flexisaf.repository.ShipmentRepository;
import com.prince.flexisaf.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    private Shipment testShipment;
    private ShipmentRequest shipmentRequest;

    @BeforeEach
    void setUp() {
        testShipment = TestDataFactory.createTestShipment();
        shipmentRequest = TestDataFactory.createValidShipmentRequest();
    }

    @Test
    void createShipment_ShouldReturnShipmentResponse_WhenValidRequest() {
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(testShipment);

        ShipmentResponse response = shipmentService.createShipment(shipmentRequest);

        assertThat(response).isNotNull();
        assertThat(response.getTrackingNumber()).isEqualTo(shipmentRequest.getTrackingNumber());
        assertThat(response.getOrigin()).isEqualTo(shipmentRequest.getOrigin());
        assertThat(response.getDestination()).isEqualTo(shipmentRequest.getDestination());
        assertThat(response.getStatus()).isEqualTo(ShipmentStatus.PENDING);

        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void getShipmentById_ShouldReturnShipmentResponse_WhenShipmentExists() {
        when(shipmentRepository.findById(testShipment.getShipmentId())).thenReturn(Optional.of(testShipment));

        ShipmentResponse response = shipmentService.getShipmentById(testShipment.getShipmentId());

        assertThat(response).isNotNull();
        assertThat(response.getShipmentId()).isEqualTo(testShipment.getShipmentId());
        assertThat(response.getTrackingNumber()).isEqualTo(testShipment.getTrackingNumber());

        verify(shipmentRepository).findById(testShipment.getShipmentId());
    }

    @Test
    void getShipmentById_ShouldThrowException_WhenShipmentNotFound() {
        when(shipmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shipmentService.getShipmentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Shipment not found with id: 999");

        verify(shipmentRepository).findById(999L);
    }

    @Test
    void getAllShipments_ShouldReturnListOfShipments_WhenNoFilter() {
        Page<Shipment> page = new PageImpl<>(List.of(testShipment));
        Pageable pageable = PageRequest.of(0, 10);
        
        when(shipmentRepository.findAll(pageable)).thenReturn(page);

        List<ShipmentResponse> responses = shipmentService.getAllShipments(null, 0, 10);

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTrackingNumber()).isEqualTo(testShipment.getTrackingNumber());

        verify(shipmentRepository).findAll(pageable);
    }

    @Test
    void getAllShipments_ShouldReturnFilteredShipments_WhenStatusProvided() {
        Page<Shipment> page = new PageImpl<>(List.of(testShipment));
        Pageable pageable = PageRequest.of(0, 10);
        
        when(shipmentRepository.findByStatus(ShipmentStatus.PENDING, pageable)).thenReturn(page);

        List<ShipmentResponse> responses = shipmentService.getAllShipments(ShipmentStatus.PENDING, 0, 10);

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getStatus()).isEqualTo(ShipmentStatus.PENDING);

        verify(shipmentRepository).findByStatus(ShipmentStatus.PENDING, pageable);
    }

    @Test
    void updateShipmentStatus_ShouldReturnUpdatedShipment_WhenShipmentExists() {
        ShipmentStatus newStatus = ShipmentStatus.IN_TRANSIT;
        testShipment.setStatus(newStatus);
        
        when(shipmentRepository.findById(testShipment.getShipmentId())).thenReturn(Optional.of(testShipment));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(testShipment);

        ShipmentResponse response = shipmentService.updateShipmentStatus(testShipment.getShipmentId(), newStatus);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(newStatus);

        verify(shipmentRepository).findById(testShipment.getShipmentId());
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void updateShipmentStatus_ShouldThrowException_WhenShipmentNotFound() {
        when(shipmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shipmentService.updateShipmentStatus(999L, ShipmentStatus.DELIVERED))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Shipment not found with id: 999");

        verify(shipmentRepository).findById(999L);
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }

    @Test
    void deleteShipment_ShouldDeleteShipment_WhenShipmentExists() {
        when(shipmentRepository.findById(testShipment.getShipmentId())).thenReturn(Optional.of(testShipment));
        doNothing().when(shipmentRepository).delete(any(Shipment.class));

        shipmentService.deleteShipment(testShipment.getShipmentId());

        verify(shipmentRepository).findById(testShipment.getShipmentId());
        verify(shipmentRepository).delete(testShipment);
    }

    @Test
    void deleteShipment_ShouldThrowException_WhenShipmentNotFound() {
        when(shipmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shipmentService.deleteShipment(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Shipment not found with id: 999");

        verify(shipmentRepository).findById(999L);
        verify(shipmentRepository, never()).delete(any(Shipment.class));
    }
}
