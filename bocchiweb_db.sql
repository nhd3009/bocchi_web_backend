CREATE DATABASE  IF NOT EXISTS `bocchiweb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bocchiweb`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: bocchiweb
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `cart_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (5,2,1,3);
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK64t7ox312pqal3p7fg9o503c2` (`user_id`),
  CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (1,'2025-04-18 13:44:52.062387','user@gmail.com','2025-04-18 13:44:52.062387','user@gmail.com',1),(2,'2025-04-19 16:01:32.631762','test@gmail.com','2025-04-19 16:01:32.631762','test@gmail.com',2);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'2025-04-16 22:42:49.366642','admin@gmail.com','2025-04-16 22:44:21.171486','admin@gmail.com','ChildPorn'),(2,'2025-04-16 22:45:14.731789','admin@gmail.com','2025-04-16 22:45:14.732798','admin@gmail.com','Test'),(4,'2025-04-19 15:52:53.201574','admin@gmail.com','2025-04-19 15:52:53.201574','admin@gmail.com','Laptop'),(5,'2025-04-19 15:53:02.788861','admin@gmail.com','2025-04-19 15:53:02.788861','admin@gmail.com','Smartphone'),(6,'2025-04-19 15:53:08.547709','admin@gmail.com','2025-04-19 15:53:08.547709','admin@gmail.com','Misc');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` decimal(38,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  KEY `FK4q98utpd73imf4yhttm3w0eax` (`product_id`),
  CONSTRAINT `FK4q98utpd73imf4yhttm3w0eax` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (1,999.99,1,1,1),(2,999.99,1,2,1),(3,999.99,1,3,1),(4,999.99,1,4,1),(5,999.99,1,5,1),(6,599.00,2,6,3),(7,599.00,1,7,3),(8,299.00,2,7,5);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `order_date` datetime(6) DEFAULT NULL,
  `recipient_name` varchar(255) DEFAULT NULL,
  `recipient_phone` varchar(255) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED','PENDING','PROCESSING','SHIPPED') DEFAULT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2025-04-18 16:37:55.481660','user@gmail.com','2025-04-19 15:05:18.607869','admin@gmail.com','2025-04-18 16:37:55.424169','User','0987654321','User Address','PROCESSING',NULL,1),(2,'2025-04-18 16:56:11.307415','user@gmail.com','2025-04-19 15:02:07.808555','user@gmail.com','2025-04-18 16:56:11.303353','User2','0123456789','User2 Address','CANCELLED',999.99,1),(3,'2025-04-18 17:39:07.394447','user@gmail.com','2025-04-18 17:39:07.459780','user@gmail.com','2025-04-18 17:39:07.392455','User1','0987654321','User1 Address','PENDING',999.99,1),(4,'2025-04-18 17:42:27.973820','user@gmail.com','2025-04-18 17:42:27.973820','user@gmail.com','2025-04-18 17:42:27.944817','User1','0987654321','User1 Address','PENDING',NULL,1),(5,'2025-04-18 17:51:47.919904','user@gmail.com','2025-04-19 16:15:48.531542','admin@gmail.com','2025-04-18 17:51:47.877906','User1','0987654321','User1 Address','PROCESSING',999.99,1),(6,'2025-04-19 16:07:27.774870','test@gmail.com','2025-04-19 16:13:43.679034','admin@gmail.com','2025-04-19 16:07:27.772865','Test','0987876523','Test Address','PROCESSING',1198.00,2),(7,'2025-04-19 16:09:51.618414','test@gmail.com','2025-04-19 16:12:24.057105','admin@gmail.com','2025-04-19 16:09:51.617415','Test1','0987876523','Test1 Address','PROCESSING',1197.00,2);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'2025-04-17 20:00:22.552764','admin@gmail.com','2025-04-17 20:52:45.263924','admin@gmail.com','A new iporn','/uploads/products/6e3dbbdc-7f82-427f-ba5f-35548b1f25fb_433090703_429271072942198_7131943522042142996_n.jpg','Iphone 15 Pro',999.99,10,2),(3,'2025-04-19 15:55:16.025433','admin@gmail.com','2025-04-19 16:09:51.637536','test@gmail.com','A new Acer Laptop','/uploads/products/4471eefc-d1c0-4031-b30b-500b47284476_433117288_429270979608874_6323544149711194741_n.jpg','Acer Aspire 7 A715',599.00,17,4),(4,'2025-04-19 15:55:56.689694','admin@gmail.com','2025-04-19 15:55:56.689694','admin@gmail.com','A new Asus Smartphone','/uploads/products/0ec04a90-8199-4f4e-8160-90d36495ccb5_433117288_429270979608874_6323544149711194741_n.jpg','Asus Zenfone 10',799.00,10,5),(5,'2025-04-19 15:56:49.352998','admin@gmail.com','2025-04-19 16:09:51.654606','test@gmail.com','A new Product','/uploads/products/a92cb77e-3bb6-4a7f-aed6-7398b235cb17_432748571_429271056275533_48646605254632059_n.jpg','Test Product',299.00,28,2);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `rating` int NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpl51cejpw4gy5swfar8br9ngi` (`product_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpl51cejpw4gy5swfar8br9ngi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ROLE_ADMIN'),(1,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `refresh_token` mediumtext,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'user@gmail.com','$2a$10$dPdrrUVpV7T7Bl2MVVyCc./yR6k/ehVmizEjOJCFG/mebX7V7WdP2','User',1,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImV4cCI6MTc0NTIyODcxOCwiaWF0IjoxNzQ1MTQyMzE4LCJyb2xlcyI6WyJST0xFX1VTRVIiXX0.K6F9F3t1oqu18aoE0naGrL6iWFuBkQPxDK3vs3e-crnmF9Ol83-9E2TLnxEKFvRNRPO6flmnAVzXLF7rSekr7g',NULL,NULL,'2025-04-20 16:45:18.822073','user@gmail.com'),(2,'test@gmail.com','$2a$10$ht2mZkjMyZ.8LqOzVIubBex4/CHBnBoKRj9l9c5GeG2bOFYYqRhmK','Test',1,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTc0NTEzOTYwNiwiaWF0IjoxNzQ1MDUzMjA2LCJyb2xlcyI6WyJST0xFX1VTRVIiXX0.BbmDWsDYPW9NediORWCFlUDHoG6Rby4rRbpABD2zUXvdNUzaZE-pZQf3MVX1cLt4EKlZ14RxO1XKqtehYeuRbQ',NULL,NULL,'2025-04-19 16:00:06.819839','test@gmail.com'),(3,'admin@gmail.com','$2a$10$DiB7fl5iWS4yNhmI.O./.eqSpqwu11Fgb3GDW33rogQlQX5MSTS72','Admin',2,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJleHAiOjE3NDUxMzk1NTksImlhdCI6MTc0NTA1MzE1OSwicm9sZXMiOlsiUk9MRV9BRE1JTiJdfQ.8emrw11DtCXx2pnP3B5WW0HqQ6EJ8gsZS_wmfZvushtEPhHgS9vmuLey5iNm3X60Za6gRztvQj8ofDtNfeQPXw',NULL,NULL,'2025-04-19 15:59:19.639638','admin@gmail.com'),(5,'test2@gmail.com','$2a$10$LIKAycFpgxz0pf7P20drEuN5Iv2rHT/Z.H9EFP9Z9BNfKD0njeyTO','Test2',1,NULL,NULL,NULL,NULL,NULL),(6,'test3@gmail.com','$2a$10$dziwZbPgMShvlyKBKgmDx.ro7Q07gaVF1n.3nvnu92ji2d2PzZGDG','test33',1,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0M0BnbWFpbC5jb20iLCJleHAiOjE3NDQ4OTg2NzksImlhdCI6MTc0NDgxMjI3OSwicm9sZXMiOlsiUk9MRV9VU0VSIl19.F9YdXvLTBw7svh2NFnYNMuqViHAMxMZJblT2Ar5V_p-ZkDu1kDdsLE5KbRNFvhkQY6hY-2zml8pLe_nl9RtZPw','2025-04-16 21:03:12.498751','anonymousUser','2025-04-16 21:06:10.705353','test3@gmail.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-21 15:16:19
