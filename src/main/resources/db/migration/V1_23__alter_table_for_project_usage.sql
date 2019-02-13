-- -----------------------------------------------------
-- TABLE `prod`.`project_usage`
-- Add columns incurred and waived
-- -----------------------------------------------------
ALTER TABLE `prod`.`project_usage`
  ADD COLUMN `incurred` NUMERIC(10,2) NOT NULL,
  ADD COLUMN `waived` NUMERIC(10,2) NOT NULL;
