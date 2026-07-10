package com.prince.flexisaf.controller;

import com.prince.flexisaf.dto.ShipmentRequest;
import com.prince.flexisaf.dto.ShipmentResponse;
import com.prince.flexisaf.enums.ShipmentStatus;
import com.prince.flexisaf.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipments", description = "Manage logistics shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    // ── Create ───────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new shipment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shipment created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<ShipmentResponse> createShipment(
            @Valid @RequestBody ShipmentRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shipmentService.createShipment(request));
    }

    // ── Read all ─────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get all shipments")
    @ApiResponse(responseCode = "200", description = "List returned")
    public ResponseEntity<List<ShipmentResponse>> getAllShipments(
            @RequestParam(required = false) ShipmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(shipmentService.getAllShipments(status, page, size));
    }

    // ── Read one ─────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get a shipment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shipment found"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    public ResponseEntity<ShipmentResponse> getShipment(
            @Parameter(description = "Shipment ID")
            @PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    // ── Update status ─────────────────────────────────────────────────────────

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update shipment status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    public ResponseEntity<ShipmentResponse> updateStatus(
            @Parameter(description = "Shipment ID")
            @PathVariable Long id,
            @Parameter(description = "New status value")
            @RequestParam ShipmentStatus status) {
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, status));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a shipment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Shipment deleted"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    public ResponseEntity<Void> deleteShipment(
            @Parameter(description = "Shipment ID")
            @PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }
}