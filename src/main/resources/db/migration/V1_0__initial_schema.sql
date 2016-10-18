CREATE SCHEMA IF NOT EXISTS `prod`;

USE `prod`;

-- -----------------------------------------------------
-- Table `addresses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `addresses` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
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
/*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `credentials`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `credentials` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `password`           VARCHAR(255) NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `username`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_l7xhygibdj6cgkpj2ih1jgx14` (`username` ASC),
  INDEX `IDXl7xhygibdj6cgkpj2ih1jgx14` (`username` ASC)
)
  ENGINE = InnoDB
/*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `credentials_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `credentials_roles` (
  `credentials_id` VARCHAR(255) NOT NULL,
  `role`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`credentials_id`, `role`),
  CONSTRAINT `FKguvv5t3qk4mwhefyca0l0bj3y`
  FOREIGN KEY (`credentials_id`)
  REFERENCES `credentials` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `deterlab_project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `deterlab_project` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `deter_project_id`   VARCHAR(255) NOT NULL,
  `ncl_team_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_hh8y4kg284v94a79ygpy31ewb` (`deter_project_id` ASC),
  UNIQUE INDEX `UK_hhtgwx7cpbibo46ca82l32oce` (`ncl_team_id` ASC)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `deterlab_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `deterlab_user` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `deter_user_id`      VARCHAR(255) NOT NULL,
  `ncl_user_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_nu93l9b4l6t5kcremcgxl6nqv` (`deter_user_id` ASC),
  UNIQUE INDEX `UK_i6dsyy2vylkp88w3ttkm1v22g` (`ncl_user_id` ASC)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `email_retries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `email_retries` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
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
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `experiments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `experiments` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
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
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `user_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_details` (
  `id`                       BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`             TINYBLOB     NOT NULL,
  `last_modified_date`       TINYBLOB     NOT NULL,
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
  REFERENCES `addresses` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `application_date`   TINYBLOB     NOT NULL,
  `is_email_verified`  CHAR(1)      NOT NULL,
  `processed_date`     TINYBLOB     NULL DEFAULT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `verification_key`   VARCHAR(255) NULL DEFAULT NULL,
  `user_details_id`    BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_4ai7rrtrvwtgtqavv8okpxrul` (`user_details_id` ASC),
  CONSTRAINT `FK8uj8y5ad4xl01w9wcracimb14`
  FOREIGN KEY (`user_details_id`)
  REFERENCES `user_details` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `login_activities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `login_activities` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `date`               TINYBLOB     NOT NULL,
  `ip_address`         VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK8dyu3xxtaw62bm46xqi9romha` (`user_id` ASC),
  CONSTRAINT `FK8dyu3xxtaw62bm46xqi9romha`
  FOREIGN KEY (`user_id`)
  REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `realizations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `realizations` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
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
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `registrations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `registrations` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
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
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teams` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
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
  UNIQUE INDEX `UK_a510no6sjwqcx153yd5sm4jrr` (`name` ASC)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `team_members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `team_members` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `joined_date`        TINYBLOB     NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `member_type`        VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255) NOT NULL,
  `team_id`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKs8nuwsa7nvebc246ed822w68x` (`team_id` ASC, `user_id` ASC),
  CONSTRAINT `FKtgca08el3ofisywcf11f0f76t`
  FOREIGN KEY (`team_id`)
  REFERENCES `teams` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `users_teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users_teams` (
  `user_id` VARCHAR(255) NOT NULL,
  `team_id` VARCHAR(255) NOT NULL,
  UNIQUE INDEX `UKbqjpbghgx6qnprt06mi96bv1q` (`user_id` ASC, `team_id` ASC),
  INDEX `IDX4fw1eq004xbrrr0lh5e6nhvrv` (`user_id` ASC),
  INDEX `IDXook8yqr9dleaw16r7iusof0f9` (`team_id` ASC),
  CONSTRAINT `FK31k9hhkcp7fiugrk2lu7vq9jo`
  FOREIGN KEY (`user_id`)
  REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;
