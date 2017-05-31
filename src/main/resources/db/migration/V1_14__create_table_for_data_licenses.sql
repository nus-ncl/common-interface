-- -----------------------------------------------------
-- Table `prod`.`data_licenses`
-- Keywords that describes a dataset.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_licenses` (
  `id`       	         BIGINT(20)	  NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `name`               VARCHAR(255)	NOT NULL UNIQUE,
  `acronym`            VARCHAR(255)	NOT NULL UNIQUE,
  `description`        LONGTEXT	    NULL,
  `link`               LONGTEXT	    NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- TABLE `prod`.`data`
-- Update dataset to have a license.
-- -----------------------------------------------------
ALTER TABLE `prod`.`data`
  ADD COLUMN `license_id` BIGINT(20) NOT NULL AFTER `category_id`;
