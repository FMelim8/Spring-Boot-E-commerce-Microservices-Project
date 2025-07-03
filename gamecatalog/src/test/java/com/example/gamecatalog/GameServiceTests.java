package com.example.gamecatalog;

import com.example.gamecatalog.kafka.KafkaPublisher;
import com.example.gamecatalog.models.Game;
import com.example.gamecatalog.payload.request.GameCreateRequest;
import com.example.gamecatalog.payload.request.PaginationRequest;
import com.example.gamecatalog.payload.response.GameResponse;
import com.example.gamecatalog.payload.response.PagingResult;
import com.example.gamecatalog.repository.GameRepository;
import com.example.gamecatalog.service.impl.GameServiceImpl;
import com.example.gamecatalog.utils.PaginationUtils;
import com.example.sharedlib.dto.GameEvent;
import com.example.sharedlib.dto.OrderEvent;
import com.example.sharedlib.dto.StockState;
import com.example.sharedlib.exception.ServiceCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private KafkaPublisher kafkaPublisher;

    @InjectMocks
    private GameServiceImpl gameService;

    @DisplayName("Add Game Test")
    @Test
    void testAddGame_success() {
        GameCreateRequest request = GameCreateRequest.builder()
                .title("Game A")
                .description("Desc")
                .genre("Action")
                .type("PC")
                .price(BigDecimal.valueOf(59.99))
                .stock(10)
                .releaseDate(LocalDate.now())
                .build();

        GameResponse response = gameService.addGame(request);

        assertEquals("Game A", response.getTitle());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @DisplayName("Get Game By Id Test (Found)")
    @Test
    void testGetGameById_found() {
        Game game = Game.builder()
                .id(1L)
                .title("Game A")
                .stock(5)
                .build();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        GameResponse response = gameService.getGameById(1L);

        assertEquals("Game A", response.getTitle());
    }

    @DisplayName("Get Game By Id Test (Not Found)")
    @Test
    void testGetGameById_notFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ServiceCustomException exception = assertThrows(ServiceCustomException.class, () -> {
            gameService.getGameById(1L);
        });

        assertEquals("Not Found", exception.getError());
    }

    @DisplayName("Game Stock Check Test (Available)")
    @Test
    void testGameStockCheck_available() {
        OrderEvent orderEvent = new OrderEvent(1L, 1L, 2);
        Game game = Game.builder().id(1L).stock(5).build();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        gameService.gameStockCheck(orderEvent);

        verify(gameRepository).save(any(Game.class));
        verify(kafkaPublisher).publish(eq("gameEventSupplier-out-0"), any(GameEvent.class));
    }

    @DisplayName("Game Stock Check Test (Not Found)")
    @Test
    void testGameStockCheck_gameNotFound() {
        OrderEvent orderEvent = new OrderEvent(1L, 999L, 2);

        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        gameService.gameStockCheck(orderEvent);

        verify(kafkaPublisher).publish(eq("gameEventSupplier-out-0"), argThat(event ->
                event.getStockState() == StockState.NOT_AVAILABLE
        ));
    }

    @DisplayName("Game Stock Check Test (Out Of Stock)")
    @Test
    void testGameStockCheck_gameOOS() {
        OrderEvent orderEvent = new OrderEvent(1L, 8L, 10);
        Game game = Game.builder().id(8L).stock(5).build();

        when(gameRepository.findById(8L)).thenReturn(Optional.of(game));

        gameService.gameStockCheck(orderEvent);

        verify(kafkaPublisher).publish(eq("gameEventSupplier-out-0"), argThat(event ->
                event.getStockState() == StockState.OUT_OF_STOCK
        ));
    }

    @Test
    void testGetAllGames_paginated() {
        PaginationRequest paginationRequest = new PaginationRequest(null, null, null ,null);

        Game game1 = Game.builder()
                .title("Game 1")
                .build();

        Game game2 = Game.builder()
                .title("Game 2")
                .build();

        List<Game> gameList = new ArrayList<>();
        gameList.add(game1);
        gameList.add(game2);

        final Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<Game> games = new PageImpl<>(gameList, pageable, 1);

        when(gameRepository.findAll(any(Pageable.class))).thenReturn(games);

        PagingResult<GameResponse> pagingResult = gameService.getAllGames(paginationRequest);

        assertThat(pagingResult.getTotalElements()).isEqualTo(2);
        assertThat(pagingResult.getTotalPages()).isEqualTo(1);
        assertThat(pagingResult.isEmpty()).isFalse();

    }
}
