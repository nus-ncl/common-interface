-- -----------------------------------------------------
-- Table `prod`.`data`
-- -----------------------------------------------------
ALTER TABLE `prod`.`data`
ADD COLUMN `release_date` DATETIME NOT NULL AFTER `contributor_id`;
