/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Application`
--

DROP TABLE IF EXISTS `Application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Application` (
  `AppKey` varchar(10) NOT NULL,
  `OrgKey` varchar(25) NOT NULL,
  `Description` varchar(155) DEFAULT NULL,
  `URL` varchar(500) DEFAULT NULL,
  `EmailDefaultDomain` varchar(45) DEFAULT NULL,
  `IsDefaultApp` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`AppKey`),
  KEY `FK_COMPANY_KEY_idx` (`OrgKey`),
  CONSTRAINT `FK_ORGKEY` FOREIGN KEY (`OrgKey`) REFERENCES `Organization` (`OrgKey`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Haxor`
--

DROP TABLE IF EXISTS `Haxor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Haxor` (
  `SourceIP` varchar(255) NOT NULL,
  `WhiteList` tinyint(4) DEFAULT '0',
  `Created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `Country` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SourceIP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Organization`
--

DROP TABLE IF EXISTS `Organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Organization` (
  `OrgKey` varchar(25) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `OwnerEmail` varchar(255) DEFAULT NULL,
  `IsDefault` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`OrgKey`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ReferralHistory`
--

DROP TABLE IF EXISTS `ReferralHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReferralHistory` (
  `AppKey` varchar(45) NOT NULL,
  `MappedMilliseconds` bigint(20) NOT NULL,
  `MappedTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `OriginalURL` varchar(255) DEFAULT NULL,
  `ReferringURL` varchar(255) DEFAULT NULL,
  `ResultURL` varchar(255) DEFAULT NULL,
  `UserName` varchar(255) DEFAULT NULL,
  `SourceIP` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`AppKey`,`MappedMilliseconds`),
  KEY `FK_Application_idx` (`AppKey`),
  CONSTRAINT `FK_Application` FOREIGN KEY (`AppKey`) REFERENCES `Application` (`AppKey`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;