package com.karlmarxindustries.dao;

import com.karlmarxindustries.dto.Round;

import java.util.List;

public interface RoundDao {
    Round addRound(Round round);
    List<Round> getAll();
    List<Round> getAllForGame(int gameId);

}
