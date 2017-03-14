-- -----------------------------------------------------
-- AlTER `teams`.`description`
-- -----------------------------------------------------
USE `prod`;
ALTER TABLE teams
MODIFY COLUMN description LONGTEXT;