package com.example.gameorder.repository;

import com.example.gameorder.models.OrderDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    Page<OrderDetails> findAllByUserId(Long userId, Pageable pageable);

}
