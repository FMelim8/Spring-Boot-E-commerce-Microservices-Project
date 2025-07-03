package com.example.gamecatalog.repository;

import com.example.gamecatalog.models.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {


    @Override
    Page<Game> findAll(Pageable pageable);

    List<Game> findAllById(Iterable<Long> gameIds);

    @Query(value = "SELECT * FROM games", nativeQuery = true)
    List<Game> findAllDeleted();
}
