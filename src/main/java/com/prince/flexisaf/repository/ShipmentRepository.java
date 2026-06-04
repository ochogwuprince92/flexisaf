package com.prince.flexisaf.repository;

import com.prince.flexisaf.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository <Shipment, Long> {
}
