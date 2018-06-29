-- -----------------------------------------------------
-- TABLE `prod`.`team_members`
-- Update team_members to have a member_privilege column
-- Set member_privilege column to default to 'LOCAL_ROOT'
-- -----------------------------------------------------
ALTER TABLE `prod`.`team_members`
  ADD COLUMN `member_privilege` VARCHAR(255) NOT NULL DEFAULT 'LOCAL_ROOT';

-- ---------------------------------------------------------------------------------
-- TABLE `prod`.`team_members`
-- Update member_privilege column to PROJECT_ROOT if member is an owner of the team
-- ---------------------------------------------------------------------------------
UPDATE `prod`.`team_members`
  SET `member_privilege` = 'PROJECT_ROOT'
  WHERE `member_type` = 'OWNER';