CREATE DATABASE  IF NOT EXISTS `bsma` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bsma`;
-- MySQL dump 10.13  Distrib 5.5.37, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: bsma
-- ------------------------------------------------------
-- Server version	5.5.37-0ubuntu0.13.10.1

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
-- Table structure for table `JobInfo`
--

DROP TABLE IF EXISTS `JobInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JobInfo` (
  `jobid` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `dbImpl` tinyint(4) DEFAULT NULL,
  `custDbImpl` varchar(100) DEFAULT NULL,
  `props` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QueryFinalReport`
--

DROP TABLE IF EXISTS `QueryFinalReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QueryFinalReport` (
  `idQueryFinalReport` int(11) NOT NULL,
  `queryid` int(11) DEFAULT NULL,
  `latency50` int(11) DEFAULT NULL,
  `latency75` int(11) DEFAULT NULL,
  `latency95` int(11) DEFAULT NULL,
  `latency99` int(11) DEFAULT NULL,
  `avglatency` float DEFAULT NULL,
  `minlatency` float DEFAULT NULL,
  `maxlatency` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idQueryFinalReport`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QueryRunningReport`
--

DROP TABLE IF EXISTS `QueryRunningReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QueryRunningReport` (
  `subjobid` int(11) NOT NULL,
  `queryid` tinyint(4) NOT NULL,
  `time` int(11) NOT NULL,
  `avglatency` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Rel_Job_Query`
--

DROP TABLE IF EXISTS `Rel_Job_Query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rel_Job_Query` (
  `jobid` int(11) NOT NULL,
  `queryid` int(11) NOT NULL,
  `fraction` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Rel_Job_SubJob`
--

DROP TABLE IF EXISTS `Rel_Job_SubJob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rel_Job_SubJob` (
  `jobid` int(11) NOT NULL,
  `subjobid` int(11) NOT NULL,
  `opcount` int(11) NOT NULL,
  `threadnum` int(11) NOT NULL,
  `state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`subjobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RunningReport`
--

DROP TABLE IF EXISTS `RunningReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RunningReport` (
  `subjobid` int(11) NOT NULL,
  `time` int(11) NOT NULL,
  `totalops` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SubJobFinalReport`
--

DROP TABLE IF EXISTS `SubJobFinalReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SubJobFinalReport` (
  `subjobid` int(11) NOT NULL,
  `totalops` int(11) DEFAULT NULL,
  `totaltime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`subjobid`)
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

-- Dump completed on 2014-07-03 20:56:42
