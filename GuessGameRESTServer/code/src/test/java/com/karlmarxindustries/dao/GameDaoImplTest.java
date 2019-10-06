package com.karlmarxindustries.dao;

import com.karlmarxindustries.TestApplicationConfiguration;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoImplTest {
    @Autowired
    GameDao gameDao;
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
    public void addGameGetGame() {
        Game gameBeingAdded = new Game();
        gameBeingAdded.setSolved(false);
        gameBeingAdded.setAnswer("3210");
        Game addedGame = gameDao.addGame(gameBeingAdded);
        Game shouldBeGameOne = gameDao.getGameById(addedGame.getGameId());
        List<Game> shouldHaveOneOnly = gameDao.getAllGames();


        assertEquals(addedGame, shouldBeGameOne);
        assertTrue(shouldHaveOneOnly.contains(addedGame));
        assertEquals(shouldHaveOneOnly.size(), 1);
    }

    @Test
    public void getAll() {
        List<Game> shouldBeEmpty = gameDao.getAllGames();
        Game game = new Game("1432", false);
        Game gameWithId = gameDao.addGame(game);
        Game game2 = new Game("8622", false);
        Game game2WithId = gameDao.addGame(game2);
        Game game3 = new Game("0193", true);
        Game game3WithId = gameDao.addGame(game3);
        List<Game> theseGamesOnly = new ArrayList<>();
        theseGamesOnly.add(gameWithId);
        theseGamesOnly.add(game2WithId);
        theseGamesOnly.add(game3WithId);

        List<Game> shouldBeThreeGames = gameDao.getAllGames();
        assertEquals(3, shouldBeThreeGames.size());
        assertEquals(0, shouldBeEmpty.size());
        assertEquals(shouldBeThreeGames, theseGamesOnly);
    }

    @Test
    public void testUpdateGame() {
        Game game = new Game("1432", false);
        Game gameWithId = gameDao.addGame(game);
        Game changedGameToUpdate = gameWithId;
        changedGameToUpdate.setAnswer("5426");
        changedGameToUpdate.setSolved(true);
        gameDao.updateGame(changedGameToUpdate);
        Game gottenGamePostUpdate = gameDao.getGameById(gameWithId.getGameId());

        assertEquals(true, gottenGamePostUpdate.isSolved());
        assertEquals("5426", gottenGamePostUpdate.getAnswer());
    }

//    @Test
//    public void getAllForGame() {
//        Round round42 = new Round("6453", "e:1:p:2", 42, 2, LocalDateTime.now().withNano(0));
//        Round round81 = new Round("1217", "e:0:p:0", 81, 2, LocalDateTime.of(2014, 10, 1, 4, 53, 22).withNano(0));
//        Round round86 = new Round("1432", "e:4:p:0", 86, 3, LocalDateTime.of(1972, 8, 11, 4, 22, 22).withNano(0));
//        roundDao.addRound(round42);
//        roundDao.addRound(round81);
//        roundDao.addRound(round86);
//        List<Round> shouldBeRoundsFor2 = roundDao.getAllForGame(2);
//        List<Round> shouldBeRoundsFor3 = roundDao.getAllForGame(3);
//        List<Round> shouldBeEmpty = roundDao.getAllForGame(4);
//        assertTrue(shouldBeRoundsFor2.contains(round42));
//        assertTrue(shouldBeRoundsFor2.contains(round81));
//        assertFalse(shouldBeRoundsFor2.contains(round86));
//        assertFalse(shouldBeRoundsFor3.contains(round42));
//        assertFalse(shouldBeRoundsFor3.contains(round81));
//        assertTrue(shouldBeRoundsFor3.contains(round86));
//        assertEquals(0, shouldBeEmpty.size());
//    }
}