-- -----------------------------------------------------
-- Table `prod`.`data_downloads`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_downloads` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `data_id`            BIGINT(20)   NOT NULL,
  `resource_id`        BIGINT(20)   NOT NULL,
  `download_date`      DATETIME     NOT NULL,
  `hashed_user_id`     VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;