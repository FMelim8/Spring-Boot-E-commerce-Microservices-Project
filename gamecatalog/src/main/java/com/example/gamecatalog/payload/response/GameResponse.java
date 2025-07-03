package com.example.gamecatalog.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class GameResponse {
    private Long id;

    private String title;

    private String description;

    private String genre;

    private String type;

    private BigDecimal price;

    private int stock;

    @JsonIgnore
    private LocalDate releaseDate;
}
