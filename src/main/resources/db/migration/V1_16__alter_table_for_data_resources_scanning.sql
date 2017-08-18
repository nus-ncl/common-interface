-- -----------------------------------------------------
-- TABLE `prod`.`data_resources`
-- Update dataset to have a is_malicious column
-- Set dataset is_malicious column to default to 'N'
-- -----------------------------------------------------
ALTER TABLE `prod`.`data_resources`
  ADD COLUMN `is_malicious` CHAR(1) NOT NULL DEFAULT 'N';

-- -----------------------------------------------------
-- TABLE `prod`.`data_resources`
-- Update dataset to have a is_scanned column
-- Set dataset is_scanned column to default to 'N'
-- -----------------------------------------------------
ALTER TABLE `prod`.`data_resources`
  ADD COLUMN `is_scanned` CHAR(1) NOT NULL DEFAULT 'N';