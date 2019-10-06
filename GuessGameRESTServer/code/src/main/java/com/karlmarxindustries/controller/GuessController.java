package com.karlmarxindustries.controller;

import com.karlmarxindustries.dto.Game;
import com.karlmarxindustries.dto.Round;
import com.karlmarxindustries.service.GuessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GuessController {
    @Autowired
    GuessService service;
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int begin() {
        int gameId = service.begin();
        return gameId;
    }
    @PostMapping("/guess")
    public Round guess(@RequestBody Round guessToCheck) {
        Round checkedRound = service.checkGuess(guessToCheck);
        return checkedRound;
    }
    @GetMapping("/game")
    public List<Game> getAllGamesCensored () {
        return service.getAllGamesRedacted();
    }
    @GetMapping("game/{gameId}")
    public Game getGameCensored (@PathVariable(value = "gameId") int gameId) {
        Game retrievedGame = service.getGameRedacted(gameId);
        return retrievedGame;
    }
    @GetMapping("rounds/{gameId}")
    public List<Round> sortRoundsForGame(@PathVariable(value = "gameId") int gameId){
        return service.sortRoundsForGame(gameId);
    }

    //extra features:
    @GetMapping("hint/{gameId}")
    public String getHint(@PathVariable(value = "gameId") int gameId){
        return service.getRandomHint(gameId);
    }
    @PostMapping("/guessforme")
    public Round guessForMe(@RequestBody Round guessToGenerate) {
        Round checkedRound = service.makeAndCheck(guessToGenerate);
        return checkedRound;
    }
    @PostMapping("/playforme")
    public List<Round> keepGuessingForMe(@RequestBody Round guessToGenerate) {
        List<Round> computerRounds = service.keepGuessingForMe(guessToGenerate);
        return computerRounds;
    }
}
