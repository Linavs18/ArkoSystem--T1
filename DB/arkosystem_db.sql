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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.clients: ~7 rows (aproximadamente)
INSERT INTO `clients` (`id`, `name`, `phone`, `address`, `email`) VALUES
	(1, 'Constructora Los Andes T', '310-876-5432', 'Calle 72 #15-23, Medellín', NULL),
	(2, 'Pedro Antonio Moreno', '320-654-3210', 'Carrera 45 #67-89, Bogotá', NULL),
	(3, 'Obras Civiles del Cauca S.A.S', '300-432-1098', 'Avenida 6 #34-56, Popayán', NULL),
	(4, 'Martha Lucía Gómez', '315-210-9876', 'Calle 123 #45-67, Cali', NULL),
	(5, 'Inmobiliaria Santander', '305-098-7654', 'Carrera 27 #89-12, Bucaramanga', NULL),
	(6, 'Alberto de Jesús Ramírez', '311-987-6543', 'Avenida 15 #23-45, Pereira', NULL),
	(7, 'Remodelaciones JM S.A.S', '322-876-5432', 'Calle 89 #56-78, Manizales', NULL),
	(16, 'lina', '', '', 'hola@gmail.com');

-- Volcando estructura para tabla arkosystem_db.employees
CREATE TABLE IF NOT EXISTS `employees` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `document` bigint NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `salary` decimal(38,2) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.employees: ~8 rows (aproximadamente)
INSERT INTO `employees` (`id`, `document`, `email`, `name`, `position`, `salary`, `role_id`, `user_id`, `role`) VALUES
	(2, 2345678901, 'miguel.rodri@empresa.com', 'Miguel Ángel Rodríguez', 'Specialized Salesperson', 2600000.00, 7, 2, NULL),
	(4, 4567890123, 'jairomorales@empresa.com', 'Jairo Enrique Morales', 'Warehouse Supervisor', 3100000.00, 7, 4, NULL),
	(5, 5678901234, 'sandramilena@empresa.com', 'Sandra Milena Vargas', 'Counter Salesperson', 1700000.00, 7, 5, NULL),
	(29, 2323, 'blackcloverq999@gmail.com', 'prueba 2', 'limpiador', 123345656767879.00, 7, 34, NULL);

-- Volcando estructura para tabla arkosystem_db.inventory
CREATE TABLE IF NOT EXISTS `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
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
	(1, 'Corona 16oz Carpenter Hammer', 35000.00, 1, 45, 10, 1, '/assets/img/uploads/Caja_vacia.jpg'),
	(2, 'Makita HP1640 Impact Drill', 280000.00, 2, 5, 5, 8, '/assets/img/uploads/1754929148514_D_NQ_NP_916306-MCO73835146842_012024-O.webp'),
	(3, 'Argos Cement x 50kg', 18500.00, 3, 150, 20, 3, '/assets/img/uploads/Caja_vacia.jpg'),
	(4, 'Pavco 4" x 6m PVC Pipe', 45000.00, 4, 80, 15, 4, '/assets/img/uploads/Caja_vacia.jpg'),
	(5, 'Philips 9W LED Bulb', 12000.00, 5, 200, 25, 5, '/assets/img/uploads/Caja_vacia.jpg'),
	(6, 'Pintuco Viniltex White Gallon Paint', 85000.00, 6, 30, 8, 6, '/assets/img/uploads/Caja_vacia.jpg'),
	(7, '1" Self-Tapping Screws x100 units', 8500.00, 7, 120, 20, 7, '/assets/img/uploads/Caja_vacia.jpg'),
	(12, 'prueba 3', 242453.00, 4, 20, 54, 11, '/assets/img/uploads/1754930654500_Arkosystemlogo.jpg'),
	(17, 'telecospio ultra sonico', 2343.00, 1, 32, 56, 9, '/assets/img/uploads/1754933127816_Elbit-Systems-space-telescope-for-Israels-Ultraviolet-Transient-Astronomy-Satellite-ULTRASAT.webp'),
	(18, 'pulidora', 2347383.00, 4, 10, 10, 10, '/assets/img/uploads/1754933650403_Imagen de WhatsApp 2025-03-11 a las 07.32.41_d2d3c8eb.jpg');

-- Volcando estructura para tabla arkosystem_db.order_details
CREATE TABLE IF NOT EXISTS `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(20,6) NOT NULL,
  `subtotal` decimal(20,6) GENERATED ALWAYS AS ((`quantity` * `unit_price`)) STORED,
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
	(1, 1, 3, 150, 18500.000000),
	(2, 2, 1, 5, 35000.000000),
	(3, 2, 2, 1, 280000.000000),
	(4, 3, 4, 30, 45000.000000),
	(5, 4, 5, 15, 12000.000000),
	(6, 4, 7, 10, 8500.000000),
	(7, 5, 6, 9, 85000.000000),
	(8, 5, 3, 5, 18500.000000),
	(9, 6, 1, 3, 35000.000000),
	(10, 6, 7, 8, 8500.000000),
	(11, 7, 5, 30, 12000.000000);

-- Volcando estructura para tabla arkosystem_db.purchase_order
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `order_date` datetime NOT NULL DEFAULT (now()),
  `total` decimal(20,6) NOT NULL DEFAULT (0),
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `purchase_order_ibfk_1` (`client_id`),
  KEY `purchase_order_ibfk_2` (`employee_id`),
  CONSTRAINT `purchase_order_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `purchase_order_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.purchase_order: ~7 rows (aproximadamente)
INSERT INTO `purchase_order` (`id`, `client_id`, `employee_id`, `order_date`, `total`, `status`) VALUES
	(1, 1, 4, '2025-08-05 21:13:39', 2775000.000000, 'DELIVERED'),
	(2, 2, 4, '2025-08-05 21:13:39', 420000.000000, 'SHIPPED'),
	(3, 3, 4, '2025-08-05 21:13:39', 1350000.000000, 'PAID'),
	(4, 4, 4, '2025-08-05 21:13:39', 255000.000000, 'PENDING'),
	(5, 5, 4, '2025-08-05 21:13:39', 810000.000000, 'DELIVERED'),
	(6, 6, 4, '2025-08-05 21:13:39', 170000.000000, 'PAID'),
	(7, 7, 4, '2025-08-05 21:13:39', 360000.000000, 'CANCELED');

-- Volcando estructura para tabla arkosystem_db.role
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.role: ~6 rows (aproximadamente)
INSERT INTO `role` (`id`, `description`, `name`) VALUES
	(4, 'Administrador del sistema', 'ROLE_ADMIN'),
	(6, 'Cliente del sistema', 'ROLE_CLIENT'),
	(7, 'Empleado del sistema', 'ROLE_EMPLOYEE');

-- Volcando estructura para tabla arkosystem_db.sale
CREATE TABLE IF NOT EXISTS `sale` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `sale_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total` decimal(20,6) NOT NULL DEFAULT (0),
  `status` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_client` (`client_id`) USING BTREE,
  KEY `id_employee` (`employee_id`) USING BTREE,
  CONSTRAINT `sale_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `sale_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `sale_chk_1` CHECK ((`total` >= 0.0))
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.sale: ~7 rows (aproximadamente)
INSERT INTO `sale` (`id`, `client_id`, `employee_id`, `sale_date`, `total`, `status`, `payment_method`) VALUES
	(36, 1, 2, '2025-08-06 07:13:18', 1890000.000000, 'COMPLETED', 'TRANSFER'),
	(37, 2, 5, '2025-08-06 07:13:18', 315000.000000, 'COMPLETED', 'CASH'),
	(38, 3, 2, '2025-08-06 07:13:18', 925000.000000, 'COMPLETED', 'CREDIT_CARD'),
	(39, 4, 4, '2025-08-06 07:13:18', 170000.000000, 'PENDING', 'DEBIT_CARD'),
	(40, 5, 2, '2025-08-06 07:13:18', 540000.000000, 'COMPLETED', 'TRANSFER'),
	(41, 6, 5, '2025-08-06 07:13:18', 85000.000000, 'COMPLETED', 'CASH'),
	(42, 7, 4, '2025-08-06 07:13:18', 225000.000000, 'CANCELED', 'CREDIT_CARD');

-- Volcando estructura para tabla arkosystem_db.sale_details
CREATE TABLE IF NOT EXISTS `sale_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sale_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(20,6) NOT NULL,
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_users_role` (`role_id`),
  CONSTRAINT `FK_users_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.users: ~0 rows (aproximadamente)
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role_id`) VALUES
	(2, 'Miguel Ángel Rodríguez', 'miguel.rodri@empresa.com', '$2b$10$DP561HfDtrobYdJoMtxNZOH5RNgye1Yjl0C72WvK7pFSG4SffjWSe', 7),
	(4, 'Jairo Enrique Morales', 'jairomorales@empresa.com', '$2b$10$DP561HfDtrobYdJoMtxNZOH5RNgye1Yjl0C72WvK7pFSG4SffjWSe', 7),
	(5, 'Sandra Milena Vargas', 'sandramilena@empresa.com', '$2b$10$DP561HfDtrobYdJoMtxNZOH5RNgye1Yjl0C72WvK7pFSG4SffjWSe', 7),
	(17, 'Papa cerdito', 'papa.cerdito@empresa.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 4),
	(24, 'Administrador', 'admin@example.com', '$2a$10$TAl608cGKZO0XrW8FzLdDeKIMk6DV2FFDww6KkHryerjOgmC9.aVS', 4),
	(25, 'Juan Sebastian Rodriguez CRuz', 'Juansecruz9999@gmail.com', '$2a$10$Xa.Yd7tcuzOLHvHHqhaK/.e1/HqLEUH4zoArHDqsbWtIaGW.tIlS6', 7),
	(26, 'Sebaaaas', 'sr1290853@gmail.com', '$2a$10$PdaIUhTb2X2cVtJlttYSpemKV0m7jJ5kA/Gce3945os898NXrLV6a', 6),
	(27, 'Lina', 'nosemk9@gmail.com', '$2a$10$QKVZPt9pK.v9CCCZnrIbt.dtgo4WUEcY4ZOJ/vVuJvRMTMg8eApTK', 7),
	(28, 'Empleado', 'employee@arkosystem.com', '$2a$10$LkzkR3FG8hg9quWo3gJtue2WQ7C8aFsDMYKFECjHEb8n8t4LgzVm.', 7),
	(34, 'prueba 2', 'blackcloverq999@gmail.com', '$2b$10$DP561HfDtrobYdJoMtxNZOH5RNgye1Yjl0C72WvK7pFSG4SffjWSe', 7),
	(35, 'lina', 'hola@gmail.com', '$2a$10$G43jC42SigRaY0RsvIHxFO04mFKkGl.wPBtouHQfVg08XclzwIttm', 6);

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
    -- Si el rol es 2 (CLIENTE), insertar en clients
    IF NEW.role_id = 6 THEN
        INSERT INTO clients (id, name, phone, address, email)
        VALUES (NULL, NEW.name, '', '', NEW.email);
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador arkosystem_db.before_employee_insert
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `before_employee_insert` BEFORE INSERT ON `employees` FOR EACH ROW BEGIN
    DECLARE new_user_id BIGINT;

    -- Crear usuario antes de insertar el empleado
    INSERT INTO users (name, email, password, role_id)
    VALUES (NEW.name, NEW.email, '$2b$10$DP561HfDtrobYdJoMtxNZOH5RNgye1Yjl0C72WvK7pFSG4SffjWSe', NEW.role_id);

    -- Obtener el ID del nuevo usuario
    SET new_user_id = LAST_INSERT_ID();

    -- Asignar ese ID al campo user_id del empleado
    SET NEW.user_id = new_user_id;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
