-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: miristock
-- ------------------------------------------------------
-- Server version	8.0.31

# DB create
create database miristock;
use miristock;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `article_no` int NOT NULL AUTO_INCREMENT,
  `member_no` int DEFAULT NULL,
  `member_nickname` varchar(100) DEFAULT NULL,
  `article_title` varchar(100) NOT NULL,
  `article_content` varchar(1000) NOT NULL,
  `article_create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `article_modify_date` timestamp NULL DEFAULT NULL,
  `article_heart_count` int DEFAULT '0',
  PRIMARY KEY (`article_no`),
  KEY `article_ibfk_1` (`member_no`),
  KEY `article_ibfk_2` (`member_nickname`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `article_ibfk_2` FOREIGN KEY (`member_nickname`) REFERENCES `member` (`member_nickname`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES 
(1,1,'닉네임','자신에게 맞는 산업군', '저에게 맞는 산업군을 쉽게 찾는 방법을 알려주세요','2023-02-12 09:11:23',NULL, 0),
(2,2,'닉네임','주식 투자시 좋은 습관을 알려주세요', '주식을 투자할 때 가지면 좋은 습관들이 무엇이 있을까요','2023-02-12 11:11:49',NULL, 0),
(3,3,'닉네임','호가가 무엇인가요', '주식 뉴스에 많이 나오는 내용인데 무슨 용어인지 궁금합니다.','2023-02-13 13:42:49',NULL, 0),
(4,4,'닉네임','종가? 시가?', '종가와 시가는 무엇인가요?','2023-02-13 14:23:22',NULL, 0),
(5,5,'닉네임','매수가 무엇인가요?', '아까 서비스 이용할때 주식 매수라는 옵션이 있는데 무엇인지 궁금합니다.','2023-02-13 18:23:22',NULL, 0),
(6,6,'닉네임','재무재표가 무엇인가요?', '상세주식 정보를 보면 뉴스 아래 재무재표라는게 있는데 무엇인지 궁금해요','2023-02-13 23:12:54',NULL, 0),
(7,7,'닉네임','주식이 무엇인가요?', '주식 한번 해보려고 하는데 주식이 무엇인가요?','2023-02-14 20:12:54',NULL, 0);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_favorite`
--

DROP TABLE IF EXISTS `article_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_favorite` (
  `article_favorite_no` int NOT NULL AUTO_INCREMENT,
  `article_no` int NOT NULL,
  `member_no` int NOT NULL,
  PRIMARY KEY (`article_favorite_no`),
  KEY `article_favorite_to_member_member_no_fk` (`member_no`),
  KEY `article_favorite_to_article_article_no_fk` (`article_no`),
  CONSTRAINT `article_favorite_to_article_article_no_fk` FOREIGN KEY (`article_no`) REFERENCES `article` (`article_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `article_favorite_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_favorite`
--

LOCK TABLES `article_favorite` WRITE;
/*!40000 ALTER TABLE `article_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_no` int NOT NULL AUTO_INCREMENT,
  `article_no` int NOT NULL,
  `member_no` int DEFAULT NULL,
  `member_nickname` varchar(100) DEFAULT NULL,
  `comment_content` varchar(300) NOT NULL,
  `comment_create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comment_modify_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`comment_no`),
  KEY `comment_to_article_article_no_fk` (`article_no`),
  KEY `comment_to_member_member_no_fk` (`member_no`),
  KEY `comment_ibfk_1` (`member_nickname`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`member_nickname`) REFERENCES `member` (`member_nickname`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `comment_to_article_article_no_fk` FOREIGN KEY (`article_no`) REFERENCES `article` (`article_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES 
(1,1,1,'닉네임','내가 잘 알고, 자주 소비하며, 좋아하는 분야의 산업과 기업을 중심으로 관심을 가지고 투자를 시작해 보세요', '2023-02-12 10:02:23', NULL),
(2,1,1,'닉네임','내가 평소에 잘 아는 분야나 좋아하는 분야에 속한 기업들을 공부해 보면 쉽게 찾을 수 있을 것 같아요!', '2023-02-12 10:05:23', NULL),
(3,2,1,'닉네임','본인만의 원칙을 정하고 투자를 하는 것이 좋아요', '2023-02-12 12:02:23', NULL),
(4,2,1,'닉네임','너무 주식에만 매몰되지말고 일상생활에 집중하는 것도 좋아요. 주식은 단순히 부업으로 생각하고 본업에 충실하는 것이 좋아요', '2023-02-12 14:02:23', NULL),
(5,3,1,'닉네임','주식 매매를 위해 누군가가 사거나 팔기 위해 제시하는 가격이요', '2023-02-13 16:02:23', NULL),
(6,3,1,'닉네임','시가는 주식거래 중 하루의 시작 가격으로 보통 9시를 기준으로 시가를 말해요', '2023-02-13 17:02:23', NULL),
(7,4,1,'닉네임','종가는 시가와 반대로 주식거래 중 하루의 마지막 가격으로 보통 3시 30분을 기준으로 한답니다', '2023-02-13 15:53:22', NULL),
(8,5,1,'닉네임','매수는 주식을 사는 것을 뜻합니다', '2023-02-13 22:23:22', NULL),
(9,6,1,'닉네임','기업의 재무상태나 경영성과 등을 보여주는 문서요', '2023-02-14 01:45:54', NULL),
(10,6,1,'닉네임','회사의 경영 활동을 숫자로 나타낸 요약표 라고 생각하시면 좋겠습니다', '2023-02-14 04:12:54', NULL),
(11,6,1,'닉네임','일정 회계 기간 동안 회사의 경영 성과와 동 기간 말의 재무 상태 등에 회계 정보를 주주, 채권자와 같은 이해 관계자에게 보고하는 각종 보고서 입니다~~', '2023-02-14 08:12:54', NULL),
(12,7,1,'닉네임','주식회사의 자본을 구성하는 단위입니다', '2023-02-14 22:12:54', NULL),
(13,7,1,'닉네임','주식회사가 자금 조달을 위해 발행하는 증서를 뜻해요', '2023-02-14 23:12:54', NULL);

/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interest`
--

DROP TABLE IF EXISTS `interest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interest` (
  `interest_no` int NOT NULL AUTO_INCREMENT,
  `stock_code` char(6) DEFAULT NULL,
  `member_no` int DEFAULT NULL,
  PRIMARY KEY (`interest_no`),
  KEY `interest_to_stock_stock_code_fk` (`stock_code`),
  KEY `interest_to_member_member_no_fk` (`member_no`),
  CONSTRAINT `interest_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `interest_to_stock_stock_code_fk` FOREIGN KEY (`stock_code`) REFERENCES `stock` (`stock_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interest`
--

LOCK TABLES `interest` WRITE;
/*!40000 ALTER TABLE `interest` DISABLE KEYS */;
/*!40000 ALTER TABLE `interest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `limitpriceorder`
--

DROP TABLE IF EXISTS `limitpriceorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `limitpriceorder` (
  `limitpriceorder_no` int NOT NULL AUTO_INCREMENT,
  `stock_code` varchar(6) NOT NULL,
  `stock_name` varchar(40) NOT NULL,
  `member_no` int NOT NULL,
  `limitpriceorder_price` int NOT NULL,
  `limitpriceorder_amount` bigint NOT NULL,
  `limitpriceorder_type` varchar(10) NOT NULL,
  PRIMARY KEY (`limitpriceorder_no`),
  KEY `limitpriceorder_to_stock_stock_code_fk` (`stock_code`),
  KEY `limitpriceorder_to_member_member_no_fk` (`member_no`),
  CONSTRAINT `limitpriceorder_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `limitpriceorder_to_stock_stock_code_fk` FOREIGN KEY (`stock_code`) REFERENCES `stock` (`stock_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `limitpriceorder`
--

LOCK TABLES `limitpriceorder` WRITE;
/*!40000 ALTER TABLE `limitpriceorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `limitpriceorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_no` int NOT NULL AUTO_INCREMENT,
  `member_email` varchar(320) NOT NULL,
  `member_nickname` varchar(20) DEFAULT NULL,
  `ROLE` varchar(10) DEFAULT NULL,
  `provider` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`member_no`),
  UNIQUE KEY `member_nickname_UNIQUE` (`member_nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `memberasset`
--

DROP TABLE IF EXISTS `memberasset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `memberasset` (
  `memberasset_no` int NOT NULL AUTO_INCREMENT,
  `member_no` int NOT NULL,
  `memberasset_total_asset` bigint NOT NULL DEFAULT '100000000',
  `memberasset_available_asset` bigint NOT NULL DEFAULT '100000000',
  `memberasset_stock_asset` bigint NOT NULL DEFAULT '0',
  `memberasset_current_time` int NOT NULL DEFAULT '20180102',
  `memberasset_last_total_asset` bigint DEFAULT NULL,
  PRIMARY KEY (`memberasset_no`),
  UNIQUE KEY `member_no_UNIQUE` (`member_no`),
  KEY `memberasset_to_member_member_no_fk` (`member_no`),
  CONSTRAINT `memberasset_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `memberstock`
--

DROP TABLE IF EXISTS `memberstock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `memberstock` (
  `memberstock_no` int NOT NULL AUTO_INCREMENT,
  `stock_code` varchar(6) NOT NULL,
  `stock_name` varchar(40) NOT NULL,
  `member_no` int NOT NULL,
  `memberstock_amount` bigint NOT NULL,
  `memberstock_avgprice` int NOT NULL,
  `memberstock_acc_purchaseprice` bigint NOT NULL,
  `memberstock_acc_sellprice` bigint DEFAULT '0',
  `memberstock_acc_earnrate` float DEFAULT '0',
  PRIMARY KEY (`memberstock_no`),
  KEY `memberstock_to_stock_stock_code_fk` (`stock_code`),
  KEY `memberstock_to_member_member_no_fk` (`member_no`),
  CONSTRAINT `memberstock_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `memberstock_to_stock_stock_code_fk` FOREIGN KEY (`stock_code`) REFERENCES `stock` (`stock_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `memberstock`
--

LOCK TABLES `memberstock` WRITE;
/*!40000 ALTER TABLE `memberstock` DISABLE KEYS */;
/*!40000 ALTER TABLE `memberstock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search`
--

DROP TABLE IF EXISTS `search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `search` (
  `search_no` int NOT NULL AUTO_INCREMENT,
  `member_no` int NOT NULL,
  `stock_code` char(6) NOT NULL,
  `stock_name` varchar(40) NOT NULL,
  PRIMARY KEY (`search_no`),
  KEY `search_to_member_member_no_fk` (`member_no`),
  CONSTRAINT `search_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search`
--

LOCK TABLES `search` WRITE;
/*!40000 ALTER TABLE `search` DISABLE KEYS */;
/*!40000 ALTER TABLE `search` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockdeal`
--

DROP TABLE IF EXISTS `stockdeal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stockdeal` (
  `stockdeal_no` int NOT NULL AUTO_INCREMENT,
  `stock_code` varchar(6) NOT NULL,
  `stock_name` varchar(40) NOT NULL,
  `member_no` int NOT NULL,
  `stockdeal_date` int NOT NULL,
  `stockdeal_order_closing_price` bigint DEFAULT NULL,
  `stockdeal_avg_closing_price` bigint DEFAULT NULL,
  `stockdeal_amount` int DEFAULT NULL,
  `stockdeal_type` varchar(10) DEFAULT NULL,
  `stockdeal_earn_rate` float DEFAULT NULL,
  `stockdeal_earn_price` bigint DEFAULT NULL,
  PRIMARY KEY (`stockdeal_no`),
  KEY `stockdeal_to_stock_stock_code_fk` (`stock_code`),
  KEY `stockdeal_to_member_member_no_fk` (`member_no`),
  CONSTRAINT `stockdeal_to_member_member_no_fk` FOREIGN KEY (`member_no`) REFERENCES `member` (`member_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stockdeal_to_stock_stock_code_fk` FOREIGN KEY (`stock_code`) REFERENCES `stock` (`stock_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockdeal`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
   `stock_code` char(6) NOT NULL,
   `stock_name` varchar(40) NOT NULL,
   PRIMARY KEY (`stock_code`),
   UNIQUE KEY `stock_name_UNIQUE` (`stock_name`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `financialstatement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financialstatement` (
   `financialstatement_no` int NOT NULL AUTO_INCREMENT,
   `stock_code` char(6) DEFAULT NULL,
   `net_income` bigint NOT NULL,
   `year` int NOT NULL,
   `sales_revenue` bigint NOT NULL,
   `operating_profit` bigint NOT NULL,
   PRIMARY KEY (`financialstatement_no`),
   KEY `financialstatement_to_stock_stock_code_fk` (`stock_code`),
   CONSTRAINT `financialstatement_to_stock_stock_code_fk` FOREIGN KEY (`stock_code`) REFERENCES `stock` (`stock_code`) ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE=InnoDB AUTO_INCREMENT=13389 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `stockdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stockdata` (
   `stockdata_no` int NOT NULL,
   `stock_code` char(6) NOT NULL,
   `stock_name` varchar(50) NOT NULL,
   `stockdata_date` int NOT NULL,
   `stockdata_closing_price` int NOT NULL,
   `stockdata_amount` bigint NOT NULL,
   `stockdata_price_increasement` int NOT NULL,
   `stockdata_flucauation_rate` float DEFAULT NULL,
   PRIMARY KEY (`stockdata_no`),
   KEY `stockdata_to_stock_stock_code_fk` (`stock_code`),
   KEY `stockdata_to_stock_stock_name_fk` (`stock_name`),
   KEY `IDX_stockdata_date_stock_code` (`stockdata_date`,`stock_code`),
   KEY `IDX_stock_code_stockdata_date` (`stock_code`,`stockdata_date`),
   CONSTRAINT `stockdata_to_stock_stock_name_fk` FOREIGN KEY (`stock_name`) REFERENCES `stock` (`stock_name`) ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




