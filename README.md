# 🛠️ ArkoSystem

**ArkoSystem** es una aplicación web empresarial desarrollada con **Spring Boot**, **Thymeleaf** y **MySQL**, diseñada para gestionar de forma integral de una ferretería. Incorpora exportación de datos, autenticación segura y panel administrativo.

---

## 📋 Funcionalidades principales
<picture> <img align="right" src="https://certificadossena.net/wp-content/uploads/2022/10/logo-sena-verde-complementario-svg-2022.svg" width="250px"></picture>

- 📦 **Gestión de entidades**:
  - Clientes (`Clients`)
  - Empleados (`Employee`)
  - Inventario (`Inventory`)
  - Proveedores (`Supplier`)
  - Órdenes de compra (`PurchaseOrder`)
  - Detalles de orden (`OrderDetails`)
  - Ventas y detalles de venta
  - Usuarios y roles

- 🔐 **Seguridad y autenticación**:
  - Inicio de sesión con Spring Security
  - Roles y permisos personalizados
  - Recuperación de contraseña

- 📊 **Reportes y exportación**:
  - **PDF** (iText) con formato profesional
  - **Excel** (Apache POI) con tablas formateadas
  - Gráficos y estadísticas desde el panel

- 🧭 Navegación intuitiva:
  - Menú lateral para todas las secciones
  - Diseño responsivo con **Bootstrap 5**

---

## 🚀 Comenzando

### Requisitos previos

- JDK 17 o superior
- Maven
- MySQL Server

### Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Linavs18/ArkoSystem--T1.git
   cd ArkoSystem--T1/arkosystem
   ```
2. Crea la base de datos en MySQL usando el archivo:
   ```
   DB/arkosystem_db.sql
   ```
3. Configura `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/arkosystem
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```
4. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```
5. Accede en tu navegador:
   ```
   http://localhost:8080/
   ```

---

## 📌 Estructura del proyecto

```
arkosystem/
├── src/
│   ├── main/
│   │   ├── java/co/edu/sena/arkosystem/
│   │   │   ├── config/         # Configuración (Spring Security, WebConfig)
│   │   │   ├── controller/     # Controladores MVC para cada módulo
│   │   │   ├── model/          # Entidades JPA
│   │   └── resources/
│   │       ├── templates/      # Vistas Thymeleaf
│   │       └── static/         # Recursos CSS, JS, imágenes
├── DB/                         # Script SQL
├── MER/                        # Modelo entidad-relación
├── UML/                        # Diagramas de casos de uso, actividades, secuencia
└── pom.xml                     # Dependencias y configuración Maven
```

---

## 📄 Exportar listados

- **PDF**: Botones dedicados en cada módulo, con estilos y logotipos
- **Excel**: Archivos `.xlsx` formateados automáticamente

---

## 🛠️ Buenas prácticas implementadas

- Arquitectura MVC con Spring Boot + Thymeleaf
- Repositorios JPA para persistencia de datos
- Control de acceso basado en roles
- Vistas responsivas y adaptadas para uso empresarial

---

## 👥 Contribuciones

### Lina Vanessa Salcedo Cuellar
- Creación inicial del repositorio y estructura del proyecto
- Diseño del mockup y diagramas MER
- Configuración de la base de datos (`DB/arkosystem_db.sql`)
- Generación del proyecto con Spring Boot
- Configuración de seguridad (`SecurityConfig`, `WebConfig`)
- Vistas y controladores: Clientes (`ViewClients`, `ControllerClients`)
- Vistas y controladores: Inventario (`ViewInventory`, `ControllerInventory`)
- Dashboard de clientes (`ControllerDashboard`)
- Módulo de Reportes (`ViewReports`, `ControllerReports`)
- Servicio de autenticación de usuarios (`UserDetailsServiceImpl`)
- Corrección de errores y ajustes generales en múltiples módulos

### Juan Fernando Velasquez Sarmiento
- Diagramas de interfaz de usuario (DIU) y UML (casos de uso, actividades, secuencia)
- Modelos JPA de todas las entidades: `Clients`, `Employee`, `Inventory`, `Suppliers`, `Category`, `PurchaseOrder`, `OrderDetails`, `Sale`, `SaleDetails`, `PayMethod`
- Repositorios JPA para todas las entidades
- Controladores MVC para todos los módulos del sistema
- Módulo de Proveedores (`ControllerSupplier`, vistas `Suppliers`)
- Módulo de Ventas (`ControllerSale`, `ControllerSaleDetail`, `ViewSale`, vistas `Sales`)
- Configuración de seguridad (`SecurityConfig`, `WebConfig`)
- Javadocs y comentarios de documentación del código
- Barra lateral de navegación (`sidebar.html`)
- Scripts JavaScript del frontend

### Juan Sebastian Rodriguez Cruz
- Diseño visual y estilos CSS (`custom.css`, `volt.css`, integración con Bootstrap)
- Implementación de autenticación con Spring Security (`UserDetailsImpl`, `SecurityConfig`)
- Módulo de Empleados (`ControllerEmployee`, `ViewEmployee`)
- Gestión de Usuarios y Roles (`ControllerUsers`, `ControllerRoles`)
- Configuración de cuenta (`ControllerSettings`)
- Recuperación de contraseña (`ControllerForgotPassword`)
- Controlador de errores (`ControllerError`)
- Búsqueda (`Search`)
- Paginación de listados
- Módulo de Ventas Pt1 (`ViewSale`)
- Repositorios: `RepositoryUser`, `RoleRepository`, `RepositoryClients`, `RepositoryEmployee`, `RepositorySaleDetails`, `RepositorySuppliers`

---

## 📚 Créditos

Proyecto desarrollado como actividad del **SENA (Servicio Nacional de Aprendizaje)** dentro del programa de **Análisis y Desarrollo de Software (ADSO)**.

**Elaborado por:**
- Lina Vanessa Salcedo Cuellar
- Juan Fernando Velasquez Sarmiento
- Juan Sebastian Rodriguez Cruz
