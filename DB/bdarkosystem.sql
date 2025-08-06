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

-- Volcando estructura para tabla arkosystem_db.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.categoria: ~0 rows (aproximadamente)
INSERT INTO `categoria` (`id`, `descripcion`) VALUES
	(1, 'Herramientas Manuales'),
	(2, 'Herramientas Eléctricas'),
	(3, 'Materiales de Construcción'),
	(4, 'Plomería y Fontanería'),
	(5, 'Electricidad y Iluminación'),
	(6, 'Pinturas y Acabados'),
	(7, 'Ferretería General');

-- Volcando estructura para tabla arkosystem_db.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `teléfono` varchar(20) NOT NULL,
  `dirección` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `correo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.clientes: ~0 rows (aproximadamente)
INSERT INTO `clientes` (`id`, `nombre`, `teléfono`, `dirección`, `correo`) VALUES
	(1, 'Constructora Los Andes Ltda', '310-876-5432', 'Calle 72 #15-23, Medellín', 'gerencia@losandes.com.co'),
	(2, 'Pedro Antonio Moreno', '320-654-3210', 'Carrera 45 #67-89, Bogotá', 'pedro.moreno@gmail.com'),
	(3, 'Obras Civiles del Cauca S.A.S', '300-432-1098', 'Avenida 6 #34-56, Popayán', 'contratacion@obrasdelcauca.co'),
	(4, 'Martha Lucía Gómez', '315-210-9876', 'Calle 123 #45-67, Cali', 'martha.gomez@hotmail.com'),
	(5, 'Inmobiliaria Santander', '305-098-7654', 'Carrera 27 #89-12, Bucaramanga', 'proyectos@inmosantander.com'),
	(6, 'Alberto de Jesús Ramírez', '311-987-6543', 'Avenida 15 #23-45, Pereira', 'alberto.ramirez@yahoo.com'),
	(7, 'Remodelaciones JM S.A.S', '322-876-5432', 'Calle 89 #56-78, Manizales', 'ventas@remodelacionesjm.co');

-- Volcando estructura para tabla arkosystem_db.detalle_orden
CREATE TABLE IF NOT EXISTS `detalle_orden` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `orden_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` double NOT NULL,
  `subtotal` double GENERATED ALWAYS AS ((`cantidad` * `precio_unitario`)) STORED,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_orden_producto` (`orden_id`,`producto_id`) USING BTREE,
  KEY `detalle_orden_ibfk_2` (`producto_id`) USING BTREE,
  CONSTRAINT `detalle_orden_ibfk_1` FOREIGN KEY (`orden_id`) REFERENCES `orden_compra` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `detalle_orden_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `inventario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `detalle_orden_chk_1` CHECK ((`cantidad` > 0)),
  CONSTRAINT `detalle_orden_chk_2` CHECK ((`precio_unitario` >= 0.0))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.detalle_orden: ~0 rows (aproximadamente)
INSERT INTO `detalle_orden` (`id`, `orden_id`, `producto_id`, `cantidad`, `precio_unitario`) VALUES
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

-- Volcando estructura para tabla arkosystem_db.detalle_venta
CREATE TABLE IF NOT EXISTS `detalle_venta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `venta_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` double NOT NULL,
  `subtotal` double GENERATED ALWAYS AS ((`cantidad` * `precio_unitario`)) STORED,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_venta_producto` (`venta_id`,`producto_id`),
  KEY `producto_id` (`producto_id`),
  CONSTRAINT `detalle_venta_ibfk_1` FOREIGN KEY (`venta_id`) REFERENCES `venta` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `detalle_venta_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `inventario` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `detalle_venta_chk_1` CHECK ((`cantidad` > 0)),
  CONSTRAINT `detalle_venta_chk_2` CHECK ((`precio_unitario` >= 0.0))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.detalle_venta: ~0 rows (aproximadamente)
INSERT INTO `detalle_venta` (`id`, `venta_id`, `producto_id`, `cantidad`, `precio_unitario`) VALUES
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

-- Volcando estructura para tabla arkosystem_db.empleados
CREATE TABLE IF NOT EXISTS `empleados` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `documento` bigint NOT NULL,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cargo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `salario` double NOT NULL,
  `rol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ADMIN, EMPLEADO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `documento` (`documento`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.empleados: ~0 rows (aproximadamente)
INSERT INTO `empleados` (`id`, `documento`, `nombre`, `cargo`, `salario`, `rol`) VALUES
	(1, 1234567890, 'Carlos Alberto Henao', 'Gerente de Ferretería', 4200000, 'ADMIN'),
	(2, 2345678901, 'Miguel Ángel Rodríguez', 'Vendedor Especializado', 2600000, 'EMPLEADO'),
	(3, 3456789012, 'Rosa María Jiménez', 'Cajera Principal', 1400000, 'EMPLEADO'),
	(4, 4567890123, 'Jairo Enrique Morales', 'Supervisor de Bodega', 3100000, 'EMPLEADO'),
	(5, 5678901234, 'Sandra Milena Vargas', 'Vendedora de Mostrador', 1700000, 'EMPLEADO'),
	(6, 6789012345, 'Álvaro Javier Castillo', 'Administrador', 3600000, 'ADMIN'),
	(7, 7890123456, 'Beatriz Elena Ospina', 'Atención al Cliente', 1500000, 'EMPLEADO');

-- Volcando estructura para tabla arkosystem_db.inventario
CREATE TABLE IF NOT EXISTS `inventario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `precio` double NOT NULL,
  `categoria_id` bigint NOT NULL,
  `cantidad_disponible` int NOT NULL,
  `stock_minimo` int DEFAULT '5',
  `proveedor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `categoria_id` (`categoria_id`),
  KEY `inventario_ibfk_1` (`proveedor_id`),
  CONSTRAINT `FK_inventario_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.inventario: ~0 rows (aproximadamente)
INSERT INTO `inventario` (`id`, `nombre`, `precio`, `categoria_id`, `cantidad_disponible`, `stock_minimo`, `proveedor_id`) VALUES
	(1, 'Martillo Carpintero Corona 16oz', 35000, 1, 45, 10, 1),
	(2, 'Taladro Percutor Makita HP1640', 280000, 2, 12, 3, 2),
	(3, 'Cemento Argos x 50kg', 18500, 3, 150, 20, 3),
	(4, 'Tubería PVC 4" x 6m Pavco', 45000, 4, 80, 15, 4),
	(5, 'Bombillo LED 9W Philips', 12000, 5, 200, 25, 5),
	(6, 'Pintura Viniltex Blanco Gal Pintuco', 85000, 6, 30, 8, 6),
	(7, 'Tornillos Autoroscantes 1" x100und', 8500, 7, 120, 20, 7);

-- Volcando estructura para tabla arkosystem_db.orden_compra
CREATE TABLE IF NOT EXISTS `orden_compra` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint NOT NULL,
  `empleado_id` bigint NOT NULL,
  `fecha_orden` datetime NOT NULL DEFAULT (now()),
  `total` double NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '''PENDIENTE'',''PAGADA'',''ENVIADA'',''ENTREGADA'',''CANCELADA''',
  PRIMARY KEY (`id`),
  KEY `orden_compra_ibfk_1` (`cliente_id`),
  KEY `orden_compra_ibfk_2` (`empleado_id`),
  CONSTRAINT `orden_compra_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `orden_compra_ibfk_2` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.orden_compra: ~0 rows (aproximadamente)
INSERT INTO `orden_compra` (`id`, `cliente_id`, `empleado_id`, `fecha_orden`, `total`, `estado`) VALUES
	(1, 1, 4, '2025-08-05 21:13:39', 2775000, 'ENTREGADA'),
	(2, 2, 4, '2025-08-05 21:13:39', 420000, 'ENVIADA'),
	(3, 3, 4, '2025-08-05 21:13:39', 1350000, 'PAGADA'),
	(4, 4, 4, '2025-08-05 21:13:39', 255000, 'PENDIENTE'),
	(5, 5, 4, '2025-08-05 21:13:39', 810000, 'ENTREGADA'),
	(6, 6, 4, '2025-08-05 21:13:39', 170000, 'PAGADA'),
	(7, 7, 4, '2025-08-05 21:13:39', 360000, 'CANCELADA');

-- Volcando estructura para tabla arkosystem_db.proveedores
CREATE TABLE IF NOT EXISTS `proveedores` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `contacto` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.proveedores: ~0 rows (aproximadamente)
INSERT INTO `proveedores` (`id`, `nombre`, `contacto`) VALUES
	(1, 'Herramientas Corona S.A.', '601-234-5678'),
	(2, 'Distribuidora Makita Colombia', '604-567-8901'),
	(3, 'Cemex Colombia S.A.', '601-890-1234'),
	(4, 'Pavco Wavin S.A.', '604-123-4567'),
	(5, 'Schneider Electric Colombia', '601-456-7890'),
	(6, 'Pintuco S.A.', '604-789-0123'),
	(7, 'Ferretería Nacional Ltda', '601-345-6789');

-- Volcando estructura para tabla arkosystem_db.venta
CREATE TABLE IF NOT EXISTS `venta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint NOT NULL,
  `empleado_id` bigint NOT NULL,
  `fecha_venta` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total` double NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '''PENDIENTE'',''COMPLETADA'',''CANCELADA''',
  `metodo_pago` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '''EFECTIVO'',''TARJETA_DEBITO'',''TARJETA_CREDITO'',''TRANSFERENCIA'',''OTRO''',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_cliente` (`cliente_id`) USING BTREE,
  KEY `id_empleado` (`empleado_id`) USING BTREE,
  CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `venta_ibfk_2` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `venta_chk_1` CHECK ((`total` >= 0.0))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.venta: ~0 rows (aproximadamente)
INSERT INTO `venta` (`id`, `cliente_id`, `empleado_id`, `fecha_venta`, `total`, `estado`, `metodo_pago`) VALUES
	(1, 1, 2, '2025-08-06 02:13:18', 1890000, 'COMPLETADA', 'TRANSFERENCIA'),
	(2, 2, 5, '2025-08-06 02:13:18', 315000, 'COMPLETADA', 'EFECTIVO'),
	(3, 3, 2, '2025-08-06 02:13:18', 925000, 'COMPLETADA', 'TARJETA_CREDITO'),
	(4, 4, 3, '2025-08-06 02:13:18', 170000, 'PENDIENTE', 'TARJETA_DEBITO'),
	(5, 5, 2, '2025-08-06 02:13:18', 540000, 'COMPLETADA', 'TRANSFERENCIA'),
	(6, 6, 5, '2025-08-06 02:13:18', 85000, 'COMPLETADA', 'EFECTIVO'),
	(7, 7, 3, '2025-08-06 02:13:18', 225000, 'CANCELADA', 'TARJETA_CREDITO');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
