CREATE SCHEMA `snakes_and_ladders` ;

CREATE TABLE `snakes_and_ladders`.`game` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `token_position` INT NOT NULL,
  PRIMARY KEY (`id`));

INSERT INTO `snakes_and_ladders`.`game` (`id`, `token_position`) VALUES ('1', '1');
