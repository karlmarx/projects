package com.karlmarxindustries.service;

import com.karlmarxindustries.TestApplicationConfiguration;
import com.karlmarxindustries.dao.GameDao;
import com.karlmarxindustries.dao.RoundDao;
import com.karlmarxindustries.dto.Game;
import com.karlmarxindustries.dto.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GuessServiceTest {
    @Autowired
    GameDao gDao;
    @Autowired
    RoundDao rDao;
    @Autowired
    GuessService service;
    @Autowired
    JdbcTemplate jdbc;

    @Before
    public void setUp() throws Exception {
        jdbc.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbc.execute("TRUNCATE TABLE `GuessTest`.`round`;");
        jdbc.execute("TRUNCATE TABLE `GuessTest`.`game`;");
        jdbc.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }

    @Test
    public void getAllForGame() {
    }

    @Test
    public void begin() {
        int shouldBeFirstGameId = service.begin();
        int shouldBeSecondGameId = service.begin();
        List<Game> createdGames = gDao.getAllGames();
        List<Integer> idsOfCreatedGames = new ArrayList<>();
        for (Game each: createdGames) {
            idsOfCreatedGames.add(each.getGameId());
        }
        assertEquals(1, shouldBeFirstGameId);
        assertEquals(2, shouldBeSecondGameId);
        assertEquals(2, createdGames.size());
        assertTrue(idsOfCreatedGames.contains(shouldBeFirstGameId));
        assertTrue(idsOfCreatedGames.contains(shouldBeSecondGameId));
    }

    @Test
    public void testGenerateUniqueRandomNumbers() {
        List<Integer> generatedInts1 = service.generateUniqueRandomNumbers(8, 5);
        List<Integer> generatedInts2 = service.generateUniqueRandomNumbers(8, 5);
        List<Integer> generatedInts3 = service.generateUniqueRandomNumbers(8, 5);
        List<Integer> generatedInts4 = service.generateUniqueRandomNumbers(8, 5);
        Set<Integer> setOfInts1 = GuessService.convertListToSet(generatedInts1);
        Set<Integer> setOfInts2 = GuessService.convertListToSet(generatedInts2);
        Set<Integer> setOfInts3 = GuessService.convertListToSet(generatedInts3);
        Set<Integer> setOfInts4 = GuessService.convertListToSet(generatedInts4);
        Collections.frequency(generatedInts1, generatedInts1.get(0));
        Collections.frequency(generatedInts1, generatedInts1.get(1));
        Collections.frequency(generatedInts1, generatedInts1.get(2));
        assertEquals(5, generatedInts1.size());
        assertFalse(generatedInts1.contains(9));
        assertFalse(generatedInts2.contains(9));
        assertFalse(generatedInts3.contains(9));
        assertFalse(generatedInts4.contains(9));
        assertEquals(generatedInts1.size(),setOfInts1.size());
        assertEquals(generatedInts2.size(),setOfInts2.size());
        assertEquals(generatedInts3.size(),setOfInts3.size());
        assertEquals(generatedInts4.size(),setOfInts4.size());
    }

    @Test
    public void checkGuess() {
        Game gameToAdd = new Game("3242", false);
        Game gameWithId = gDao.addGame(gameToAdd);
        Round roundToCheck = new Round();
        roundToCheck.setGuess("3114");
        roundToCheck.setGameId(gameWithId.getGameId());
        Round checkedRound = service.checkGuess(roundToCheck);
        Game gottenGameAfterCheck = gDao.getGameById(gameWithId.getGameId());
        Round winningRound = new Round();
        winningRound.setGuess("3242");
        winningRound.setGameId(gameWithId.getGameId());
        roundToCheck.setGameId(gameWithId.getGameId());
        Round checkedWinningRound = service.checkGuess(winningRound);
        Game gottenGameAfterWinningRound = gDao.getGameById(gameWithId.getGameId());

        assertEquals("e:4:p:0", checkedWinningRound.getResult());
        assertEquals(true,gottenGameAfterWinningRound.isSolved());
    }

    @Test
    public void testCalculateResult() {
        String correctAnswer = "1234";
        Round roundE1P2 = new Round();
        roundE1P2.setGuess("1329");
        Round roundE4P0 = new Round();
        roundE4P0.setGuess("1234");
        Round roundE0P0 = new Round();
        roundE0P0.setGuess("5678");
        String resultE1P2 = service.calculateResult(roundE1P2, correctAnswer);
        String resultE4P0 = service.calculateResult(roundE4P0, correctAnswer);
        String resultE0P0 = service.calculateResult(roundE0P0, correctAnswer);

        assertEquals("e:4:p:0", resultE4P0);
        assertEquals("e:1:p:2", resultE1P2);
        assertEquals("e:0:p:0", resultE0P0);
    }

    @Test
    public void getAllGamesRedacted() {
        Game game = new Game(1, "1432", false);
        gDao.addGame(game);
        Game game2 = new Game(2, "8622", false);
        gDao.addGame(game2);
        Game game3 = new Game(3, "0193", true);
        gDao.addGame(game3);
        List<Game> gamesUncut = gDao.getAllGames();
        List<Game> gamesShouldBeEdited = service.getAllGamesRedacted();
        List<String> listOfAnswersEdited = new ArrayList<>();
        for (Game each: gamesShouldBeEdited) {
            listOfAnswersEdited.add(each.getAnswer());
        }

        assertNotEquals(gamesUncut, gamesShouldBeEdited);
        assertTrue(listOfAnswersEdited.contains("0193"));
        assertFalse(listOfAnswersEdited.contains("8622"));
        assertFalse(listOfAnswersEdited.contains("1432"));
        assertTrue(listOfAnswersEdited.contains("----"));
    }

    @Test
    public void getGameRedacted() {
        Game game2 = new Game(2, "8622", false);
        gDao.addGame(game2);
        Game game3 = new Game(3, "0193", true);
        gDao.addGame(game3);
        Game shouldBe2Redacted = service.getGameRedacted(game2.getGameId());
        Game shouldBe3Redacted = service.getGameRedacted(game3.getGameId());

        assertEquals("----", shouldBe2Redacted.getAnswer());
        assertEquals("0193", shouldBe3Redacted.getAnswer());
    }

    @Test
    public void testSortRoundsForGame() {
        Game game = new Game(1, "0193", true);
        gDao.addGame(game);
        Round round49 = new Round("1231", "e:1:p:2", 49, 1, LocalDateTime.of(2012,12,12,1,2,0).withNano(0));
        Round round50 = new Round("9999", "e:0:p:0", 50, 1, LocalDateTime.of(2010, 8, 21, 4, 53, 22).withNano(0));
        Round round51 = new Round("1432", "e:4:p:0", 51, 1, LocalDateTime.of(2015, 8, 21, 4, 53, 22).withNano(0));
        rDao.addRound(round49);
        rDao.addRound(round50);
        rDao.addRound(round51);
        List<Round> shouldBeSorted = service.sortRoundsForGame(1);

        assertEquals(round50, shouldBeSorted.get(0));
        assertEquals(round49, shouldBeSorted.get(1));
        assertEquals(round51, shouldBeSorted.get(2));

    }
    @Test
    public void testIsPrime() {
        assertTrue(service.isPrime(3));
        assertFalse(service.isPrime(4));
        assertTrue(service.isPrime(1289));
        assertFalse(service.isPrime(1290));
    }
    @Test
    public void testRhyme() {
        assertEquals(service.getRhyme(5), "JIVE");
        assertEquals(service.getRhyme(0), "NERO");
    }
}