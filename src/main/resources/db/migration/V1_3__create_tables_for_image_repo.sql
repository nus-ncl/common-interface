-- -----------------------------------------------------
-- Table `prod`.`images`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`images` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `team_id`            VARCHAR(255) NOT NULL,
  `node_id`            VARCHAR(255) NOT NULL,
  `image_name`         VARCHAR(255) NOT NULL,
  `description`        VARCHAR(255) NULL,
  `visibility`         VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_krxed372ocm2ni4s42df6hji9` (`id` ASC),
  INDEX `IDXkrxed372ocm2ni4s42df6hji9` (`id` ASC)
)
ENGINE = InnoDB;