package com.karlmarxindustries.dao;


import com.karlmarxindustries.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoundDaoImpl implements RoundDao {
    @Autowired
    JdbcTemplate jdbc;


    @Override
    public Round addRound(Round round) {
        String sql = "INSERT INTO round (guess, result, game_id, timestamp) VALUES (?,?,?,?)";
        jdbc.update(sql, round.getGuess(), round.getResult(), round.getGameId(), round.getTimestamp().withNano(0));
        Integer newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        round.setRoundId(newId);
        return round;
    }

    @Override
    public List<Round> getAll() {
        String sql = "SELECT * FROM round";
        return jdbc.query(sql, new RoundMapper());
    }

    @Override
    public List<Round> getAllForGame(int gameId) {
        String sql = "SELECT * FROM round WHERE game_id = ?";
        List<Round> rounds = jdbc.query(sql, new RoundMapper(), gameId);
        return rounds;
    }

    public class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet row, int i) throws SQLException {
            Round result = new Round();
            result.setRoundId(row.getInt("round_id"));
            result.setGameId(row.getInt("game_id"));
            result.setGuess(row.getString("guess"));
            result.setResult(row.getString("result"));
            result.setTimestamp(row.getTimestamp("timestamp").toLocalDateTime());
            return result;
        }
    }
}
