-- -----------------------------------------------------
-- Table `prod`.`project_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`project_details` (
  `id`                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `organisation_type`  VARCHAR(255) NOT NULL,
  `organisation_name`  VARCHAR(255) NOT NULL,
  `project_name`       VARCHAR(255) NOT NULL UNIQUE,
  `owner`              VARCHAR(255) NOT NULL,
  `date_created`       DATETIME     NOT NULL,
  `is_education`       CHAR(1)      NOT NULL DEFAULT 'N',
  `is_service_tool`    CHAR(1)      NOT NULL DEFAULT 'N',
  `supported_by`       TEXT         NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;

-- -----------------------------------------------------
-- Table `prod`.`project_usage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `prod`.`project_usage` (
  `created_date`       DATETIME     NOT NULL,
  `last_modified_date` DATETIME     NOT NULL,
  `version`            BIGINT(20)   NOT NULL,
  `project_details_id` BIGINT(20)   NOT NULL,
  `month_year`         VARCHAR(255) NOT NULL,
  `monthly_usage`      INT          NOT NULL,
  PRIMARY KEY (`project_details_id`, `month_year`),
  CONSTRAINT project_usage_project_details_id_fk
  FOREIGN KEY (`project_details_id`) REFERENCES project_details (`id`)
)
  ENGINE = InnoDB
  /*!  DEFAULT CHARACTER SET = latin1 */;
