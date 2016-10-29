-- -----------------------------------------------------
-- Table `prod`.`data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `name`               VARCHAR(255) NOT NULL,
  `description`        LONGTEXT     NULL     DEFAULT NULL,
  `accessibility`      VARCHAR(255) NOT NULL,
  `visibility`         VARCHAR(255) NOT NULL,
  `contributor_id`     VARCHAR(255) NOT NULL,
  `release_date`       DATETIME     NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_krxed372ocm2ni4s42qjnuxmn` (`name` ASC),
  INDEX `IDXkrxed372ocm2ni4s42qjnuxmn` (`name` ASC)
)
  ENGINE = InnoDB
/*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `prod`.`data_resources`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_resources` (
  `id`                 BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME   NOT NULL,
  `last_modified_date` DATETIME   NOT NULL,
  `version`            BIGINT(20) NOT NULL,
  `uri`                LONGTEXT   NOT NULL,
  `data_id`            BIGINT(20)     NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKrdkpugv0dirr7qndj3mbpp3sk` (`data_id` ASC),
  CONSTRAINT `FKrdkpugv0dirr7qndj3mbpp3sk`
  FOREIGN KEY (`data_id`)
  REFERENCES `prod`.`data` (`id`)
)
  ENGINE = InnoDB
/*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `prod`.`data_users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_users` (
  `data_id` BIGINT(20)   NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`data_id`, `user_id`),
  INDEX `IDX8mpouaqdmub2nq9ddo8qejf1c` (`data_id` ASC),
  INDEX `IDXi9wyunmr4u1s8y7m1rs6vj3rk` (`user_id` ASC),
  CONSTRAINT `FK85t3qytqm5q1l005hj4qi5702`
  FOREIGN KEY (`data_id`)
  REFERENCES `prod`.`data` (`id`)
)
  ENGINE = InnoDB
/*!  DEFAULT CHARACTER SET = latin1 */;

