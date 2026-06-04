package com.prince.flexisaf.service;

import com.prince.flexisaf.entity.Shipment;
import com.prince.flexisaf.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

//    Create shipment
    public Shipment createShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

//    List all shipment
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

//    Get shipment by Id
    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
    }

//    Update shipment
    public Shipment updateShipmentStatus(Long id, String status) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setStatus(status);

        return shipmentRepository.save(shipment);
    }

    public void  deleteShipment(Long id) {

        Shipment existingshipment = shipmentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException( "Shipment not found"));

        shipmentRepository.delete(existingshipment);

    }

//    Delete shipment

}