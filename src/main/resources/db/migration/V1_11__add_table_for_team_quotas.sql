-- -----------------------------------------------------
-- Table `prod`.`team_quotas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`team_quotas` (
  `id`                 BIGINT(20)      NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME        NOT NULL,
  `last_modified_date` DATETIME        NOT NULL,
  `version`            BIGINT(20)      NOT NULL,
  `team_id`            VARCHAR(255)    NOT NULL,
  `quota`              NUMERIC(10,2)   NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_krxed372l252ni4s42df6hji9` (`team_id` ASC)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;