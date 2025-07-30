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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.categoria: ~0 rows (aproximadamente)

-- Volcando estructura para tabla arkosystem_db.chat_mensaje
CREATE TABLE IF NOT EXISTS `chat_mensaje` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint DEFAULT NULL,
  `empleado_id` bigint DEFAULT NULL,
  `mensaje` text NOT NULL,
  `fecha_envio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `leido` tinyint(1) DEFAULT '0',
  `tipo_usuario` enum('CLIENTE','EMPLEADO') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cliente_id` (`cliente_id`),
  KEY `empleado_id` (`empleado_id`),
  CONSTRAINT `chat_mensaje_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chat_mensaje_ibfk_2` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.chat_mensaje: ~0 rows (aproximadamente)

-- Volcando estructura para tabla arkosystem_db.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `teléfono` varchar(20) NOT NULL,
  `dirección` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `correo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.clientes: ~0 rows (aproximadamente)

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.detalle_orden: ~0 rows (aproximadamente)

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.detalle_venta: ~0 rows (aproximadamente)

-- Volcando estructura para tabla arkosystem_db.empleados
CREATE TABLE IF NOT EXISTS `empleados` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cargo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'EMPLEADO, ADMINISTRADOR',
  `salario` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.empleados: ~0 rows (aproximadamente)

-- Volcando estructura para tabla arkosystem_db.factura
CREATE TABLE IF NOT EXISTS `factura` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `venta_id` bigint NOT NULL,
  `numero_factura` varchar(50) NOT NULL,
  `fecha_emision` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total` double NOT NULL,
  `estado` enum('EMITIDA','ENVIADA','ANULADA') NOT NULL DEFAULT 'EMITIDA',
  `ruta_pdf` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `venta_id` (`venta_id`),
  UNIQUE KEY `numero_factura` (`numero_factura`),
  CONSTRAINT `factura_ibfk_1` FOREIGN KEY (`venta_id`) REFERENCES `venta` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `factura_chk_1` CHECK ((`total` >= 0.0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.factura: ~0 rows (aproximadamente)

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.inventario: ~0 rows (aproximadamente)

-- Volcando estructura para tabla arkosystem_db.orden_compra
CREATE TABLE IF NOT EXISTS `orden_compra` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint NOT NULL,
  `empleado_id` bigint NOT NULL,
  `fecha_orden` datetime NOT NULL DEFAULT (now()),
  `total` double NOT NULL,
  `estado` enum('PENDIENTE','PAGADA','ENVIADA','ENTREGADA','CANCELADA') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDIENTE',
  PRIMARY KEY (`id`),
  KEY `orden_compra_ibfk_1` (`cliente_id`),
  KEY `orden_compra_ibfk_2` (`empleado_id`),
  CONSTRAINT `orden_compra_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `orden_compra_ibfk_2` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.orden_compra: ~0 rows (aproximadamente)

-- Volcando estructura para tabla arkosystem_db.proveedores
CREATE TABLE IF NOT EXISTS `proveedores` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `contacto` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.proveedores: ~0 rows (aproximadamente)

-- Volcando estructura para tabla arkosystem_db.venta
CREATE TABLE IF NOT EXISTS `venta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint NOT NULL,
  `empleado_id` bigint NOT NULL,
  `fecha_venta` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total` double NOT NULL DEFAULT '0',
  `estado` enum('PENDIENTE','COMPLETADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
  `metodo_pago` enum('EFECTIVO','TARJETA_DEBITO','TARJETA_CREDITO','TRANSFERENCIA','OTRO') NOT NULL DEFAULT 'EFECTIVO',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_cliente` (`cliente_id`) USING BTREE,
  KEY `id_empleado` (`empleado_id`) USING BTREE,
  CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `venta_ibfk_2` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `venta_chk_1` CHECK ((`total` >= 0.0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla arkosystem_db.venta: ~0 rows (aproximadamente)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
