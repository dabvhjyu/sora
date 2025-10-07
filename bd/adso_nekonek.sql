-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: adso_nekonek
-- ------------------------------------------------------
-- Server version	8.4.6

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
-- Table structure for table `anime`
--

DROP TABLE IF EXISTS `anime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anime` (
  `a_id` int NOT NULL AUTO_INCREMENT,
  `c_id` int NOT NULL,
  `a_episodios` int DEFAULT NULL,
  `a_temporadas` int DEFAULT NULL,
  `a_video` varchar(255) DEFAULT NULL,
  `a_miniatura` varchar(255) DEFAULT NULL,
  `a_estado` int DEFAULT NULL,
  PRIMARY KEY (`a_id`),
  KEY `c_id` (`c_id`),
  KEY `a_estado` (`a_estado`),
  CONSTRAINT `anime_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `contenidos` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `anime_ibfk_2` FOREIGN KEY (`a_estado`) REFERENCES `estados` (`e_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anime`
--

LOCK TABLES `anime` WRITE;
/*!40000 ALTER TABLE `anime` DISABLE KEYS */;
INSERT INTO `anime` VALUES (3,12,NULL,NULL,'uploads/anime/video_12.mp4','uploads/anime/mini_12.png',NULL),(4,17,NULL,NULL,'uploads/anime/video_17.mp4','uploads/anime/mini_17.png',NULL),(5,20,NULL,NULL,'/uploads/anime/video_20.mp4','/uploads/anime/mini_20.png',NULL),(6,22,NULL,NULL,'/uploads/anime/video_22.mp4','/uploads/anime/mini_22.png',NULL),(7,24,NULL,NULL,'/uploads/anime/video_24.mp4','/uploads/anime/mini_24.png',NULL),(28,59,NULL,NULL,'uploads/anime/video_59.mp4','uploads/anime/mini_59.png',NULL),(29,60,NULL,NULL,'uploads/anime/video_60.mp4','uploads/anime/mini_60.png',NULL),(31,62,NULL,NULL,'uploads/anime/video_62.mp4','uploads/anime/mini_62.png',NULL);
/*!40000 ALTER TABLE `anime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenidos`
--

DROP TABLE IF EXISTS `contenidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenidos` (
  `c_id` int NOT NULL AUTO_INCREMENT,
  `s_id` int NOT NULL,
  `u_id` int NOT NULL,
  `t_id` int NOT NULL,
  `c_titulo` varchar(255) NOT NULL,
  `c_descripcion` text,
  `c_completo` tinyint(1) DEFAULT '0',
  `fecha_subida` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`c_id`),
  KEY `idx_contenidos_serie` (`s_id`),
  KEY `idx_contenidos_usuario` (`u_id`),
  KEY `fk_contenidos_tipo` (`t_id`),
  CONSTRAINT `contenidos_ibfk_1` FOREIGN KEY (`s_id`) REFERENCES `series` (`s_id`) ON DELETE CASCADE,
  CONSTRAINT `contenidos_ibfk_2` FOREIGN KEY (`u_id`) REFERENCES `usuarios` (`u_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_contenidos_tipo` FOREIGN KEY (`t_id`) REFERENCES `tipos` (`t_id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenidos`
--

LOCK TABLES `contenidos` WRITE;
/*!40000 ALTER TABLE `contenidos` DISABLE KEYS */;
INSERT INTO `contenidos` VALUES (9,20,6,3,'volumen 1','el inicio',1,'2025-09-18 18:27:56'),(10,20,6,3,'un','sssss',1,'2025-09-18 19:23:13'),(11,20,6,3,'esto debe mostrarse','una descripcion',1,'2025-09-18 19:25:57'),(12,19,9,1,'un anime','un anime',0,'2025-09-18 19:53:45'),(13,20,9,3,'balon de oro','balon de oro',1,'2025-09-22 20:40:44'),(14,19,9,2,'un manga','un manga',0,'2025-09-22 20:44:01'),(15,20,9,3,'ina serie','una descripcion',3,'2025-09-22 21:28:51'),(16,20,9,3,'contenido prueba','descripcion',1,'2025-09-22 21:31:41'),(17,19,9,1,'dddddd','ddddddd',0,'2025-09-22 21:32:47'),(18,20,9,3,'dddddddd','ddddddddd',3,'2025-09-22 21:37:11'),(19,20,9,3,'dddddd','dddddd',3,'2025-09-22 21:39:01'),(20,19,9,1,'eeeeeeeee','eeeeeeeeeeeee',0,'2025-09-22 21:55:27'),(21,20,9,3,'wwwwwwwwwwww','wwwwwwwwwwwwwwwww',1,'2025-09-22 22:08:57'),(22,19,9,1,'ssssssssssss','ssssssssssssssss',0,'2025-09-22 22:09:35'),(23,20,9,3,'dddddddd','ddddddd',3,'2025-09-22 22:23:06'),(24,19,9,1,'qqqqqqqqqqq','qqqqqqqqqq',0,'2025-09-22 22:25:00'),(25,20,9,3,'wwwwwwweeeew','weeeee',4,'2025-09-22 22:39:48'),(26,20,9,3,'eeeeeee','wwwwwwww',3,'2025-09-22 22:41:32'),(59,35,9,1,'una serie','una serie',0,'2025-09-29 02:28:17'),(60,38,9,1,'primer capitulo','primer capitulo',0,'2025-09-29 05:50:30'),(62,40,16,1,'el inicio','primer capitulo',0,'2025-09-29 14:22:45');
/*!40000 ALTER TABLE `contenidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estados`
--

DROP TABLE IF EXISTS `estados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estados` (
  `e_id` int NOT NULL AUTO_INCREMENT,
  `e_nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`e_id`),
  UNIQUE KEY `e_nombre` (`e_nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estados`
--

LOCK TABLES `estados` WRITE;
/*!40000 ALTER TABLE `estados` DISABLE KEYS */;
INSERT INTO `estados` VALUES (3,'Cancelado'),(1,'En Emisión'),(4,'En pausa'),(2,'Finalizado'),(5,'Sin terminar');
/*!40000 ALTER TABLE `estados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `generos`
--

DROP TABLE IF EXISTS `generos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `generos` (
  `g_id` int NOT NULL AUTO_INCREMENT,
  `g_nombre` varchar(30) NOT NULL,
  PRIMARY KEY (`g_id`),
  UNIQUE KEY `g_nombre` (`g_nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `generos`
--

LOCK TABLES `generos` WRITE;
/*!40000 ALTER TABLE `generos` DISABLE KEYS */;
INSERT INTO `generos` VALUES (1,'Acción'),(9,'Aventura'),(6,'Ciencia Ficción'),(3,'Comedia'),(4,'Drama'),(5,'Fantasía'),(8,'Horror'),(7,'Misterio'),(2,'Romance'),(10,'Slice of Life'),(11,'Terror');
/*!40000 ALTER TABLE `generos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manga`
--

DROP TABLE IF EXISTS `manga`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manga` (
  `m_id` int NOT NULL AUTO_INCREMENT,
  `c_id` int NOT NULL,
  `m_capitulos` int DEFAULT NULL,
  `m_imagenes` text,
  `m_volumenes` int DEFAULT NULL,
  `m_estado` int DEFAULT NULL,
  `m_portada` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`m_id`),
  KEY `c_id` (`c_id`),
  KEY `m_estado` (`m_estado`),
  CONSTRAINT `manga_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `contenidos` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `manga_ibfk_2` FOREIGN KEY (`m_estado`) REFERENCES `estados` (`e_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manga`
--

LOCK TABLES `manga` WRITE;
/*!40000 ALTER TABLE `manga` DISABLE KEYS */;
INSERT INTO `manga` VALUES (3,14,NULL,NULL,NULL,NULL,'uploads/manga/portada_14.png');
/*!40000 ALTER TABLE `manga` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `novela`
--

DROP TABLE IF EXISTS `novela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `novela` (
  `n_id` int NOT NULL AUTO_INCREMENT,
  `c_id` int NOT NULL,
  `n_volumenes` int DEFAULT NULL,
  `n_pdf` varchar(255) DEFAULT NULL,
  `n_portada` varchar(255) DEFAULT NULL,
  `n_estado` int DEFAULT NULL,
  PRIMARY KEY (`n_id`),
  KEY `c_id` (`c_id`),
  KEY `n_estado` (`n_estado`),
  CONSTRAINT `novela_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `contenidos` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `novela_ibfk_2` FOREIGN KEY (`n_estado`) REFERENCES `estados` (`e_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `novela`
--

LOCK TABLES `novela` WRITE;
/*!40000 ALTER TABLE `novela` DISABLE KEYS */;
INSERT INTO `novela` VALUES (2,9,NULL,'uploads/novela/novela_9.pdf','uploads/novela/portada_9.png',1),(3,10,NULL,'uploads/novela/novela_10.pdf','uploads/novela/portada_10.png',1),(4,11,NULL,'uploads/novela/novela_11.pdf','uploads/novela/portada_11.png',1),(5,13,NULL,'uploads/novela/novela_13.pdf','uploads/novela/portada_13.png',1),(6,15,NULL,'uploads/novela/novela_15.pdf','uploads/novela/portada_15.png',1),(7,16,NULL,'uploads/novela/novela_16.pdf','uploads/novela/portada_16.png',1),(8,18,NULL,'/uploads/novela/novela_18.pdf','/uploads/novela/portada_18.png',1),(9,19,NULL,'/uploads/novela/novela_19.pdf','/uploads/novela/portada_19.png',1),(10,21,NULL,'/uploads/novelas/novela_21.pdf','/uploads/novelas/portada_21.png',1),(11,23,NULL,'/uploads/novelas/novela_23.pdf','/uploads/novelas/portada_23.png',1),(12,25,NULL,'/uploads/novelas/novela_25.pdf','/uploads/novelas/portada_25.png',1),(13,26,NULL,'/uploads/novelas/novela_26.pdf','/uploads/novelas/portada_26.png',1);
/*!40000 ALTER TABLE `novela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paises`
--

DROP TABLE IF EXISTS `paises`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paises` (
  `p_id` int NOT NULL AUTO_INCREMENT,
  `p_nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`p_id`),
  UNIQUE KEY `p_nombre` (`p_nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paises`
--

LOCK TABLES `paises` WRITE;
/*!40000 ALTER TABLE `paises` DISABLE KEYS */;
INSERT INTO `paises` VALUES (3,'China'),(2,'Corea'),(1,'Japón');
/*!40000 ALTER TABLE `paises` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `r_id` int NOT NULL AUTO_INCREMENT,
  `r_nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `r_nombre` (`r_nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Administrador'),(2,'Usuario Normal');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series`
--

DROP TABLE IF EXISTS `series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `series` (
  `s_id` int NOT NULL AUTO_INCREMENT,
  `u_id` int NOT NULL,
  `s_titulo` varchar(255) NOT NULL,
  `s_descripcion` text,
  `s_cantidadepisodios` int DEFAULT '0',
  `s_pais` int NOT NULL,
  `s_estado` int NOT NULL,
  `s_imagenc` varchar(255) DEFAULT NULL,
  `s_imagenv` varchar(255) DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`s_id`),
  KEY `s_pais` (`s_pais`),
  KEY `s_estado` (`s_estado`),
  KEY `idx_series_usuario` (`u_id`),
  CONSTRAINT `series_ibfk_1` FOREIGN KEY (`u_id`) REFERENCES `usuarios` (`u_id`) ON DELETE CASCADE,
  CONSTRAINT `series_ibfk_3` FOREIGN KEY (`s_pais`) REFERENCES `paises` (`p_id`),
  CONSTRAINT `series_ibfk_4` FOREIGN KEY (`s_estado`) REFERENCES `estados` (`e_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series`
--

LOCK TABLES `series` WRITE;
/*!40000 ALTER TABLE `series` DISABLE KEYS */;
INSERT INTO `series` VALUES (1,8,'prueba','esto es una prueba',0,3,3,'https://i.ytimg.com/vi/pMDofvqezYk/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLBDpf54v_Xc-xJAXr4CVgGaaE9Jyg','https://i.ytimg.com/vi/pMDofvqezYk/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLBDpf54v_Xc-xJAXr4CVgGaaE9Jyg','2025-09-15 02:30:42'),(2,8,'prueba','una descripcion',0,3,3,'https://i.ytimg.com/vi/pMDofvqezYk/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLBDpf54v_Xc-xJAXr4CVgGaaE9Jyg','https://i.ytimg.com/vi/pMDofvqezYk/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLBDpf54v_Xc-xJAXr4CVgGaaE9Jyg','2025-09-15 02:34:32'),(3,8,'prueba','esto es una prueba',0,1,3,'https://img.freepik.com/fotos-premium/adorable-gatito-diciendo-miau-llorar-gato_369656-817.jpg','https://img.freepik.com/fotos-premium/adorable-gatito-diciendo-miau-llorar-gato_369656-817.jpg','2025-09-15 02:30:42'),(8,8,'prueva v1','una descripcion',0,1,1,'/uploads/1757889227269_c_logo3.png','/uploads/1757889227293_v_Transformar una cart.png','2025-09-15 03:33:20'),(9,8,'prueba','prueba',0,1,3,'/uploads/1757889601954_c_Transformar una cart.png','/uploads/1757889601981_v_logo3.png','2025-09-15 03:39:33'),(10,6,'prueba','ejemplo',0,1,3,'/uploads/1757902799515_c_logo3.png','/uploads/1757902799537_v_logo4.png','2025-09-15 07:19:35'),(11,6,'prueba','ejemplo',0,1,3,'/uploads/1757903501409_c_logo3.png','/uploads/1757903501438_v_logo4.png','2025-09-15 07:20:00'),(12,6,'guapa nico, nico guapa','los generos son mi vida desde que te conoci',0,3,3,'/uploads/1757904243671_c_Transformar una cart.png','/uploads/1757904243688_v_logo4.png','2025-09-15 07:43:26'),(13,6,'mamabicho','huwuwewe',0,1,3,'/uploads/1757967328710_c_Transformar una cart.png','/uploads/1757967328732_v_Transformar una cart.png','2025-09-16 01:13:45'),(14,6,'pruebbbb','aaaaaaaaa',0,3,3,'/uploads/1757987040557_c_logo3.png','/uploads/1757987040605_v_logo3.png','2025-09-16 06:43:36'),(15,6,'gatoooooooo','gatoooooooo',0,1,1,'/uploads/1757987617550_c_logo3.png','/uploads/1757987617599_v_logo4.png','2025-09-16 06:53:07'),(16,6,'aladin','alaaaadin',0,1,1,'/uploads/1757997838582_c_logo3.png','/uploads/1757997838601_v_Transformar una cart.png','2025-09-16 09:43:05'),(19,6,'una prueba serie','una descripcion de serie',0,1,1,'/uploads/1758212651536_c_paisaje-de-la-luna-en-el-anime.jpg','/uploads/1758212651608_v_paisaje-de-la-luna-en-el-anime.jpg','2025-09-18 21:23:34'),(20,6,'una serie mas','otra serie',0,1,1,'/uploads/1758214034679_c_qw.png','/uploads/1758214034685_v_e7b4134401cfe10a7a4fb93a6f4d333d.webp','2025-09-18 21:45:29'),(35,9,'buenos dias','buenas noches',0,1,1,'/uploads/1759109640523_c_1338124.png','/uploads/1759113046369_v_1335175.png','2025-09-29 06:33:19'),(36,9,'debe verse en el reporte','debe verse en el reporte',0,2,4,'/uploads/1759115148861_c_1338124.png','/uploads/1759115149182_v_1335181 (1).png','2025-09-29 08:05:09'),(37,9,'un gato en la lluvia','un gato en la lluvia',0,2,1,'/uploads/1759115534543_c_1335181 (1).png','/uploads/1759115534730_v_1337187.png','2025-09-29 08:11:21'),(38,9,'pastelito de gato ','ay mi gatito miau miau, ay mi gatito miau miau, no hay dark roman',0,1,1,'/uploads/1759124821476_c_1345294.png','/uploads/1759124821605_v_1277346.png','2025-09-29 10:43:15'),(39,15,'una serie','descripcion',0,2,1,'/uploads/1759145896347_c_1336106.png','/uploads/1759145917605_v_1336944.png','2025-09-29 16:36:49'),(40,16,'City The Animation segunda','Midori está en un aprieto. Tiene deudas y su casera intenta extorsionarla por alquileres impagados. Su mejor amiga se niega a prestarle dinero ya que ha descubierto sus trucos. Tal vez un poco de acoso podría ayudar. ¿O un poco de robo menor? Ninguna de las dos opciones es sostenible. Quizás conseguir un trabajo resolvería las cosas... Pero trabajar significa menos tiempo para divertidas aventuras en la gran ciudad...',0,1,1,'/uploads/1759155664582_c_city-the-animation.jpg','/uploads/1759155722841_v_1335178.png','2025-09-29 19:19:23');
/*!40000 ALTER TABLE `series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series_generos`
--

DROP TABLE IF EXISTS `series_generos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `series_generos` (
  `sg_id` int NOT NULL AUTO_INCREMENT,
  `s_id` int NOT NULL,
  `g_id` int NOT NULL,
  PRIMARY KEY (`sg_id`),
  UNIQUE KEY `unique_serie_genero` (`s_id`,`g_id`),
  KEY `g_id` (`g_id`),
  CONSTRAINT `series_generos_ibfk_1` FOREIGN KEY (`s_id`) REFERENCES `series` (`s_id`) ON DELETE CASCADE,
  CONSTRAINT `series_generos_ibfk_2` FOREIGN KEY (`g_id`) REFERENCES `generos` (`g_id`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series_generos`
--

LOCK TABLES `series_generos` WRITE;
/*!40000 ALTER TABLE `series_generos` DISABLE KEYS */;
INSERT INTO `series_generos` VALUES (1,1,1),(2,1,2),(3,8,1),(6,12,2),(5,12,3),(4,12,9),(7,13,1),(11,13,2),(9,13,4),(10,13,7),(8,13,9),(12,13,10),(13,13,11),(15,16,3),(16,16,4),(14,16,6),(25,19,3),(26,19,4),(24,19,9),(28,20,3),(29,20,4),(27,20,9),(82,36,4),(83,36,5),(84,36,8),(97,37,4),(98,37,5),(96,37,6),(99,37,8),(95,37,9),(102,38,1),(103,38,11),(110,39,1),(113,39,3),(114,39,4),(112,39,6),(111,39,9),(115,39,11),(119,40,1),(121,40,8),(120,40,9);
/*!40000 ALTER TABLE `series_generos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series_tipos`
--

DROP TABLE IF EXISTS `series_tipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `series_tipos` (
  `st_id` int NOT NULL AUTO_INCREMENT,
  `s_id` int NOT NULL,
  `t_id` int NOT NULL,
  PRIMARY KEY (`st_id`),
  UNIQUE KEY `unique_serie_tipo` (`s_id`,`t_id`),
  KEY `t_id` (`t_id`),
  CONSTRAINT `series_tipos_ibfk_1` FOREIGN KEY (`s_id`) REFERENCES `series` (`s_id`) ON DELETE CASCADE,
  CONSTRAINT `series_tipos_ibfk_2` FOREIGN KEY (`t_id`) REFERENCES `tipos` (`t_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series_tipos`
--

LOCK TABLES `series_tipos` WRITE;
/*!40000 ALTER TABLE `series_tipos` DISABLE KEYS */;
INSERT INTO `series_tipos` VALUES (1,1,1),(2,2,1),(3,3,1),(4,8,1),(5,9,1),(6,10,1),(7,11,1),(8,12,1),(9,13,1),(16,16,1),(17,16,2),(23,19,1),(24,19,2),(25,20,3),(56,35,1),(57,35,3),(58,36,2),(59,36,3),(64,37,2),(65,37,3),(68,38,1),(69,38,2),(72,39,1),(73,39,3),(76,40,1),(77,40,3);
/*!40000 ALTER TABLE `series_tipos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipos`
--

DROP TABLE IF EXISTS `tipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipos` (
  `t_id` int NOT NULL AUTO_INCREMENT,
  `t_nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`t_id`),
  UNIQUE KEY `t_nombre` (`t_nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipos`
--

LOCK TABLES `tipos` WRITE;
/*!40000 ALTER TABLE `tipos` DISABLE KEYS */;
INSERT INTO `tipos` VALUES (1,'Anime'),(2,'Manga'),(3,'Novela');
/*!40000 ALTER TABLE `tipos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `u_id` int NOT NULL AUTO_INCREMENT,
  `u_email` varchar(100) NOT NULL,
  `u_password` varchar(255) NOT NULL,
  `u_nombre` varchar(100) NOT NULL,
  `u_descripcion` text,
  `u_telefono` varchar(20) DEFAULT NULL,
  `rol_u` int NOT NULL,
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`u_id`),
  UNIQUE KEY `u_email` (`u_email`),
  KEY `rol_u` (`rol_u`),
  KEY `idx_usuarios_email` (`u_email`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_u`) REFERENCES `roles` (`r_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'demo@nekonek.com','e10adc3949ba59abbe56e057f20f883e','Demo User','Soy un usuario de prueba para la plataforma NekoNek.','3001234567',2,'2025-09-11 03:03:03'),(2,'admin@nekonek.com','0192023a7bbd73250516f069df18b500','Administrador NekoNek','Administrador general del sistema.','3109876543',1,'2025-09-11 03:10:46'),(3,'prueba@nekonek.com','e10adc3949ba59abbe56e057f20f883e','usuario de prueba','esto es una descripcion','3247480864',2,'2025-09-13 02:14:25'),(4,'prueba2@nekonek.com','e10adc3949ba59abbe56e057f20f883e','funciona?','esto es otra descripcion','3247480866',2,'2025-09-13 02:15:15'),(5,'estoseguarda@nekonek.com','e10adc3949ba59abbe56e057f20f883e','esto se guarda','pues una prueba y ya','3245647654',2,'2025-09-13 07:51:18'),(6,'vamosaver@nekonek.com','e10adc3949ba59abbe56e057f20f883e','dieof','ddddd','3245434542',2,'2025-09-13 08:11:04'),(7,'diego@nekonek.com','e10adc3949ba59abbe56e057f20f883e','diego','ddd','3454345654',2,'2025-09-13 20:59:23'),(8,'onepuch@nekonek.com','e10adc3949ba59abbe56e057f20f883e','onepuch','un intento','3245436543',2,'2025-09-14 06:40:12'),(9,'niconico@gmail.com','e10adc3949ba59abbe56e057f20f883e','nico nico','nico nico ni','3254565435',2,'2025-09-16 04:47:40'),(10,'basket@gmail.com','e10adc3949ba59abbe56e057f20f883e','yo soy el usuario','una descripcion','3254657643',2,'2025-09-26 16:38:06'),(11,'dmitricassan@gmail.com','e10adc3949ba59abbe56e057f20f883e','dabvhy','el propietario osea yo pero sin ser administrador','3247480864',2,'2025-09-29 00:29:43'),(12,'fernandocastillo1266@gmail.com','e10adc3949ba59abbe56e057f20f883e','luiscastillo','padre','3247480864',2,'2025-09-29 01:10:59'),(13,'yulunacaro@gmail.com','e10adc3949ba59abbe56e057f20f883e','carolina','co fundadora','1234567',2,'2025-09-29 01:48:54'),(14,'nicolmartinezforero12@gmail.com','e10adc3949ba59abbe56e057f20f883e','noco 2','una','3245434565',2,'2025-09-29 04:30:38'),(15,'carolinablancomendoza@gmail.com','e10adc3949ba59abbe56e057f20f883e','carolina','descripcion','3245465456',2,'2025-09-29 11:36:31'),(16,'patriciacc2074@gmail.com','e10adc3949ba59abbe56e057f20f883e','marthe','conocer novelas','3256546756',2,'2025-09-29 14:18:46');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-07 15:22:32
