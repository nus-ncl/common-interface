-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema ncl
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ncl`
  DEFAULT CHARACTER SET latin1;
USE `ncl`;

-- -----------------------------------------------------
-- Table `ncl`.`addresses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`addresses` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `address_1`          VARCHAR(255) NOT NULL,
  `address_2`          VARCHAR(255) NULL     DEFAULT NULL,
  `city`               VARCHAR(255) NOT NULL,
  `country`            VARCHAR(255) NOT NULL,
  `region`             VARCHAR(255) NULL     DEFAULT NULL,
  `zip_code`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`credentials`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`credentials` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `password`           VARCHAR(255) NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `username`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_l7xhygibdj6cgkpj2ih1jgx14` (`username` ASC),
  INDEX `IDXl7xhygibdj6cgkpj2ih1jgx14` (`username` ASC)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`credentials_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`credentials_roles` (
  `credentials_id` VARCHAR(255) NOT NULL,
  `role`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`credentials_id`, `role`),
  CONSTRAINT `FK_credentials`
  FOREIGN KEY (`credentials_id`)
  REFERENCES `ncl`.`credentials` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`datasets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`datasets` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `name`               VARCHAR(255) NOT NULL,
  `description`        LONGTEXT     NULL,
  `accessibility`      VARCHAR(255) NOT NULL,
  `visibility`         VARCHAR(255) NOT NULL,
  `contributer_id`     VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  INDEX `name_idx` (`name` ASC),
  FULLTEXT INDEX `description_txt` (`description` ASC)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`datasets_statistics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`datasets_statistics` (
  `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `date`       DATETIME     NOT NULL,
  `user_id`    VARCHAR(255) NOT NULL,
  `dataset_id` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_datasets_statistics_idx` (`dataset_id` ASC),
  CONSTRAINT `FK_datasets_statistics`
  FOREIGN KEY (`dataset_id`)
  REFERENCES `ncl`.`datasets` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`datasets_resources`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`datasets_resources` (
  `id`                 BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME   NOT NULL,
  `last_modified_date` DATETIME   NOT NULL,
  `version`            BIGINT(20) NOT NULL,
  `dataset_id`         BIGINT(20) NOT NULL,
  `uri`                TEXT       NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_datasets_resources_idx` (`dataset_id` ASC),
  CONSTRAINT `FK_datasets_resources`
  FOREIGN KEY (`dataset_id`)
  REFERENCES `ncl`.`datasets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`deterlab_project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`deterlab_project` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `deter_project_id`   VARCHAR(255) NOT NULL,
  `ncl_team_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_hh8y4kg284v94a79ygpy31ewb` (`deter_project_id` ASC),
  UNIQUE INDEX `UK_hhtgwx7cpbibo46ca82l32oce` (`ncl_team_id` ASC)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`deterlab_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`deterlab_user` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `deter_user_id`      VARCHAR(255) NOT NULL,
  `ncl_user_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_nu93l9b4l6t5kcremcgxl6nqv` (`deter_user_id` ASC),
  UNIQUE INDEX `UK_i6dsyy2vylkp88w3ttkm1v22g` (`ncl_user_id` ASC)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`email_retries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`email_retries` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `bcc`                TINYBLOB     NULL     DEFAULT NULL,
  `cc`                 TINYBLOB     NULL     DEFAULT NULL,
  `content`            VARCHAR(255) NOT NULL,
  `error_message`      VARCHAR(255) NULL     DEFAULT NULL,
  `html`               BIT(1)       NOT NULL,
  `last_retry_time`    DATETIME     NOT NULL,
  `recipients`         TINYBLOB     NOT NULL,
  `retry_times`        INT(11)      NOT NULL,
  `sender`             VARCHAR(255) NOT NULL,
  `sent`               BIT(1)       NOT NULL,
  `subject`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`experiments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`experiments` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `description`        VARCHAR(255) NOT NULL,
  `idle_swap`          INT(11)      NOT NULL,
  `max_duration`       INT(11)      NOT NULL,
  `name`               VARCHAR(255) NOT NULL,
  `ns_file`            VARCHAR(255) NOT NULL,
  `ns_file_content`    LONGTEXT     NOT NULL,
  `team_id`            VARCHAR(255) NOT NULL,
  `team_name`          VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`user_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`user_details` (
  `id`                       BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`             DATETIME     NOT NULL,
  `last_modified_date`       DATETIME     NOT NULL,
  `version`                  BIGINT(20)   NOT NULL,
  `email`                    VARCHAR(255) NOT NULL,
  `first_name`               VARCHAR(255) NOT NULL,
  `institution`              VARCHAR(255) NOT NULL,
  `institution_abbreviation` VARCHAR(255) NOT NULL,
  `institution_web`          VARCHAR(255) NOT NULL,
  `job_title`                VARCHAR(255) NOT NULL,
  `last_name`                VARCHAR(255) NOT NULL,
  `phone`                    VARCHAR(255) NOT NULL,
  `address_id`               BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK4d9rdl7d52k8x3etihxlaujvh` (`email` ASC),
  INDEX `FK3d5b1ii2vbv6fyt2bbijwmlqg` (`address_id` ASC),
  CONSTRAINT `FK3d5b1ii2vbv6fyt2bbijwmlqg`
  FOREIGN KEY (`address_id`)
  REFERENCES `ncl`.`addresses` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`users` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `application_date`   DATETIME     NOT NULL,
  `is_email_verified`  CHAR(1)      NOT NULL,
  `processed_date`     DATETIME     NULL DEFAULT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `verification_key`   VARCHAR(255) NULL DEFAULT NULL,
  `user_details_id`    BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_4ai7rrtrvwtgtqavv8okpxrul` (`user_details_id` ASC),
  CONSTRAINT `FK8uj8y5ad4xl01w9wcracimb14`
  FOREIGN KEY (`user_details_id`)
  REFERENCES `ncl`.`user_details` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`login_activities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`login_activities` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `date`               DATETIME     NOT NULL,
  `ip_address`         VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK8dyu3xxtaw62bm46xqi9romha` (`user_id` ASC),
  CONSTRAINT `FK8dyu3xxtaw62bm46xqi9romha`
  FOREIGN KEY (`user_id`)
  REFERENCES `ncl`.`users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`realizations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`realizations` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `details`            LONGTEXT     NULL     DEFAULT NULL,
  `experiment_id`      BIGINT(20)   NOT NULL,
  `experiment_name`    VARCHAR(255) NOT NULL,
  `idle_minutes`       BIGINT(20)   NOT NULL,
  `num_nodes`          INT(11)      NOT NULL,
  `running_minutes`    BIGINT(20)   NOT NULL,
  `state`              VARCHAR(255) NOT NULL,
  `team_id`            VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`registrations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`registrations` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `pid`                VARCHAR(255) NOT NULL,
  `uid`                VARCHAR(255) NOT NULL,
  `usr_addr`           VARCHAR(255) NOT NULL,
  `usr_addr2`          VARCHAR(255) NULL     DEFAULT NULL,
  `usr_affil`          VARCHAR(255) NOT NULL,
  `usr_affil_abbrev`   VARCHAR(255) NOT NULL,
  `usr_city`           VARCHAR(255) NOT NULL,
  `usr_country`        VARCHAR(255) NOT NULL,
  `usr_email`          VARCHAR(255) NOT NULL,
  `usr_name`           VARCHAR(255) NOT NULL,
  `usr_phone`          VARCHAR(255) NOT NULL,
  `usr_state`          VARCHAR(255) NOT NULL,
  `usr_title`          VARCHAR(255) NOT NULL,
  `usr_zip`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_6t1f14auw7in83xccrbi8kef2` (`uid` ASC),
  UNIQUE INDEX `UK_i4da24h32w6sf4upvsts7uhog` (`usr_email` ASC)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`teams` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `application_date`   DATETIME     NOT NULL,
  `description`        VARCHAR(255) NULL DEFAULT NULL,
  `name`               VARCHAR(255) NOT NULL,
  `organisation_type`  VARCHAR(255) NOT NULL,
  `privacy`            VARCHAR(255) NOT NULL,
  `processed_date`     DATETIME     NULL DEFAULT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `visibility`         VARCHAR(255) NOT NULL,
  `website`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_a510no6sjwqcx153yd5sm4jrr` (`name` ASC)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`team_members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`team_members` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `joined_date`        DATETIME     NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `member_type`        VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NOT NULL,
  `team_id`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKs8nuwsa7nvebc246ed822w68x` (`team_id` ASC, `user_id` ASC),
  CONSTRAINT `FKtgca08el3ofisywcf11f0f76t`
  FOREIGN KEY (`team_id`)
  REFERENCES `ncl`.`teams` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`datasets_users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`datasets_users` (
  `dataset_id` BIGINT(20)   NOT NULL,
  `user_id`    VARCHAR(255) NOT NULL,
  PRIMARY KEY (`dataset_id`, `user_id`),
  CONSTRAINT `FK_datasets_users`
  FOREIGN KEY (`dataset_id`)
  REFERENCES `ncl`.`datasets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `ncl`.`users_teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ncl`.`users_teams` (
  `user_id` VARCHAR(255) NOT NULL,
  `team_id` VARCHAR(255) NOT NULL,
  UNIQUE INDEX `UKbqjpbghgx6qnprt06mi96bv1q` (`user_id` ASC, `team_id` ASC),
  INDEX `IDX4fw1eq004xbrrr0lh5e6nhvrv` (`user_id` ASC),
  INDEX `IDXook8yqr9dleaw16r7iusof0f9` (`team_id` ASC),
  CONSTRAINT `FK31k9hhkcp7fiugrk2lu7vq9jo`
  FOREIGN KEY (`user_id`)
  REFERENCES `ncl`.`users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = latin1;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
