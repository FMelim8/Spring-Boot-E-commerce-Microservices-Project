package com.example.gamecatalog;

import com.example.gamecatalog.models.Game;
import com.example.gamecatalog.repository.GameRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(OrderAnnotation.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameRepositoryTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:17"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private GameRepository gameRepository;

    @BeforeAll
    void setup() {
        gameRepository.save(Game.builder()
                .title("Default Game")
                .build());
    }

    @Order(1)
    @Test
    void shouldSaveAndFindGameById() {
        Game game = Game.builder()
                .title("Test Game")
                .price(BigDecimal.valueOf(59.99))
                .build();

        long gameId = gameRepository.save(game).getId();

        Game foundGame = gameRepository.findById(gameId).orElseThrow();
        assertThat(foundGame.getTitle()).isEqualTo(game.getTitle());
        assertThat(foundGame.getPrice()).isEqualTo(game.getPrice());
    }

    @Order(2)
    @Test
    void shouldExistById() {
        Boolean existsGame = gameRepository.existsById(1L);

        assertThat(existsGame).isTrue();
    }

    @Order(3)
    @Test
    void shouldNotExistById() {
        Boolean existsGame = gameRepository.existsById(2L);

        assertThat(existsGame).isFalse();
    }

    @Order(4)
    @Test
    void shouldUpdateGame() {
        Optional<Game> game = gameRepository.findById(1L);

        assertThat(game.isPresent()).isTrue();
        game.get().setTitle("Game Test");

        gameRepository.save(game.get());

        assertThat(gameRepository.findById(1L).isPresent()).isTrue();
        assertThat(gameRepository.findById(1L).get().getTitle()).isEqualTo("Game Test");

    }

    @Order(5)
    @Test
    void shouldDeleteGame() {
        Optional<Game> game = gameRepository.findById(1L);

        assertThat(game.isPresent()).isTrue();
        gameRepository.delete(game.get());

        assertThat(gameRepository.findById(1L).isPresent()).isFalse();
    }

}
