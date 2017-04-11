-- -----------------------------------------------------
-- AlTER `experiments`.`description`
-- -----------------------------------------------------
USE `prod`;
ALTER TABLE experiments
MODIFY COLUMN description LONGTEXT NOT NULL;