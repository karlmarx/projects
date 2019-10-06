DROP DATABASE IF EXISTS GuessTestTest;

CREATE DATABASE GuessTest;

USE GuessTest;

CREATE TABLE 
`GuessTest`.`game` (
    `game_id` INT NOT NULL AUTO_INCREMENT,
    `solved` TINYINT NOT NULL DEFAULT '0',
    `answer` CHAR(4),
    PRIMARY KEY (`game_id`)
);
  
CREATE TABLE `GuessTest`.`round` (
    `round_id` INT NOT NULL AUTO_INCREMENT,
	`GuessTest` CHAR(4),
    `result` CHAR(7),
	`game_id` INT NOT NULL,
    `timestamp` TIMESTAMP,
    PRIMARY KEY (`round_id`)
);
  
  
ALTER TABLE `GuessTest`.`round` 
ADD CONSTRAINT fk_round_game
FOREIGN KEY (`game_id`)
        REFERENCES `game` (`game_id`)
        ;

    
    

   
