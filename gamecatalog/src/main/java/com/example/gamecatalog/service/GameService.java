package com.example.gamecatalog.service;


import com.example.gamecatalog.payload.request.GameCreateRequest;
import com.example.gamecatalog.payload.request.PaginationRequest;
import com.example.gamecatalog.payload.response.GameDeletedResponse;
import com.example.gamecatalog.payload.response.GameResponse;
import com.example.gamecatalog.payload.response.PagingResult;
import com.example.sharedlib.dto.OrderEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GameService {

    GameResponse addGame(GameCreateRequest gamecreateRequest);

    GameResponse getGameById(long gameId);

    PagingResult<GameResponse> getAllGames(PaginationRequest request);

    GameResponse reduceQuantity(long gameId, long quantity);

    GameResponse updateGameByGameId(long gameId, GameCreateRequest gameRequest);

    GameDeletedResponse deleteGameById(long gameId);

    @Transactional(readOnly = true)
    List<GameResponse> getGamesByIds(List<Long> gameIds);

    void gameStockCheck(OrderEvent orderEvent);
}
