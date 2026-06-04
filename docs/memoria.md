# Memoria Técnica

Este documento detalla las decisiones arquitectónicas clave tomadas en el proyecto y proporciona las instrucciones de instalación basadas en las releases generadas en GitHub.

## 1. Decisiones con respecto a la Arquitectura

El proyecto `mgcss-track-grupo2` está diseñado siguiendo los principios de la **Arquitectura Limpia (Clean Architecture)**. Esta decisión permite mantener un alto nivel de desacoplamiento entre las reglas de negocio (dominio) y las tecnologías externas (bases de datos, frameworks, UI).

### 1.1. Estructura de Capas
El código está organizado en las siguientes capas principales dentro del paquete `com.mgcss`:
* **Domain (`domain`)**: Contiene la lógica pura de negocio y las entidades (ej. `Solicitud`, `Tecnico`). Es el corazón de la aplicación y no tiene dependencias de ningún framework externo (excepto anotaciones de persistencia básicas en algunos casos pragmáticos).
* **Service (`service`)**: Orquesta los casos de uso de la aplicación, utilizando los repositorios y las entidades de dominio para ejecutar operaciones de negocio.
* **Infrastructure (`infrastructure`)**: Contiene los adaptadores que interactúan con agentes externos, como las implementaciones de repositorios (Spring Data JPA) para la persistencia en base de datos.
* **API (`api`)**: Define los controladores REST y los endpoints que exponen la funcionalidad al exterior. Se integran además Controladores Web (MVC) para la gestión del Frontend.
* **Frontend (Thymeleaf)**: Plantillas renderizadas del lado del servidor (`src/main/resources/templates`) que consumen directamente los servicios internos, sustituyendo la anterior aproximación de SPA estática.
* **Security (`config`)**: Capa transversal que protege la aplicación mediante Spring Security, permitiendo acceso programático público a las APIs pero blindando las vistas mediante autenticación de formulario en memoria.

### 1.2. Stack Tecnológico
* **Framework Principal**: Spring Boot (v3.5.14) con Java 17, facilitando la creación de aplicaciones web robustas de forma rápida.
* **Persistencia y Testing**: Se emplea Spring Data JPA con Hibernate.
  * *Desarrollo/Testing*: Uso de **Testcontainers** para levantar de forma dinámica contenedores de PostgreSQL efímeros durante las pruebas de integración, igualando el entorno de test con el de producción.
  * *Producción*: PostgreSQL gestionado vía `docker-compose`.
* **Calidad y CI/CD**: Integración continua mediante GitHub Actions y SonarCloud. Automatización del Despliegue Continuo (CD) hacia Render.com mediante Webhooks desencadenados al publicar releases.
* **Monitorización**: Integración de un servicio interno de SLA y Spring Boot Actuator para exponer un Dashboard de control en tiempo real (rutas `/dashboard` y `/actuator/health`).

---

## 2. Instrucciones de Instalación

El pipeline de CI/CD del proyecto compila y empaqueta automáticamente el código en cada release creada en GitHub (etiquetas de tipo `v*`). Puedes instalar y ejecutar el proyecto de dos formas.

### Opción A: Ejecutar el archivo `.jar` (Requiere Java 17)

1. Dirígete a la sección de **[Releases](https://github.com/antonioabadpro/mgcss-track-grupo2/releases)** en el repositorio de GitHub.
2. En la release más reciente, descarga el archivo empaquetado `.jar` que se encuentra en la sección *Assets*.
3. Abre un terminal en la carpeta donde descargaste el archivo.
4. Ejecuta el siguiente comando (sustituyendo el nombre exacto del archivo `.jar`):
   ```bash
   java -jar mgcss-track-grupo2-0.0.1-SNAPSHOT.jar
   ```
5. La aplicación se iniciará y estará disponible en `http://localhost:8080`.

### Opción B: Usar Docker (Recomendado)

Cada release también publica automáticamente una imagen Docker lista para usar en Docker Hub.

1. Asegúrate de tener [Docker](https://docs.docker.com/get-docker/) instalado y en ejecución en tu sistema.
2. Abre un terminal y ejecuta el siguiente comando para descargar y correr la imagen de la última release (ejemplo con la etiqueta de versión correspondiente, como `v1.0.0`):
   ```bash
   docker run -p 8080:8080 mininh/mgcss-track-grupo2:<version>
   ```
   *(Nota: Puedes verificar la etiqueta exacta de la versión observando el tag de la release en GitHub).*
3. La aplicación se descargará e iniciará automáticamente, quedando accesible en `http://localhost:8080`.
