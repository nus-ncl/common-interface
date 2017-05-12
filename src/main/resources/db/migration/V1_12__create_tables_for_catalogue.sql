-- -----------------------------------------------------
-- Table `prod`.`data_keywords`
-- Keywords that describes a dataset.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_keywords` (
  `data_id`    BIGINT(20)    NOT NULL,
  `keyword`    VARCHAR(255)  NOT NULL,
  PRIMARY KEY (`data_id`, `keyword`),
  INDEX `IDXa565e755709166653ad81ac92b39cc15` (`data_id` ASC),
  INDEX `IDXd7df5b64df1181ef1d62d646a13aa860` (`keyword` ASC),
  CONSTRAINT `FKa565e755709166653ad81ac92b39cc15`
  FOREIGN KEY (`data_id`)
  REFERENCES `prod`.`data` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `prod`.`data_categories`
-- Categories that a dataset can belongs to.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_categories` (
  `id`       	         BIGINT(20)	  NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `name`               VARCHAR(255)	NOT NULL,
  `description`        LONGTEXT	    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- TABLE `prod`.`data`
-- Update dataset to have a category.
-- -----------------------------------------------------
ALTER TABLE `prod`.`data`
  ADD COLUMN `category_id` BIGINT(20) NOT NULL AFTER `contributor_id`;
