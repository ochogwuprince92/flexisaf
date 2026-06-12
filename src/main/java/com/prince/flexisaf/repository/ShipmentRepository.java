package com.prince.flexisaf.repository;

import com.prince.flexisaf.entity.Shipment;
import com.prince.flexisaf.enums.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    // Derived — Spring writes the SQL from the method name
//    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    Page<Shipment> findByStatus(ShipmentStatus status, Pageable pageable);
//
//    List<Shipment> findByCustomer_CustomerId(Long customerId);
//
//    boolean existsByTrackingNumber(String trackingNumber);

//    // JPQL — class and field names, not table/column names
//    @Query("SELECT s FROM Shipment s JOIN FETCH s.customer " +
//            "WHERE s.status = :status")
//    List<Shipment> findByStatusWithCustomer(@Param("status") ShipmentStatus status);

    // Native SQL for PostgreSQL-specific syntax
    @Query(value = "SELECT * FROM shipments " +
            "WHERE created_at > NOW() - INTERVAL '7 days'",
            nativeQuery = true)
    List<Shipment> findRecentShipments();

    // Bulk update — @Modifying required for any write JPQL
    @Modifying
    @Query("UPDATE Shipment s SET s.status = :status " +
            "WHERE s.shipmentId = :id")
    int updateStatus(@Param("id") Long id,
                     @Param("status") ShipmentStatus status);
}