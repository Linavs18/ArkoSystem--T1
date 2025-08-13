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

## 📚 Créditos

Proyecto desarrollado como actividad del **SENA (Servicio Nacional de Aprendizaje)** dentro del programa de **Análisis y Desarrollo de Software (ADSO)**.

**Elaborado por:**
- Lina Vanessa Salcedo Cuellar
- Juan Fernando Velasquez Sarmiento
- Juan Sebastian Rodriguez Cruz
