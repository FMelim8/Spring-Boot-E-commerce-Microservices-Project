package com.example.gamecatalog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")

@SQLRestriction("deleted_at is null")
@SQLDelete(sql = "UPDATE games SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")

@Data @NoArgsConstructor
@AllArgsConstructor
@Builder

public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private String genre;

    private String type;

    private BigDecimal price;

    private int stock;

    private LocalDate releaseDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @JsonIgnore
    private LocalDateTime deletedAt;

    public Game(String title, String description, String genre, String type, BigDecimal price, int stock, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.type = type;
        this.price = price;
        this.stock = stock;
        this.releaseDate = releaseDate;
    }
}
