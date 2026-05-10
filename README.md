# MGCSS-TRACK-GRUPO2

Este repositorio contiene el proyecto de seguimiento de servicio técnico desarrollado para la asignatura MGCSS (Mantenimiento y Gestión del Cambio en Sistemas Software).

---

## 👥 Equipo de Trabajo
* **Antonio Abad Hernández Gálvez**
* **Javier León Castañeda**
* **Daniel Marchena Jiménez**

---

## 📁 Estructura del Proyecto

El proyecto sigue una Arquitectura Limpia estructurada para separar el Dominio (Lógica de Negocio) de la Infraestructura y los Servicios.

```text
📦 MGCSS-TRACK-GRUPO2
├── 📂 .github
│   └── 📂 workflows
│       └── 📄 ci.yml                  # Pipeline de Integración Continua (GitHub Actions)
├── 📂 docs                            # Documentación técnica y entregables
│   ├── 📄 refactor-notes.md           # Notas de refactorización y métricas (Sesión 8)
│   └── 📄 change-analysis.md          # Análisis de impacto de nuevos requisitos (Sesión 9)
├── 📂 src
│   ├── 📂 main/java/com/mgcss
│   │   ├── 📂 api                     # Controladores y endpoints REST
│   │   ├── 📂 domain                  # Lógica de negocio pura y entidades
│   │   │   ├── 📄 EstadoSolicitud.java
│   │   │   ├── 📄 Solicitud.java
│   │   │   └── 📄 Tecnico.java
│   │   ├── 📂 infraestructure         # Adaptadores externos y persistencia
│   │   │   └── 📂 repository
│   │   │       ├── 📄 SolicitudRepository.java
│   │   │       └── 📄 TecnicoRepository.java
│   │   ├── 📂 service                 # Orquestación y casos de uso
│   │   │   └── 📄 SolicitudService.java
│   │   └── 📄 MgcssTrackGrupo2Application.java  # Clase principal de Spring Boot
│   │
│   └── 📂 test/java/com/mgcss
│       ├── 📂 domain                  # Tests unitarios del dominio (Rápidos)
│       │   ├── 📄 SolicitudTests.java
│       │   └── 📄 TecnicoTests.java
│       └── 📂 infraestructure         # Tests de integración con base de datos H2
│           └── 📂 repository
│               └── 📄 SolicitudRepositoryTest.java
├── 📄 .gitignore                      # Archivos excluidos del control de versiones
├── 📄 pom.xml                         # Configuración de Maven y dependencias
└── 📄 README.md                       # Información principal del repositorio
