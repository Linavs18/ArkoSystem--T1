-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.40 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para arkosystem_db
CREATE DATABASE IF NOT EXISTS `arkosystem_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `arkosystem_db`;

-- Volcando estructura para tabla arkosystem_db.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.category: ~7 rows (aproximadamente)
INSERT INTO `category` (`id`, `description`) VALUES
	(1, 'Hand Tools'),
	(2, 'Power Tools'),
	(3, 'Construction Materials'),
	(4, 'Plumbing'),
	(5, 'Electricity and Lighting'),
	(6, 'Paints and Finishes'),
	(7, 'General Hardware');

-- Volcando estructura para tabla arkosystem_db.clients
CREATE TABLE IF NOT EXISTS `clients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.clients: ~7 rows (aproximadamente)
INSERT INTO `clients` (`id`, `name`, `phone`, `address`, `email`) VALUES
	(1, 'Constructora Los Andes T', '310-876-5432', 'Calle 72 #15-23, Medellín', NULL),
	(2, 'Pedro Antonio Moreno', '320-654-3210', 'Carrera 45 #67-89, Bogotá', NULL),
	(3, 'Obras Civiles del Cauca S.A.S', '300-432-1098', 'Avenida 6 #34-56, Popayán', NULL),
	(4, 'Martha Lucía Gómez', '315-210-9876', 'Calle 123 #45-67, Cali', NULL),
	(5, 'Inmobiliaria Santander', '305-098-7654', 'Carrera 27 #89-12, Bucaramanga', NULL),
	(6, 'Alberto de Jesús Ramírez', '311-987-6543', 'Avenida 15 #23-45, Pereira', NULL),
	(7, 'Remodelaciones JM S.A.S', '322-876-5432', 'Calle 89 #56-78, Manizales', NULL);

-- Volcando estructura para tabla arkosystem_db.employees
CREATE TABLE IF NOT EXISTS `employees` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `document` bigint NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `salary` double NOT NULL,
  `role_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `document` (`document`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_employees_role` (`role_id`),
  KEY `FK_employees_users` (`user_id`),
  CONSTRAINT `FK_employees_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_employees_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.employees: ~8 rows (aproximadamente)
INSERT INTO `employees` (`id`, `document`, `email`, `name`, `position`, `salary`, `role_id`, `user_id`, `role`) VALUES
	(1, 1234567890, 'carlos.henao@empresa.com', 'Carlos Alberto Henao', 'Hardware Store Manager', 4200000, 1, 1, NULL),
	(2, 2345678901, 'miguel.rodri@empresa.com', 'Miguel Ángel Rodríguez', 'Specialized Salesperson', 2600000, 3, 2, NULL),
	(3, 3456789012, 'rosa.jimenez@empresa.com', 'Rosa María Jiménez', 'Head Cashier', 1400000, 3, 3, NULL),
	(4, 4567890123, 'jairomorales@empresa.com', 'Jairo Enrique Morales', 'Warehouse Supervisor', 3100000, 3, 4, NULL),
	(5, 5678901234, 'sandramilena@empresa.com', 'Sandra Milena Vargas', 'Counter Salesperson', 1700000, 3, 5, NULL),
	(6, 6789012345, 'alvaro.castillo@empresa.com', 'Álvaro Javier Castillo', 'Administrator', 3600000, 3, 6, NULL),
	(8, 1234, 'pepapig@empresa.com', 'Pepa Pig', 'Cajera', 1500000, 3, 9, NULL),
	(16, 21324, 'papa.cerdito@empresa.com', 'Papa cerdito', 'Administrador', 3000000, 1, 17, NULL);

-- Volcando estructura para tabla arkosystem_db.inventory
CREATE TABLE IF NOT EXISTS `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `category_id` bigint NOT NULL,
  `available_quantity` int NOT NULL,
  `min_stock` int DEFAULT '5',
  `supplier_id` bigint NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `inventory_ibfk_1` (`supplier_id`),
  CONSTRAINT `FK_inventory_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.inventory: ~10 rows (aproximadamente)
INSERT INTO `inventory` (`id`, `name`, `price`, `category_id`, `available_quantity`, `min_stock`, `supplier_id`, `image_url`) VALUES
	(1, 'Corona 16oz Carpenter Hammer', 35000, 1, 45, 10, 1, '/assets/img/uploads/Caja_vacia.jpg'),
	(2, 'Makita HP1640 Impact Drill', 280000, 2, 5, 5, 8, '/assets/img/uploads/1754929148514_D_NQ_NP_916306-MCO73835146842_012024-O.webp'),
	(3, 'Argos Cement x 50kg', 18500, 3, 150, 20, 3, '/assets/img/uploads/Caja_vacia.jpg'),
	(4, 'Pavco 4" x 6m PVC Pipe', 45000, 4, 80, 15, 4, '/assets/img/uploads/Caja_vacia.jpg'),
	(5, 'Philips 9W LED Bulb', 12000, 5, 200, 25, 5, '/assets/img/uploads/Caja_vacia.jpg'),
	(6, 'Pintuco Viniltex White Gallon Paint', 85000, 6, 30, 8, 6, '/assets/img/uploads/Caja_vacia.jpg'),
	(7, '1" Self-Tapping Screws x100 units', 8500, 7, 120, 20, 7, '/assets/img/uploads/Caja_vacia.jpg'),
	(12, 'prueba 3', 242453, 4, 20, 54, 11, '/assets/img/uploads/1754930654500_Arkosystemlogo.jpg'),
	(17, 'telecospio ultra sonico', 2343, 1, 32, 56, 9, '/assets/img/uploads/1754933127816_Elbit-Systems-space-telescope-for-Israels-Ultraviolet-Transient-Astronomy-Satellite-ULTRASAT.webp'),
	(18, 'pulidora', 2347383, 4, 10, 10, 10, '/assets/img/uploads/1754933650403_Imagen de WhatsApp 2025-03-11 a las 07.32.41_d2d3c8eb.jpg');

-- Volcando estructura para tabla arkosystem_db.order_details
CREATE TABLE IF NOT EXISTS `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` double NOT NULL,
  `subtotal` double GENERATED ALWAYS AS ((`quantity` * `unit_price`)) STORED,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_order_product` (`order_id`,`product_id`) USING BTREE,
  KEY `order_details_ibfk_2` (`product_id`) USING BTREE,
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `purchase_order` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `inventory` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `order_details_chk_1` CHECK ((`quantity` > 0)),
  CONSTRAINT `order_details_chk_2` CHECK ((`unit_price` >= 0.0))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.order_details: ~11 rows (aproximadamente)
INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `quantity`, `unit_price`) VALUES
	(1, 1, 3, 150, 18500),
	(2, 2, 1, 5, 35000),
	(3, 2, 2, 1, 280000),
	(4, 3, 4, 30, 45000),
	(5, 4, 5, 15, 12000),
	(6, 4, 7, 10, 8500),
	(7, 5, 6, 9, 85000),
	(8, 5, 3, 5, 18500),
	(9, 6, 1, 3, 35000),
	(10, 6, 7, 8, 8500),
	(11, 7, 5, 30, 12000);

-- Volcando estructura para tabla arkosystem_db.purchase_order
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `order_date` datetime NOT NULL DEFAULT (now()),
  `total` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `purchase_order_ibfk_1` (`client_id`),
  KEY `purchase_order_ibfk_2` (`employee_id`),
  CONSTRAINT `purchase_order_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `purchase_order_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.purchase_order: ~7 rows (aproximadamente)
INSERT INTO `purchase_order` (`id`, `client_id`, `employee_id`, `order_date`, `total`, `status`) VALUES
	(1, 1, 4, '2025-08-05 21:13:39', 2775000, 'DELIVERED'),
	(2, 2, 4, '2025-08-05 21:13:39', 420000, 'SHIPPED'),
	(3, 3, 4, '2025-08-05 21:13:39', 1350000, 'PAID'),
	(4, 4, 4, '2025-08-05 21:13:39', 255000, 'PENDING'),
	(5, 5, 4, '2025-08-05 21:13:39', 810000, 'DELIVERED'),
	(6, 6, 4, '2025-08-05 21:13:39', 170000, 'PAID'),
	(7, 7, 4, '2025-08-05 21:13:39', 360000, 'CANCELED');

-- Volcando estructura para tabla arkosystem_db.role
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.role: ~6 rows (aproximadamente)
INSERT INTO `role` (`id`, `description`, `name`) VALUES
	(1, 'ADMIN', ''),
	(2, 'CLIENTE', ''),
	(3, 'EMPLEADO', ''),
	(4, 'Administrador del sistema', 'ROLE_ADMIN'),
	(5, 'Usuario estándar', 'ROLE_USER'),
	(6, 'Cliente del sistema', 'ROLE_CLIENT');

-- Volcando estructura para tabla arkosystem_db.sale
CREATE TABLE IF NOT EXISTS `sale` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `sale_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_client` (`client_id`) USING BTREE,
  KEY `id_employee` (`employee_id`) USING BTREE,
  CONSTRAINT `sale_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `sale_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `sale_chk_1` CHECK ((`total` >= 0.0))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.sale: ~7 rows (aproximadamente)
INSERT INTO `sale` (`id`, `client_id`, `employee_id`, `sale_date`, `total`, `status`, `payment_method`) VALUES
	(1, 1, 2, '2025-08-06 02:13:18', 1890000, 'COMPLETED', 'TRANSFER'),
	(2, 2, 5, '2025-08-06 02:13:18', 315000, 'COMPLETED', 'CASH'),
	(3, 3, 2, '2025-08-06 02:13:18', 925000, 'COMPLETED', 'CREDIT_CARD'),
	(4, 4, 3, '2025-08-06 02:13:18', 170000, 'PENDING', 'DEBIT_CARD'),
	(5, 5, 2, '2025-08-06 02:13:18', 540000, 'COMPLETED', 'TRANSFER'),
	(6, 6, 5, '2025-08-06 02:13:18', 85000, 'COMPLETED', 'CASH'),
	(7, 7, 3, '2025-08-06 02:13:18', 225000, 'CANCELED', 'CREDIT_CARD');

-- Volcando estructura para tabla arkosystem_db.sale_details
CREATE TABLE IF NOT EXISTS `sale_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sale_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` double NOT NULL,
  `subtotal` double GENERATED ALWAYS AS ((`quantity` * `unit_price`)) STORED,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sale_product` (`sale_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `sale_details_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sale_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `inventory` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `sale_details_chk_1` CHECK ((`quantity` > 0)),
  CONSTRAINT `sale_details_chk_2` CHECK ((`unit_price` >= 0.0))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.sale_details: ~12 rows (aproximadamente)
INSERT INTO `sale_details` (`id`, `sale_id`, `product_id`, `quantity`, `unit_price`) VALUES
	(1, 1, 3, 50, 18500),
	(2, 1, 4, 20, 45000),
	(3, 2, 1, 1, 35000),
	(4, 2, 2, 1, 280000),
	(5, 3, 3, 25, 18500),
	(6, 3, 6, 5, 85000),
	(7, 4, 5, 10, 12000),
	(8, 4, 6, 1, 85000),
	(9, 5, 4, 12, 45000),
	(10, 6, 6, 1, 85000),
	(11, 7, 7, 15, 8500),
	(12, 7, 5, 8, 12000);

-- Volcando estructura para tabla arkosystem_db.suppliers
CREATE TABLE IF NOT EXISTS `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contact` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.suppliers: ~7 rows (aproximadamente)
INSERT INTO `suppliers` (`id`, `name`, `address`, `email`, `phone`, `contact`) VALUES
	(8, 'Comercializadora Andina S.A.S', 'Cra. 15 #45-23, Bogotá, Colombia', 'contacto@andina.com.co', '+57 1 745 8921', NULL),
	(9, 'Distribuciones El Cafetal', 'Calle 8 #12-34, Manizales, Colombia', 'ventas@elcafetal.com', '+57 6 874 5521', NULL),
	(10, 'TecnoProveedores Ltda.', 'Av. El Dorado #70-12, Bogotá, Colombia', 'info@tecnoproveedores.com', '+57 1 689 4523', NULL),
	(11, 'AgroInsumos del Caribe', 'Cl. 23 #18-76, Barranquilla, Colombia', 'agrocaribe@correo.com', '+57 5 356 7744', NULL),
	(12, 'Suministros Industriales del Norte', 'Av. 5N #34-55, Cali, Colombia', 'ventas@indunorte.com', '+57 2 664 2233', NULL),
	(13, 'Global Importaciones S.A.', 'Carrera 7 #112-45, Bogotá, Colombia', 'contacto@globalimport.com', '+57 1 487 9921', NULL),
	(14, 'Ferretería y Proveeduría Central', 'Cl. 56 #18-20, Medellín, Colombia', 'central@ferreteria.com', '+57 4 322 7788', NULL);

-- Volcando estructura para tabla arkosystem_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `document` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_users_role` (`role_id`),
  CONSTRAINT `FK_users_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.users: ~13 rows (aproximadamente)
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role_id`, `document`, `lastname`) VALUES
	(1, 'Carlos Alberto Henao', 'carlos.henao@empresa.com', 'password', 1, NULL, NULL),
	(2, 'Miguel Ángel Rodríguez', 'miguel.rodri@empresa.com', 'password', 3, NULL, NULL),
	(3, 'Rosa María Jiménez', 'rosa.jimenez@empresa.com', 'password', 3, NULL, NULL),
	(4, 'Jairo Enrique Morales', 'jairomorales@empresa.com', 'password', 3, NULL, NULL),
	(5, 'Sandra Milena Vargas', 'sandramilena@empresa.com', 'password', 3, NULL, NULL),
	(6, 'Álvaro Javier Castillo', 'alvaro.castillo@empresa.com', 'password', 3, NULL, NULL),
	(7, 'Pedro Antonio Moreno', 'pedro.antonio@gmail.com', 'password', 2, NULL, NULL),
	(8, 'Martha Lucía Gómez', 'martita123@gmail.com', 'password', 2, NULL, NULL),
	(9, 'Pepa Pig', 'pepapig@empresa.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 3, NULL, NULL),
	(17, 'Papa cerdito', 'papa.cerdito@empresa.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 1, NULL, NULL),
	(24, 'Administrador', 'admin@example.com', '$2a$10$TAl608cGKZO0XrW8FzLdDeKIMk6DV2FFDww6KkHryerjOgmC9.aVS', 4, NULL, NULL),
	(25, 'Juan Sebastian Rodriguez CRuz', 'Juansecruz9999@gmail.com', '$2a$10$Xa.Yd7tcuzOLHvHHqhaK/.e1/HqLEUH4zoArHDqsbWtIaGW.tIlS6', 5, NULL, NULL),
	(26, 'Sebaaaas', 'sr1290853@gmail.com', '$2a$10$PdaIUhTb2X2cVtJlttYSpemKV0m7jJ5kA/Gce3945os898NXrLV6a', 6, NULL, NULL);

-- Volcando estructura para disparador arkosystem_db.after_employee_delete
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `after_employee_delete` AFTER DELETE ON `employees` FOR EACH ROW BEGIN
    DELETE FROM users WHERE id = OLD.user_id;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador arkosystem_db.after_user_insert_client
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `after_user_insert_client` AFTER INSERT ON `users` FOR EACH ROW BEGIN
    IF NEW.role_id = 2 THEN
        INSERT INTO clients (user_id, phone, address)
        VALUES (NEW.id, '', '');
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador arkosystem_db.before_employee_insert
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `before_employee_insert` BEFORE INSERT ON `employees` FOR EACH ROW BEGIN
    DECLARE new_user_id BIGINT;

    -- Si no se pasa user_id, lo creamos
    IF NEW.user_id IS NULL THEN
        INSERT INTO users (name, email, password, role_id)
        VALUES (NEW.name, NEW.email, 'password', NEW.role_id);

        SET new_user_id = LAST_INSERT_ID();
        SET NEW.user_id = new_user_id;
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
