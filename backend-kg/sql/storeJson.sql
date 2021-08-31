SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `storejson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storejson` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fileId` int(11) DEFAULT NULL,
  `json` json DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `files`;
CREATE TABLE `files`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `file_name` varchar(255) DEFAULT NULL,
    `userId` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `searchrecord`;
CREATE TABLE `searchrecord`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `fileId` int(11) DEFAULT NULL,
    `record` json DEFAULT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
                        `uid` int(11) NOT NULL AUTO_INCREMENT,
                        `phone` varchar(255) DEFAULT NULL ,
                        `name` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`uid`)

                    /*AUTO_INCREMENT*/
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

BEGIN;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'12345678901','software','123456'),
                          (2,'12345678902','engineering','123456');

COMMIT;
