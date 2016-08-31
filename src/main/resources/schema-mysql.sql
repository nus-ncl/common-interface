--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `addresses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `address_1` varchar(255) NOT NULL,
  `address_2` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `region` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `credentials`
--

DROP TABLE IF EXISTS `credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credentials` (
  `id` varchar(255) NOT NULL,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l7xhygibdj6cgkpj2ih1jgx14` (`username`),
  KEY `IDXl7xhygibdj6cgkpj2ih1jgx14` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deterlab_project`
--

DROP TABLE IF EXISTS `deterlab_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deterlab_project` (
  `id` varchar(255) NOT NULL,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `deter_project_id` varchar(255) NOT NULL,
  `ncl_team_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hh8y4kg284v94a79ygpy31ewb` (`deter_project_id`),
  UNIQUE KEY `UK_hhtgwx7cpbibo46ca82l32oce` (`ncl_team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deterlab_user`
--

DROP TABLE IF EXISTS `deterlab_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deterlab_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `deter_user_id` varchar(255) NOT NULL,
  `ncl_user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nu93l9b4l6t5kcremcgxl6nqv` (`deter_user_id`),
  UNIQUE KEY `UK_i6dsyy2vylkp88w3ttkm1v22g` (`ncl_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `experiments`
--

DROP TABLE IF EXISTS `experiments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `experiments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `idle_swap` int(11) NOT NULL,
  `max_duration` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `ns_file` varchar(255) NOT NULL,
  `ns_file_content` longtext NOT NULL,
  `team_id` varchar(255) NOT NULL,
  `team_name` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `login_activities`
--

DROP TABLE IF EXISTS `login_activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_activities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `date` tinyblob NOT NULL,
  `ip_address` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8dyu3xxtaw62bm46xqi9romha` (`user_id`),
  CONSTRAINT `FK8dyu3xxtaw62bm46xqi9romha` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `realizations`
--

DROP TABLE IF EXISTS `realizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `realizations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `details` longtext,
  `experiment_id` bigint(20) NOT NULL,
  `experiment_name` varchar(255) NOT NULL,
  `idle_minutes` bigint(20) NOT NULL,
  `num_nodes` int(11) NOT NULL,
  `running_minutes` bigint(20) NOT NULL,
  `state` varchar(255) NOT NULL,
  `team_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registration`
--

DROP TABLE IF EXISTS `registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registration` (
  `id` varchar(255) NOT NULL,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `pid` varchar(255) NOT NULL,
  `uid` varchar(255) NOT NULL,
  `usr_addr` varchar(255) NOT NULL,
  `usr_addr2` varchar(255) DEFAULT NULL,
  `usr_affil` varchar(255) NOT NULL,
  `usr_affil_abbrev` varchar(255) NOT NULL,
  `usr_city` varchar(255) NOT NULL,
  `usr_country` varchar(255) NOT NULL,
  `usr_email` varchar(255) NOT NULL,
  `usr_name` varchar(255) NOT NULL,
  `usr_phone` varchar(255) NOT NULL,
  `usr_state` varchar(255) NOT NULL,
  `usr_title` varchar(255) NOT NULL,
  `usr_zip` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_penxqpyfydqs4ig4wog5gwdfo` (`uid`),
  UNIQUE KEY `UK_9chwa0oq7yqw1ru5a4x13nrgs` (`usr_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registrations`
--

DROP TABLE IF EXISTS `registrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registrations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `pid` varchar(255) NOT NULL,
  `uid` varchar(255) NOT NULL,
  `usr_addr` varchar(255) NOT NULL,
  `usr_addr2` varchar(255) DEFAULT NULL,
  `usr_affil` varchar(255) NOT NULL,
  `usr_affil_abbrev` varchar(255) NOT NULL,
  `usr_city` varchar(255) NOT NULL,
  `usr_country` varchar(255) NOT NULL,
  `usr_email` varchar(255) NOT NULL,
  `usr_name` varchar(255) NOT NULL,
  `usr_phone` varchar(255) NOT NULL,
  `usr_state` varchar(255) NOT NULL,
  `usr_title` varchar(255) NOT NULL,
  `usr_zip` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6t1f14auw7in83xccrbi8kef2` (`uid`),
  UNIQUE KEY `UK_i4da24h32w6sf4upvsts7uhog` (`usr_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `team_members`
--

DROP TABLE IF EXISTS `team_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_members` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `joined_date` tinyblob NOT NULL,
  `status` varchar(255) NOT NULL,
  `member_type` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `team_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKs8nuwsa7nvebc246ed822w68x` (`team_id`,`user_id`),
  CONSTRAINT `FKtgca08el3ofisywcf11f0f76t` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5702597833932819209 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teams` (
  `id` varchar(255) NOT NULL,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `application_date` tinyblob NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `organisation_type` varchar(255) NOT NULL,
  `privacy` varchar(255) NOT NULL,
  `processed_date` tinyblob,
  `status` varchar(255) NOT NULL,
  `visibility` varchar(255) NOT NULL,
  `website` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a510no6sjwqcx153yd5sm4jrr` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_details`
--

DROP TABLE IF EXISTS `user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `institution` varchar(255) NOT NULL,
  `institution_abbreviation` varchar(255) NOT NULL,
  `institution_web` varchar(255) NOT NULL,
  `job_title` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `address_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4d9rdl7d52k8x3etihxlaujvh` (`email`),
  KEY `FK3d5b1ii2vbv6fyt2bbijwmlqg` (`address_id`),
  CONSTRAINT `FK3d5b1ii2vbv6fyt2bbijwmlqg` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `created_date` tinyblob NOT NULL,
  `last_modified_date` tinyblob NOT NULL,
  `version` bigint(20) NOT NULL,
  `application_date` tinyblob NOT NULL,
  `is_email_verified` char(1) NOT NULL,
  `processed_date` tinyblob,
  `status` varchar(255) NOT NULL,
  `user_details_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4ai7rrtrvwtgtqavv8okpxrul` (`user_details_id`),
  CONSTRAINT `FK8uj8y5ad4xl01w9wcracimb14` FOREIGN KEY (`user_details_id`) REFERENCES `user_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_roles` (
  `user_id` varchar(255) NOT NULL,
  `roles` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`roles`),
  KEY `IDX1hjw31qvltj7v3wb5o31jsrd8` (`user_id`),
  KEY `IDXakos9oduijg8ig3wqp81bafb3` (`roles`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_teams`
--

DROP TABLE IF EXISTS `users_teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_teams` (
  `user_id` varchar(255) NOT NULL,
  `team_id` varchar(255) NOT NULL,
  UNIQUE KEY `UKbqjpbghgx6qnprt06mi96bv1q` (`user_id`,`team_id`),
  KEY `IDX4fw1eq004xbrrr0lh5e6nhvrv` (`user_id`),
  KEY `IDXook8yqr9dleaw16r7iusof0f9` (`team_id`),
  CONSTRAINT `FK31k9hhkcp7fiugrk2lu7vq9jo` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-31 17:05:03
