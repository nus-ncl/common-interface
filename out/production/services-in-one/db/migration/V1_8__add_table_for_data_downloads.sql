-- -----------------------------------------------------
-- Table `prod`.`data_downloads`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_downloads` (
  `id`              BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `data_id`         BIGINT(20)   NOT NULL,
  `resource_id`     BIGINT(20)   NOT NULL,
  `download_date`   DATETIME     NOT NULL,
  `hashed_user_id`  VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_m23m239poytcx576ugujgrr` (`hashed_user_id` ASC)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;