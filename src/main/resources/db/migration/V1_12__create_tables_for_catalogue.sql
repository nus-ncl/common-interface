-- -----------------------------------------------------
-- Table `prod`.`data_keywords`
-- Keywords that describes a dataset.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_keywords` (
  `data_id`    BIGINT(20)    NOT NULL,
  `keyword`    VARCHAR(255)  NOT NULL,
  PRIMARY KEY (`data_id`, `keyword`),
  INDEX `IDXcc8154a8589506af091ad380ffa8ae31` (`data_id` ASC),
  CONSTRAINT `FKa565e755709166653ad81ac92b39cc15`
  FOREIGN KEY (`data_id`)
  REFERENCES `prod`.`data` (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `prod`.`data_categories`
-- Categories that a dataset can belongs to.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`data_categories` (
  `id`       	         BIGINT(20)	  NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `name`               VARCHAR(255)	NOT NULL,
  `description`        LONGTEXT	    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- TABLE `prod`.`data`
-- Update dataset to have a category.
-- -----------------------------------------------------
ALTER TABLE `prod`.`data`
  ADD COLUMN `category_id` BIGINT(20) NOT NULL AFTER `contributor_id`;

-- -----------------------------------------------------
-- Predefined categories.
-- -----------------------------------------------------
INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Network', 'Datasets related to tranditional computer networks and Internet, such as BGP Routing Data, DNS Data, IDS and Firewall Data, Internet Topology Data, Traffic Flow data etc.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'System', 'Datasets related to tranditional computer systems such as system and application logs, system vulnerabilities and exploits, antivirus logs etc.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Mobile', 'Datasets related to mobile devices and their communications, such as mobile malware, malicious mobile apps, mobile apps store, 2G/3G/4G networks, Android/iOS security.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Web', 'Datasets related to web browsers, web apps and protocols etc., such as web hacking incidents data, Alexa top xxx websites, malicious/phishing websites etc.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Cyber-physical Systems', 'Datasets related to any particular cyber-physical systems such as power grid, urban transport system etc.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Malware and Virus', 'Computer malware, virus, worm and trojans etc.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Cloud and Virtualization', 'Datasets related to cloud computing and server virtulization technologies, such as data leaks across VMs on the same physical host.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Personal Data and Privacy', 'Datasets related to personal privacy such as healthcare data, transaction records etc.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Threat Feeds', 'Data feeds related to cyber threat such as VXvault, Tracker, Malware Domain List, MX Phishing DB etc.');

INSERT INTO `prod`.`data_categories` (created_date, last_modified_date, version, name, description)
  VALUES (now(), now(), 0, 'Other', 'Datasets that cannot be clearly classified into the existing categories, e.g., password frequency dataset.');
