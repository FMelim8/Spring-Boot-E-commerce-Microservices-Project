package com.example.gameorder.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_game_id", columnList = "game_id"),
        @Index(name = "idx_order_date", columnList = "order_date")
})
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentDetails payment;

    @Column(name = "GAME_ID")
    private long gameId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "ORDER_DATE")
    private ZonedDateTime orderDate;

    @Column(name = "QUANTITY")
    private long quantity;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;
}
