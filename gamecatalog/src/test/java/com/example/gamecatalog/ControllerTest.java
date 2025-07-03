package com.example.gamecatalog;

import com.example.gamecatalog.payload.request.GameCreateRequest;
import com.example.gamecatalog.payload.response.GameDeletedResponse;
import com.example.gamecatalog.payload.response.GameResponse;
import com.example.gamecatalog.service.GameService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GameService gameService;

    @Order(1)
    @Test
    void testGetGameById_shouldReturnGame() throws Exception {
        GameResponse mockGame = new GameResponse();
        mockGame.setId(1L);
        mockGame.setTitle("Test Game");

        when(gameService.getGameById(1L)).thenReturn(mockGame);

        mockMvc.perform(get("/api/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Order(2)
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddGame_shouldReturnCreated() throws Exception {
        GameCreateRequest request = GameCreateRequest.builder()
                .title("New Game")
                .price(BigDecimal.valueOf(59.99))
                .stock(10)
                .build();

        GameResponse response = GameResponse.builder()
                .id(1L)
                .title("New Game")
                .build();

        when(gameService.addGame(any())).thenReturn(response);

        mockMvc.perform(post("/api/games/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Game"));
    }

    @Order(3)
    @Test
    @WithMockUser(roles = "USER")
    void testAddGame_shouldReturnForbidden() throws Exception {
        GameCreateRequest request = GameCreateRequest.builder()
                .title("New Game")
                .price(BigDecimal.valueOf(59.99))
                .stock(10)
                .build();

        GameResponse response = GameResponse.builder()
                .id(1L)
                .title("New Game")
                .build();

        when(gameService.addGame(any())).thenReturn(response);

        mockMvc.perform(post("/api/games/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Order(4)
    @Test
    void testGetGamesByIds_shouldReturnGames() throws Exception {
        GameResponse game1 = new GameResponse();
        game1.setId(1L);
        game1.setTitle("Game 1");

        GameResponse game2 = new GameResponse();
        game2.setId(2L);
        game2.setTitle("Game 2");

        when(gameService.getGamesByIds(List.of(1L, 2L))).thenReturn(List.of(game1, game2));

        mockMvc.perform(get("/api/games/batch?gameIds=1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Game 1"))
                .andExpect(jsonPath("$[1].title").value("Game 2"));
    }

    @Order(5)
    @Test
    @WithMockUser(roles = "ADMIN")
    void testEndpointsWithAdminAccess_shouldReturnOk() throws Exception {
        AdminEndpointsTest(true);
    }

    @Order(6)
    @Test
    @WithMockUser(roles = "USER")
    void testEndpointsWithAdminAccess_shouldReturnForbidden() throws Exception {
        AdminEndpointsTest(false);
    }

    void AdminEndpointsTest(Boolean isAdmin) throws Exception {

        GameCreateRequest gameCreateRequest = GameCreateRequest.builder()
                .title("Some Game")
                .description("Some Description")
                .genre("Genre")
                .type("Type")
                .price(BigDecimal.valueOf(59.99))
                .stock(10)
                .releaseDate(LocalDate.now())
                .build();

        GameResponse gameResponse = new GameResponse();
        copyProperties(gameCreateRequest, gameResponse);
        gameResponse.setId(1L);

        when(gameService.addGame(any())).thenReturn(gameResponse);

        mockMvc.perform(post("/api/games/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameCreateRequest)))
                .andExpect(isAdmin ? status().isCreated() : status().isForbidden())
                .andExpect(isAdmin ? jsonPath("$.title").value("Some Game") : status().isForbidden());

        gameResponse.setStock(gameResponse.getStock() - 1);
        when(gameService.reduceQuantity(any(Long.class), any(Long.class))).thenReturn(gameResponse);

        mockMvc.perform(put("/api/games/reduceQuantity/1?quantity=1"))
                .andExpect(isAdmin ? status().isOk() : status().isForbidden())
                .andExpect(isAdmin ? jsonPath("$.title").value("Some Game") : status().isForbidden())
                .andExpect(isAdmin ? jsonPath("$.stock").value(9) : status().isForbidden());

        gameResponse.setTitle("Updated Game");
        when(gameService.updateGameByGameId(any(Long.class), any(GameCreateRequest.class))).thenReturn(gameResponse);

        mockMvc.perform(put("/api/games/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameCreateRequest)))
                .andExpect(isAdmin ? status().isOk() : status().isForbidden())
                .andExpect(isAdmin ? jsonPath("$.title").value("Updated Game") : status().isForbidden());

        GameDeletedResponse gameDeletedResponse = GameDeletedResponse.builder()
                .id(1L)
                .build();

        when(gameService.deleteGameById(any(Long.class))).thenReturn(gameDeletedResponse);

        mockMvc.perform(delete("/api/games/1"))
                .andExpect(isAdmin ? status().isOk() : status().isForbidden())
                .andExpect(isAdmin ? jsonPath("$.id").value(1) : status().isForbidden());
    }
}
