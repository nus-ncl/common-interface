-- -----------------------------------------------------
-- Table `prod`.`password_reset_requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`password_reset_requests` (
  `id`       BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `hash`     VARCHAR(255) NOT NULL,
  `time`     DATETIME     NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_m23m239sdqcx153ddm4jrr` (`hash` ASC)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;