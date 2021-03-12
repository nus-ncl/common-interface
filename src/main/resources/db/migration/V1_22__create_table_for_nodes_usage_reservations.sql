-- -----------------------------------------------------
-- Table `prod`.`nodes_usage_reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`nodes_usage_reservation` (
  `id`                 BIGINT(20)     NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME       NOT NULL,
  `last_modified_date` DATETIME       NOT NULL,
  `version`            BIGINT(20)     NOT NULL,
  `project_id`         BIGINT(20)     NOT NULL ,
  `start_date`         DATETIME       NOT NULL,
  `end_date`           DATETIME       NOT NULL,
  `no_nodes`           INT            NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`project_id`)
  REFERENCES `prod`.`project_details` (`id`)
  )
ENGINE = InnoDB;