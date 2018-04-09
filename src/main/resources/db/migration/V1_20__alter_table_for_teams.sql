-- -----------------------------------------------------
-- TABLE `prod`.`teams`
-- Add column class
-- -----------------------------------------------------
ALTER TABLE `prod`.`teams`
  ADD COLUMN `class` CHAR(1) NOT NULL DEFAULT 'N';
