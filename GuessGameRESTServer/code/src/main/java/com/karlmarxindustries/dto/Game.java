package com.karlmarxindustries.dto;

import java.util.List;
import java.util.Objects;

public class Game {
    private int gameId;
    private String answer;
    private boolean solved;
  //  private List<Round> roundList; –dont really need this


    public Game() {
    }

    public Game(int gameId, String answer, boolean solved) {
        this.gameId = gameId;
        this.answer = answer;
        this.solved = solved;
    }
    public Game(String answer, boolean solved) {
        this.gameId = gameId;
        this.answer = answer;
        this.solved = solved;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", answer='" + answer + '\'' +
                ", solved=" + solved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameId == game.gameId &&
                solved == game.solved &&
                Objects.equals(answer, game.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, answer, solved);
    }
}
