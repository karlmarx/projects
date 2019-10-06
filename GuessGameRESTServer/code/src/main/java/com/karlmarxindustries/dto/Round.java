package com.karlmarxindustries.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Round {
    private String guess;
    private String result;
    private int roundId;
    private int gameId;
    private LocalDateTime timestamp;

    public Round() {
    }

    public Round(String guess, String result, int roundId, int gameId, LocalDateTime timestamp) {
        this.guess = guess;
        this.result = result;
        this.roundId = roundId;
        this.gameId = gameId;
        this.timestamp = timestamp;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return roundId == round.roundId &&
                gameId == round.gameId &&
                Objects.equals(guess, round.guess) &&
                Objects.equals(result, round.result) &&
                Objects.equals(timestamp, round.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guess, result, roundId, gameId, timestamp);
    }
}
