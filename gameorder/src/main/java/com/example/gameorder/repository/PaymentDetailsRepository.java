package com.example.gameorder.repository;

import com.example.gameorder.models.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
    Optional<PaymentDetails> findByOrderId(Long orderId);

    @Query("SELECT p FROM PaymentDetails p WHERE p.orderId IN :orderIds")
    List<PaymentDetails> findAllByOrderIdIn(@Param("orderIds") List<Long> orderIds);

}
