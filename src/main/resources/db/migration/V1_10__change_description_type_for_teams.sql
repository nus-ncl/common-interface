-- -----------------------------------------------------
-- AlTER `teams`.`description`
-- -----------------------------------------------------
ALTER TABLE teams
MODIFY COLUMN description LONGTEXT

ALTER TABLE team_members
ADD COLUMN `notes` LONGTEXT