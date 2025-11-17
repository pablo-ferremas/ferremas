
# Información del Proyecto Ferremas

## Descripción General
**Ferremas** es un proyecto de backend desarrollado como parte de una integración de plataformas. El proyecto está construido utilizando **Spring Boot** y **Java 17**, con una base de datos **MySQL** para la persistencia de datos.

- `pom.xml:14-15`

## Detalles Técnicos

### Tecnologías Principales
- **Java:** Versión 17  
  `pom.xml:29-30`
- **Spring Boot:** Versión 3.4.5  
  `pom.xml:6-8`
- **Base de Datos:** MySQL  
  `pom.xml:42-46`
- **Gestión de Dependencias:** Maven  
  `pom.xml:63-64`

### Dependencias Principales
- **Spring Boot Starter Data JPA:** Para la persistencia de datos  
  `pom.xml:33-36`
- **Spring Boot Starter Web:** Para el desarrollo de APIs REST  
  `pom.xml:37-40`
- **MySQL Connector:** Para la conexión con la base de datos MySQL  
  `pom.xml:42-46`
- **Lombok:** Para reducir el código boilerplate  
  `pom.xml:47-51`
- **Spring Boot Starter Test:** Para pruebas unitarias e integración  
  `pom.xml:52-56`
- **Spring Boot Actuator:** Para monitoreo y gestión de la aplicación  
  `pom.xml:57-60`

## Estructura del Proyecto

```
ferremas-backend/  
├── src/  
│   ├── main/  
│   │   ├── java/  
│   │   │   └── cl/duoc/integracion/ferremas/  
│   │   │       ├── controller/  
│   │   │       ├── entity/  
│   │   │       ├── repository/  
│   │   │       ├── dto/  
│   │   │       └── FerremasApplication.java  
│   │   └── resources/  
│   │       └── application.properties  
│   └── test/  
├── .mvn/wrapper/  
├── pom.xml  
├── mvnw  
└── mvnw.cmd  
```

## Configuración de Desarrollo

### Requisitos Previos
- JDK 17 o superior
- MySQL 8.0 o compatible
- Git
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Configuración de la Base de Datos

1. Instalar MySQL Server  
2. Crear una base de datos y usuario para la aplicación:

```sql
CREATE DATABASE ferremas;  
CREATE USER 'ferremas_user'@'localhost' IDENTIFIED BY 'tu_contraseña';  
GRANT ALL PRIVILEGES ON ferremas.* TO 'ferremas_user'@'localhost';  
FLUSH PRIVILEGES;
```

3. Configurar el archivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ferremas  
spring.datasource.username=ferremas_user  
spring.datasource.password=tu_contraseña  
spring.jpa.hibernate.ddl-auto=update  
spring.jpa.show-sql=true  
server.port=8090
```

## Compilación y Ejecución

### Usando Maven Wrapper

**En Linux/macOS:**
```bash
./mvnw clean install  
./mvnw spring-boot:run
```

**En Windows:**
```cmd
mvnw.cmd clean install  
mvnw.cmd spring-boot:run
```

### Usando Maven (instalado globalmente)
```bash
mvn clean install  
mvn spring-boot:run
```

La aplicación se iniciará en:  
[http://localhost:8090](http://localhost:8090)

## API REST

| Endpoint                                  | Descripción                              | Retorna                                 |
|-------------------------------------------|------------------------------------------|------------------------------------------|
| `http://localhost:8090/api/categoria`     | Endpoint de categorías                   | Lista de todas las categorías            |
| `http://localhost:8090/api/marca`         | Endpoint de marcas                       | Lista de todas las marcas                |
| `http://localhost:8090/api/precio`        | Endpoint de precios                      | Lista de todos los precios               |
| `http://localhost:8090/api/v1`            | Productos (formato entidad)              | Lista de productos como entidad          |
| `http://localhost:8090/api/v1/producto`   | Productos (formato DTO)                  | Lista de productos como DTO              |

## Solución de Problemas Comunes

### Problemas de Conexión a la Base de Datos
- Verificar que MySQL esté en ejecución
- Comprobar las credenciales en `application.properties`
- Asegurarse de que la base de datos `ferremas` exista

### Fallos en la Compilación
- Confirmar instalación correcta de JDK 17
- Verificar configuración de Maven
- Revisar que todas las dependencias sean accesibles

### Errores en Tiempo de Ejecución
- Revisar logs para detalles de errores
- Verificar que el puerto 8090 esté libre
