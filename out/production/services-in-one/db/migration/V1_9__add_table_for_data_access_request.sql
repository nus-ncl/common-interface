-- -----------------------------------------------------
-- Table `prod`.`data_access_requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_access_requests` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `data_id`            BIGINT(20)   NOT NULL,
  `requester_id`       VARCHAR(255) NOT NULL,
  `reason`             LONGTEXT     NOT NULL,
  `request_date`       DATETIME     NOT NULL,
  `approved_date`      DATETIME     NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
/*!  DEFAULT CHARACTER SET = latin1 */;
