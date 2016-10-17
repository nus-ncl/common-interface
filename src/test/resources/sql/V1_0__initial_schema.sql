CREATE SCHEMA IF NOT EXISTS `prod`;

USE `prod`;

-- -----------------------------------------------------
-- Table `addresses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `addresses` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `address_1`          VARCHAR(255) NOT NULL,
  `address_2`          VARCHAR(255) NULL     DEFAULT NULL,
  `city`               VARCHAR(255) NOT NULL,
  `country`            VARCHAR(255) NOT NULL,
  `region`             VARCHAR(255) NULL     DEFAULT NULL,
  `zip_code`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `credentials`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `credentials` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `password`           VARCHAR(255) NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `username`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`username`)
);

-- -----------------------------------------------------
-- Table `credentials_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `credentials_roles` (
  `credentials_id` VARCHAR(255) NOT NULL,
  `role`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`credentials_id`, `role`),
  FOREIGN KEY (`credentials_id`) REFERENCES `credentials` (`id`)
);


-- -----------------------------------------------------
-- Table `deterlab_project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `deterlab_project` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `deter_project_id`   VARCHAR(255) NOT NULL,
  `ncl_team_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`deter_project_id`),
  UNIQUE (`ncl_team_id`)
);

-- -----------------------------------------------------
-- Table `deterlab_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `deterlab_user` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `deter_user_id`      VARCHAR(255) NOT NULL,
  `ncl_user_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`deter_user_id`),
  UNIQUE (`ncl_user_id`)
);

-- -----------------------------------------------------
-- Table `email_retries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `email_retries` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `bcc`                TINYBLOB     NULL     DEFAULT NULL,
  `cc`                 TINYBLOB     NULL     DEFAULT NULL,
  `content`            VARCHAR(255) NOT NULL,
  `error_message`      VARCHAR(255) NULL     DEFAULT NULL,
  `html`               BIT(1)       NOT NULL,
  `last_retry_time`    TINYBLOB     NOT NULL,
  `recipients`         TINYBLOB     NOT NULL,
  `retry_times`        INT(11)      NOT NULL,
  `sender`             VARCHAR(255) NOT NULL,
  `sent`               BIT(1)       NOT NULL,
  `subject`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `experiments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `experiments` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
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
);

-- -----------------------------------------------------
-- Table `user_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_details` (
  `id`                       BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`             TINYBLOB     NOT NULL,
  `last_modified_date`       TINYBLOB     NOT NULL,
  `version`                  BIGINT  NOT NULL,
  `email`                    VARCHAR(255) NOT NULL,
  `first_name`               VARCHAR(255) NOT NULL,
  `institution`              VARCHAR(255) NOT NULL,
  `institution_abbreviation` VARCHAR(255) NOT NULL,
  `institution_web`          VARCHAR(255) NOT NULL,
  `job_title`                VARCHAR(255) NOT NULL,
  `last_name`                VARCHAR(255) NOT NULL,
  `phone`                    VARCHAR(255) NOT NULL,
  `address_id`               BIGINT   NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`email`),
  FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
);

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `application_date`   TINYBLOB     NOT NULL,
  `is_email_verified`  CHAR(1)      NOT NULL,
  `processed_date`     TINYBLOB     NULL DEFAULT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `verification_key`   VARCHAR(255) NULL DEFAULT NULL,
  `user_details_id`    BIGINT   NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`user_details_id`),
  FOREIGN KEY (`user_details_id`) REFERENCES `user_details` (`id`)
);

-- -----------------------------------------------------
-- Table `login_activities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `login_activities` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `date`               TINYBLOB     NOT NULL,
  `ip_address`         VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

-- -----------------------------------------------------
-- Table `realizations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `realizations` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `details`            LONGTEXT     NULL     DEFAULT NULL,
  `experiment_id`      BIGINT   NOT NULL,
  `experiment_name`    VARCHAR(255) NOT NULL,
  `idle_minutes`       BIGINT   NOT NULL,
  `num_nodes`          INT(11)      NOT NULL,
  `running_minutes`    BIGINT   NOT NULL,
  `state`              VARCHAR(255) NOT NULL,
  `team_id`            VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `registrations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `registrations` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
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
  UNIQUE (`uid`),
  UNIQUE (`usr_email`)
);

-- -----------------------------------------------------
-- Table `teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teams` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `application_date`   TINYBLOB     NOT NULL,
  `description`        VARCHAR(255) NULL DEFAULT NULL,
  `name`               VARCHAR(255) NOT NULL,
  `organisation_type`  VARCHAR(255) NOT NULL,
  `privacy`            VARCHAR(255) NOT NULL,
  `processed_date`     TINYBLOB     NULL DEFAULT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `visibility`         VARCHAR(255) NOT NULL,
  `website`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`name`)
);

-- -----------------------------------------------------
-- Table `team_members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `team_members` (
  `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT   NOT NULL,
  `joined_date`        TINYBLOB     NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `member_type`        VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NOT NULL,
  `team_id`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`team_id`, `user_id`),
  FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
);

-- -----------------------------------------------------
-- Table `users_teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users_teams` (
  `user_id` VARCHAR(255) NOT NULL,
  `team_id` VARCHAR(255) NOT NULL,
  UNIQUE (`user_id`, `team_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);