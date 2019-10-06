package com.karlmarxindustries.dao;

import com.karlmarxindustries.dto.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GameDaoImpl implements GameDao {
    @Autowired
    JdbcTemplate jdbc;
    @Override
    public Game addGame(Game game) {
        String sql = "INSERT INTO game (solved, answer) VALUES (?,?)";
        jdbc.update(sql, game.isSolved(), game.getAnswer());
        Integer newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        game.setGameId(newId);
        return game;
    }

    @Override
    public void updateGame(Game game) {
        String sql = "UPDATE game SET solved = ?, answer = ? WHERE game_id = ?";
        jdbc.update(sql, game.isSolved(), game.getAnswer(), game.getGameId());
    }

    @Override
    public Game getGameById(int gameId) {
        String sql = "SELECT * FROM game WHERE game_id = ?";
        return jdbc.queryForObject(sql, new GameMapper(), gameId);
    }

    @Override
    public List<Game> getAllGames() {
        String sql = "SELECT * FROM game";
        return jdbc.query(sql, new GameMapper());
    }
    public class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet row, int i) throws SQLException {
            Game result = new Game();
            result.setGameId(row.getInt("game_id"));
            result.setSolved(row.getBoolean("solved"));
            result.setAnswer(row.getString("answer"));
            return result;
        }
    }

}
