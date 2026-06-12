package com.prince.flexisaf.controller;

import com.prince.flexisaf.entity.Shipment;
import com.prince.flexisaf.enums.ShipmentStatus;
import com.prince.flexisaf.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    // Create shipment
    @PostMapping
    public Shipment createShipment(@RequestBody @Valid Shipment shipment) {
        return shipmentService.createShipment(shipment);
    }

    // Get all shipments
    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    // Get shipment by ID
    @GetMapping("/{id}")
    public Shipment getShipment(@PathVariable Long id) {
        return shipmentService.getShipmentById(id);
    }

    // Update shipment status
    @PatchMapping("/{id}/status")
    public Shipment updateStatus(@PathVariable Long id,
                                 @RequestParam ShipmentStatus status) {
        return shipmentService.updateShipmentStatus(id, status);
    }                                                          // ← was missing

    // Delete shipment
    @DeleteMapping("/{id}")                                    // ← was "/id"
    public String deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return "Shipment deleted successfully";
    }
}