--
-- Table structure for table `addresses`
--
CREATE TABLE IF NOT EXISTS `addresses` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `address_1`          VARCHAR(255) NOT NULL,
  `address_2`          VARCHAR(255)          DEFAULT NULL,
  `city`               VARCHAR(255) NOT NULL,
  `country`            VARCHAR(255) NOT NULL,
  `region`             VARCHAR(255)          DEFAULT NULL,
  `zip_code`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `credentials`
--
CREATE TABLE IF NOT EXISTS `credentials` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `password`           VARCHAR(255) NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  `username`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l7xhygibdj6cgkpj2ih1jgx14` (`username`),
  KEY `IDXl7xhygibdj6cgkpj2ih1jgx14` (`username`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `credentials_roles`
--

CREATE TABLE IF NOT EXISTS `credentials_roles` (
  `credentials_id` VARCHAR(255) NOT NULL,
  `role`           VARCHAR(255) NOT NULL,
  PRIMARY KEY (`credentials_id`, `role`),
  CONSTRAINT `FKguvv5t3qk4mwhefyca0l0bj3y` FOREIGN KEY (`credentials_id`) REFERENCES `credentials` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `deterlab_project`
--
CREATE TABLE IF NOT EXISTS `deterlab_project` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `deter_project_id`   VARCHAR(255) NOT NULL,
  `ncl_team_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hh8y4kg284v94a79ygpy31ewb` (`deter_project_id`),
  UNIQUE KEY `UK_hhtgwx7cpbibo46ca82l32oce` (`ncl_team_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `deterlab_user`
--
CREATE TABLE IF NOT EXISTS `deterlab_user` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `deter_user_id`      VARCHAR(255) NOT NULL,
  `ncl_user_id`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nu93l9b4l6t5kcremcgxl6nqv` (`deter_user_id`),
  UNIQUE KEY `UK_i6dsyy2vylkp88w3ttkm1v22g` (`ncl_user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `experiments`
--
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
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `realizations`
--
CREATE TABLE IF NOT EXISTS `realizations` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `details`            LONGTEXT,
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
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `registrations`
--
CREATE TABLE IF NOT EXISTS `registrations` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `pid`                VARCHAR(255) NOT NULL,
  `uid`                VARCHAR(255) NOT NULL,
  `usr_addr`           VARCHAR(255) NOT NULL,
  `usr_addr2`          VARCHAR(255)          DEFAULT NULL,
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
  UNIQUE KEY `UK_6t1f14auw7in83xccrbi8kef2` (`uid`),
  UNIQUE KEY `UK_i4da24h32w6sf4upvsts7uhog` (`usr_email`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `teams`
--
CREATE TABLE IF NOT EXISTS `teams` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `application_date`   TINYBLOB     NOT NULL,
  `description`        VARCHAR(255) DEFAULT NULL,
  `name`               VARCHAR(255) NOT NULL,
  `organisation_type`  VARCHAR(255) NOT NULL,
  `privacy`            VARCHAR(255) NOT NULL,
  `processed_date`     TINYBLOB,
  `status`             VARCHAR(255) NOT NULL,
  `visibility`         VARCHAR(255) NOT NULL,
  `website`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a510no6sjwqcx153yd5sm4jrr` (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `team_members`
--
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
  UNIQUE KEY `UKs8nuwsa7nvebc246ed822w68x` (`team_id`, `user_id`),
  CONSTRAINT `FKtgca08el3ofisywcf11f0f76t` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;


--
-- Table structure for table `user_details`
--
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
  UNIQUE KEY `UK4d9rdl7d52k8x3etihxlaujvh` (`email`),
  KEY `FK3d5b1ii2vbv6fyt2bbijwmlqg` (`address_id`),
  CONSTRAINT `FK3d5b1ii2vbv6fyt2bbijwmlqg` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `users`
--
CREATE TABLE IF NOT EXISTS `users` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `application_date`   TINYBLOB     NOT NULL,
  `is_email_verified`  CHAR(1)      NOT NULL,
  `processed_date`     TINYBLOB,
  `status`             VARCHAR(255) NOT NULL,
  `verification_key`   VARCHAR(255) DEFAULT NULL,
  `user_details_id`    BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4ai7rrtrvwtgtqavv8okpxrul` (`user_details_id`),
  CONSTRAINT `FK8uj8y5ad4xl01w9wcracimb14` FOREIGN KEY (`user_details_id`) REFERENCES `user_details` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `users_teams`
--
CREATE TABLE IF NOT EXISTS `users_teams` (
  `user_id` VARCHAR(255) NOT NULL,
  `team_id` VARCHAR(255) NOT NULL,
  UNIQUE KEY `UKbqjpbghgx6qnprt06mi96bv1q` (`user_id`, `team_id`),
  KEY `IDX4fw1eq004xbrrr0lh5e6nhvrv` (`user_id`),
  KEY `IDXook8yqr9dleaw16r7iusof0f9` (`team_id`),
  CONSTRAINT `FK31k9hhkcp7fiugrk2lu7vq9jo` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `login_activities`
--
CREATE TABLE IF NOT EXISTS `login_activities` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `date`               TINYBLOB     NOT NULL,
  `ip_address`         VARCHAR(255) NOT NULL,
  `user_id`            VARCHAR(255)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8dyu3xxtaw62bm46xqi9romha` (`user_id`),
  CONSTRAINT `FK8dyu3xxtaw62bm46xqi9romha` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `datasets`
--
CREATE TABLE IF NOT EXISTS `datasets` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `name`               VARCHAR(255) NOT NULL,
  `description`        VARCHAR(255) DEFAULT NULL,
  `owner_id`           VARCHAR(255) NOT NULL,
  `accessibility`      VARCHAR(255) NOT NULL,
  `visibility`         VARCHAR(255) NOT NULL,
  `status`             VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c510no1sjcdcx153yd5sm6grr` (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `dataset_resources`
--
CREATE TABLE IF NOT EXISTS `dataset_resources` (
  `id`                 VARCHAR(255) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `link`               VARCHAR(255) NOT NULL,
  `type`               VARCHAR(255) NOT NULL,
  `dataset_id`         VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK1dtx7xxtaw62bmngcki9romha` FOREIGN KEY (`dataset_id`) REFERENCES `datasets` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `users_datasets`
--
CREATE TABLE IF NOT EXISTS `users_datasets` (
  `user_id`            VARCHAR(255) NOT NULL,
  `dataset_id`         VARCHAR(255) NOT NULL,
  UNIQUE KEY `UKbqnkflrns6qnprt06mi87n4q` (`user_id`, `dataset_id`),
  KEY `IDXjuy4nc2m8xbrrr0lh184fvvrv` (`user_id`),
  KEY `IDXm885vf39dleaw16r7iusof0f9` (`dataset_id`),
  CONSTRAINT `FK31k9hhk14b7mxhfk2lu7vq9jo` FOREIGN KEY (`dataset_id`) REFERENCES `datasets` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

--
-- Table structure for table `email_retries`
--
CREATE TABLE IF NOT EXISTS `email_retries` (
  `id`                 BIGINT(20) NOT NULL,
  `created_date`       TINYBLOB     NOT NULL,
  `last_modified_date` TINYBLOB     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `sender`             VARCHAR(255) NOT NULL,
  `recipients`         TINYBLOB     NOT NULL,
  `cc`                 TINYBLOB     DEFAULT NULL,
  `bcc`                TINYBLOB     DEFAULT NULL,
  `subject`            VARCHAR(255) NOT NULL,
  `content`            VARCHAR(255) NOT NULL,
  `html`               BOOLEAN      NOT NULL,
  `retry_times`        INT(11)      NOT NULL,
  `last_retry_time`    TINYBLOB     NOT NULL,
  `error_message`      VARCHAR(255) NOT NULL,
  `sent`               BOOLEAN      NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;