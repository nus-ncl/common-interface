-- -----------------------------------------------------
-- Table `prod`.`data_public_users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_public_users` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `full_name`          VARCHAR(255) NOT NULL,
  `email`              VARCHAR(255) NOT NULL,
  `job_title`          VARCHAR(255) NOT NULL,
  `institution`        VARCHAR(255) NOT NULL,
  `country`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `prod`.`data_public_downloads`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_public_downloads` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `data_id`            BIGINT(20)   NOT NULL,
  `resource_id`        BIGINT(20)   NOT NULL,
  `download_date`      DATETIME     NOT NULL,
  `public_user_id`     BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;