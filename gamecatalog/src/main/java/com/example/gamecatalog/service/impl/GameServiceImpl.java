package com.example.gamecatalog.service.impl;

import com.example.gamecatalog.kafka.KafkaPublisher;
import com.example.gamecatalog.models.Game;
import com.example.gamecatalog.payload.request.GameCreateRequest;
import com.example.gamecatalog.payload.request.PaginationRequest;
import com.example.gamecatalog.payload.response.GameDeletedResponse;
import com.example.gamecatalog.payload.response.GameResponse;
import com.example.gamecatalog.payload.response.PagingResult;
import com.example.gamecatalog.repository.GameRepository;
import com.example.gamecatalog.service.GameService;
import com.example.gamecatalog.utils.PaginationUtils;
import com.example.sharedlib.dto.GameEvent;
import com.example.sharedlib.dto.OrderEvent;
import com.example.sharedlib.dto.StockState;
import com.example.sharedlib.exception.ServiceCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@RequiredArgsConstructor
@Log4j2
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final KafkaPublisher kafkaPublisher;

    public void gameStockCheck(OrderEvent orderEvent) {

        Game game = gameRepository.findById(orderEvent.getGameId()).orElse(null);

        StockState stockState;

        if (game == null) {
            stockState = StockState.NOT_AVAILABLE;
        }
        else {
            stockState = (game.getStock() >= orderEvent.getQuantity()) ? StockState.AVAILABLE : StockState.OUT_OF_STOCK;
        }

        if (stockState == StockState.AVAILABLE) {
            game.setStock((int) (game.getStock() - orderEvent.getQuantity()));
            gameRepository.save(game);
        }

        kafkaPublisher.publish("gameEventSupplier-out-0", new GameEvent(
                orderEvent.getOrderId(),
                orderEvent.getGameId(),
                orderEvent.getQuantity(),
                stockState
        ));

    }

    @Override
    @Transactional()
    public GameResponse addGame(GameCreateRequest gameRequest){
        Game game = null;
        try {
            game = Game.builder()
                    .title(gameRequest.getTitle())
                    .description(gameRequest.getDescription())
                    .genre(gameRequest.getGenre())
                    .type(gameRequest.getType())
                    .price(gameRequest.getPrice())
                    .stock(gameRequest.getStock())
                    .releaseDate(gameRequest.getReleaseDate())
                    .build();
        } catch (Exception e) {
            throw new ServiceCustomException(
                    "Bad Request",
                    "Invalid request format",
                    HttpStatus.BAD_REQUEST.value(),
                    ""
            );
        }
        gameRepository.save(game);

        GameResponse gameResponse = new GameResponse();
        copyProperties(game, gameResponse);

        return gameResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public GameResponse getGameById(long gameId) {

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    return new ServiceCustomException(
                            "Not Found",
                            "Game not found with the id: " + gameId,
                            HttpStatus.NOT_FOUND.value(),
                            ""
                    );
                });

        GameResponse gameResponse = new GameResponse();
        copyProperties(game, gameResponse);

        return gameResponse;
    }

    @Override
    @Transactional()
    public GameResponse reduceQuantity(long gameId, long quantity) {

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ServiceCustomException(
                        "Not Found",
                        "Game not found with the id: " + gameId,
                        HttpStatus.NOT_FOUND.value(),
                        ""
                ));

        if (game.getStock() < quantity) {

            throw new ServiceCustomException(
                    "Insufficient Stock",
                    "Not enough stock",
                    HttpStatus.BAD_REQUEST.value(),
                    ""
            );
        }

        game.setStock((int) (game.getStock() - quantity));
        gameRepository.save(game);

        GameResponse gameResponse = new GameResponse();
        copyProperties(game, gameResponse);

        return gameResponse;
    }

    @Override
    @Transactional()
    public GameResponse updateGameByGameId(long gameId, GameCreateRequest gameRequest) {

        Game prevGame = gameRepository.findById(gameId)
                .orElseThrow(() -> new ServiceCustomException(
                        "Not Found",
                        "Game not found with the id: " + gameId,
                        HttpStatus.NOT_FOUND.value(),
                        ""
                ));

        copyNonNullProperties(gameRequest, prevGame);

        gameRepository.save(prevGame);

        GameResponse gameResponse = new GameResponse();
        copyProperties(prevGame, gameResponse);

        return gameResponse;
    }

    @Override
    @Transactional()
    public GameDeletedResponse deleteGameById(long gameId) {

        if (!gameRepository.existsById(gameId)) {
            throw new ServiceCustomException(
                    "Not Found",
                    "Game not found with the id: " + gameId,
                    HttpStatus.NOT_FOUND.value(),
                    ""
            );
        }
        gameRepository.deleteById(gameId);
        return new GameDeletedResponse(gameId);
    }

    @Override
    @Transactional(readOnly = true)
    public PagingResult<GameResponse> getAllGames(PaginationRequest request) {

        final Pageable pageable = PaginationUtils.getPageable(request);
        final Page<Game> games = gameRepository.findAll(pageable);
        final List<GameResponse> gameResponses = new ArrayList<>();
        for (Game game : games) {
            GameResponse gameResponse = new GameResponse();
            copyProperties(game, gameResponse);
            gameResponses.add(gameResponse);
        }
        return new PagingResult<>(
                gameResponses,
                games.getTotalPages(),
                games.getTotalElements(),
                games.getSize(),
                games.getNumber(),
                games.isEmpty()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<GameResponse> getGamesByIds(List<Long> gameIds) {
        List<Game> gameList = gameRepository.findAllById(gameIds);
        final List<GameResponse> gameResponseList = new ArrayList<>();
        for (Game game : gameList) {
            GameResponse gameResponse = new GameResponse();
            copyProperties(game, gameResponse);
            gameResponseList.add(gameResponse);
        }
        return gameResponseList;
    }

    private void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullProperties(source));
    }

    private String[] getNullProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> emptyPropertyNames = new HashSet<>();
        for(var propertyDescriptor : src.getPropertyDescriptors()) {
            Object value = src.getPropertyValue(propertyDescriptor.getName());
            if (value == null) {
                emptyPropertyNames.add(propertyDescriptor.getName());
            }
        }
        return emptyPropertyNames.toArray(new String[0]);
    }
}
