-- -----------------------------------------------------
-- Table `prod`.`data`
-- -----------------------------------------------------
ALTER TABLE `prod`.`data`
ADD COLUMN `released_date` DATETIME NOT NULL AFTER `contributor_id`;
