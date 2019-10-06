package com.karlmarxindustries.dao;

import com.karlmarxindustries.TestApplicationConfiguration;
import com.karlmarxindustries.dto.Game;
import com.karlmarxindustries.dto.Round;


import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class RoundDaoImplTest {
    @Autowired
    RoundDao roundDao;
    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    GameDao gameDao;
    @Before
    public void setUp() throws Exception {
        jdbc.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbc.execute("TRUNCATE TABLE `GuessTest`.`round`;");
        jdbc.execute("TRUNCATE TABLE `GuessTest`.`game`;");
        jdbc.execute("SET FOREIGN_KEY_CHECKS = 1;");
        Game game = new Game(1, "1432", false);
        gameDao.addGame(game);
        Game game2 = new Game(2, "8622", false);
        gameDao.addGame(game2);
        Game game3 = new Game(3, "0193", true);
        gameDao.addGame(game3);
    }
    @Test
    public void addRoundGetRounds() {

        Round round = new Round();
        round.setResult("e:1:p:2");
        round.setGuess("2321");
        round.setTimestamp(LocalDateTime.now().withNano(0));
        round.setGameId(1);
        round.setRoundId(48);
        round = roundDao.addRound(round);
        List<Round> fromDao = roundDao.getAllForGame(1);
        assertEquals(round, fromDao.get(0));
        assertEquals (round.getGuess(), fromDao.get(0).getGuess());
        assertEquals (round.getResult(), fromDao.get(0).getResult());
        assertEquals(round.getTimestamp(), fromDao.get(0).getTimestamp());
        assertEquals(round.getGameId(), fromDao.get(0).getGameId());
        assertEquals(round.getRoundId(), fromDao.get(0).getRoundId());
        assertEquals(fromDao.size(), 1);
    }

    @Test
    public void getAll() {
        List<Round> shouldBeEmpty = roundDao.getAll();

        Round round49 = new Round("1231", "e:1:p:2", 49, 1, LocalDateTime.now().withNano(0));
        Round round50 = new Round("9999", "e:0:p:0", 50, 1, LocalDateTime.of(2019, 8, 21, 4, 53, 22).withNano(0));
        Round round51 = new Round("1432", "e:4:p:0", 51, 1, LocalDateTime.of(2019, 8, 21, 4, 53, 22).withNano(0));
        List<Round> theseRoundsOnly = new ArrayList<>();
        theseRoundsOnly.add(round49);
        theseRoundsOnly.add(round50);
        theseRoundsOnly.add(round51);
        roundDao.addRound(round49);
        roundDao.addRound(round50);
        roundDao.addRound(round51);
        List<Round> shouldBeThreeRounds = roundDao.getAll();
        assertEquals(3, shouldBeThreeRounds.size());
        assertEquals(shouldBeThreeRounds, theseRoundsOnly);
    }

    @Test
    public void getAllForGame() {
        Round round42 = new Round("6453", "e:1:p:2", 42, 2, LocalDateTime.now().withNano(0));
        Round round81 = new Round("1217", "e:0:p:0", 81, 2, LocalDateTime.of(2014, 10, 1, 4, 53, 22).withNano(0));
        Round round86 = new Round("1432", "e:4:p:0", 86, 3, LocalDateTime.of(1972, 8, 11, 4, 22, 22).withNano(0));
        roundDao.addRound(round42);
        roundDao.addRound(round81);
        roundDao.addRound(round86);
        List<Round> shouldBeRoundsFor2 = roundDao.getAllForGame(2);
        List<Round> shouldBeRoundsFor3 = roundDao.getAllForGame(3);
        List<Round> shouldBeEmpty = roundDao.getAllForGame(4);
        assertTrue(shouldBeRoundsFor2.contains(round42));
        assertTrue(shouldBeRoundsFor2.contains(round81));
        assertFalse(shouldBeRoundsFor2.contains(round86));
        assertFalse(shouldBeRoundsFor3.contains(round42));
        assertFalse(shouldBeRoundsFor3.contains(round81));
        assertTrue(shouldBeRoundsFor3.contains(round86));
        assertEquals(0, shouldBeEmpty.size());
    }
}