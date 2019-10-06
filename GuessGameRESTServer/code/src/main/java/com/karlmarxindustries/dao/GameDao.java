package com.karlmarxindustries.dao;

import com.karlmarxindustries.dto.Game;

import java.util.List;

public interface GameDao {
    Game addGame(Game game);
    void updateGame(Game game);
    Game getGameById(int gameId);
    List<Game> getAllGames();
}
