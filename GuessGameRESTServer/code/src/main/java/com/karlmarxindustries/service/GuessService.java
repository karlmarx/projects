package com.karlmarxindustries.service;

import com.karlmarxindustries.dao.GameDao;
import com.karlmarxindustries.dao.RoundDao;
import com.karlmarxindustries.dto.Game;
import com.karlmarxindustries.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class GuessService {
    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;


    List<Round> getAllForGame(int gameId) {
        return roundDao.getAllForGame(gameId);
    }

    public int begin() {
        Game newGame = new Game();
        newGame.setSolved(false);
        newGame.setAnswer(generateAnswerOrRandomGuess());
        Game addedGame = gameDao.addGame(newGame);
        return addedGame.getGameId();
    }

    private String generateAnswerOrRandomGuess() {
        List<Integer> unique = generateUniqueRandomNumbers(9, 4);
        String answerString = "";
        for (Integer each : unique) {
            answerString += each.toString();
        }
        return answerString;
    }

    public List<Integer> generateUniqueRandomNumbers(int max, int generations) {
        List<Integer> list = new ArrayList<>();
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < (max + 1); i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < generations; i++) {
            resultList.add(list.get(i));
        }
        return resultList;
    }

    public List<Integer> generateUniqueRandomNumbers(int max, int generationsNeeded, List<Integer> alreadyChosen) {
        List<Integer> list = new ArrayList<>();
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < (max + 1); i++) {
            list.add(i);
        }
        for (Integer each : alreadyChosen) {
            list.remove(each);
        }
        Collections.shuffle(list);
        for (int i = 0; i < (generationsNeeded - alreadyChosen.size()); i++) {
            resultList.add(list.get(i));
        }
        for (Integer each : alreadyChosen) {
            resultList.add(each);
        }
        return resultList;
    }

    public Round checkGuess(Round guessToCheck) {
        guessToCheck.setTimestamp(LocalDateTime.now().withNano(0));
        Game currentGame = gameDao.getGameById(guessToCheck.getGameId());
        if (currentGame.getAnswer().equals(guessToCheck.getGuess())) {
            currentGame.setSolved(true);
            gameDao.updateGame(currentGame);
            guessToCheck.setResult("e:4:p:0");
        } else {
            String resultCalculated = calculateResult(guessToCheck, currentGame.getAnswer());
            guessToCheck.setResult(resultCalculated);
        }
        return roundDao.addRound(guessToCheck);
    }

    public String calculateResult(Round guessToCheck, String correctAnswer) {
        String[] guessArray = guessToCheck.getGuess().split("");
        String[] answerArray = correctAnswer.split("");
        int exactMatch = 0;
        int partialMatch = 0;
        for (String eachGuess : guessArray) {
            for (String eachAnswer : answerArray) {
                if (eachGuess.equals(eachAnswer)) {
                    partialMatch++;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (guessArray[i].equals(answerArray[i])) {
                exactMatch++;
                partialMatch--;
            }
        }
        String result = "e:" + exactMatch + ":p:" + partialMatch;
        return result;
    }

    public List<Game> getAllGamesRedacted() {
        List<Game> redactGames = gameDao.getAllGames();
        for (Game each : redactGames) {
            if (!each.isSolved()) {
                each.setAnswer("----");
            }

        }
        return redactGames;
    }

    public Game getGameRedacted(int gameId) {
        Game redactGame = gameDao.getGameById(gameId);
        if (!redactGame.isSolved()) {
            redactGame.setAnswer("----");
        }
        return redactGame;
    }

    public List<Round> sortRoundsForGame(int gameId) {
        List<Round> roundsBeingSorted = getAllForGame(gameId);
        Collections.sort(roundsBeingSorted, new Comparator<Round>() {
            @Override
            public int compare(Round r1, Round r2) {
                return r1.getTimestamp().compareTo(r2.getTimestamp()); //valid?
            }
        });

        return roundsBeingSorted;
    }

    public Round makeAndCheck(Round guessBeingGenerated) {
        //this.generateUniqueRandomNumbers(9,4);
        String generatedGuess = "0000";
        List<Round> existingRounds = roundDao.getAllForGame(guessBeingGenerated.getGameId());
        if (existingRounds.size() > 0) {
            int previousRoundId = 0;
            String previousGuess = "";
            String previousResult = "e:0:p:0";
            for (Round each : existingRounds) {
                if (each.getRoundId() > previousRoundId) {
                    previousRoundId = each.getRoundId();
                    previousResult = each.getResult();
                    previousGuess = each.getGuess();
                }
            }
            generatedGuess = guessBasedOnPreviousRound(previousGuess, previousResult);
        } else {
            generatedGuess = generateAnswerOrRandomGuess(); //change method name to be more general
        }
        guessBeingGenerated.setGuess(generatedGuess);
        return checkGuess(guessBeingGenerated);

    }

    //extra
    private String guessBasedOnPreviousRound(String previousGuess, String previousResult) {
        int exactMatches = Integer.parseInt(previousResult.substring(2, 3));
        int partialMatches = Integer.parseInt(previousResult.substring(6));
        int totalMatches = partialMatches + exactMatches;
        String[] previousGuessArray = previousGuess.split("");
        List<Integer> indicesToKeep = generateUniqueRandomNumbers(3, totalMatches);
        List<Integer> integersToKeep = new ArrayList<>();
        for (Integer each : indicesToKeep) {
            integersToKeep.add(Integer.valueOf(previousGuessArray[each]));
        }
        List<Integer> generatedGuess = generateUniqueRandomNumbers(9, 4, integersToKeep);
        String answerString = "";
        for (Integer each : generatedGuess) {
            answerString += each.toString();
        }
        return answerString;
    }

    //extra
    public List<Round> keepGuessingForMe(Round guessToGenerate) {
        boolean solved = false;
        Round currentRound;
        Game currentGame = gameDao.getGameById(guessToGenerate.getGameId());
        String answer = currentGame.getAnswer();
        while (!solved) {
            currentRound = makeAndCheck(guessToGenerate);
            if (currentRound.getGuess().equals(answer)) {
                currentGame.setSolved(true);
                gameDao.updateGame(currentGame);
                solved = true;
            }
        }
        return roundDao.getAllForGame(currentGame.getGameId());
    }

    //extra
    public String getRandomHint(int gameId) {
        Random random = new Random();
        String answerString = gameDao.getGameById(gameId).getAnswer();
        int answer = Integer.parseInt(answerString);
        int choice = random.nextInt(4);
        int intPosition1 = Integer.parseInt(answerString.substring(0, 1));
        int intPosition2 = Integer.parseInt(answerString.substring(1, 2));
        int intPosition3 = Integer.parseInt(answerString.substring(2, 3));
        int intPosition4 = Integer.parseInt(answerString.substring(3));
        String hint = "";
        switch (choice) {
            case 0:
                if (isPrime(answer)) {
                    hint += "The answer is a prime number";
                } else {
                    hint += "The answer is not a prime number";
                }
                break;
            case 1:
                hint += rhymeRandomDigit(answerString);
                break;
            case 2:
                int product = intPosition1 * intPosition2 * intPosition3 * intPosition4;
                hint += "The product of digits in the answer is " + String.valueOf(product);
                break;
            case 3:
                String sum = String.valueOf(intPosition1 + intPosition2 + intPosition3 + intPosition4);
                if (sum.substring(sum.length() - 1).equals("0")) {
                    hint += rhymeRandomDigit(answerString); //numbers ending in 0 have unpredictable rhymes
                } else {
                    hint += "The sum of the digits rhymes with ";
                    hint += getRhyme(Integer.parseInt(sum.substring(sum.length() - 1)));
                }
                break;
        }
        hint += ".";
        return hint;
    }

    //extra
    private String rhymeRandomDigit(String answerString) {
        Random random = new Random();
        String rhymeOfDigit = "";
        int choice = random.nextInt(4);
        String position = "";
        switch (choice) {
            case 0:
                rhymeOfDigit = getRhyme((Integer.parseInt(answerString.substring(0, 1))));
                position = "The first digit ";
                break;
            case 1:
                rhymeOfDigit = getRhyme((Integer.parseInt(answerString.substring(1, 2))));
                position = "The second digit ";
                break;
            case 2:
                rhymeOfDigit = getRhyme((Integer.parseInt(answerString.substring(2, 3))));
                position = "The third digit ";
                break;
            case 3:
                rhymeOfDigit = getRhyme((Integer.parseInt(answerString.substring(3))));
                position = "The fourth digit ";
                break;
        }
        String result = position + "rhymes with " + rhymeOfDigit;
        return result;
    }

    //extra
    public String getRhyme(int intIn) {
        switch (intIn) {
            case 0:
                return "NERO";
            case 1:
                return "FUN";
            case 2:
                return "NEW";
            case 3:
                return "FREE";
            case 4:
                return "LORE";
            case 5:
                return "JIVE";
            case 6:
                return "STYX";
            case 7:
                return "LEAVEN";
            case 8:
                return "PLAIT";
            case 9:
                return "SINE";
            default:
                return "INVALID (must be >0 and <10)";
        }

    }


    public static <T> Set<T> convertListToSet(List<T> list) {
        return new HashSet<>(list);
    }

    public boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
