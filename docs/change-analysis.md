# Análisis de Cambio: Reapertura e Histórico de Solicitudes

En base a la descripción funcional proporcionada para la nueva funcionalidad ("Change Request"), a continuación se responden las preguntas clave del análisis:

### 1. ¿Qué métodos del dominio se ven afectados?
- **En `Solicitud.java`**: Se añadirá un nuevo método `reabrirSolicitud()`. Además, los métodos u operaciones que gestionan las transiciones de estado deberán contemplar su registro en el histórico.
- **En `EstadoSolicitud.java`**: Se verán afectadas las validaciones de compatibilidad de estados, ya que ahora deberá permitirse la transición desde un estado `CERRADA` a estados gestionables como `ABIERTA` o `EN_PROCESO`.

### 2. ¿Qué reglas actuales cambian?
- **Inmutabilidad en estado cerrado**: La regla de negocio que dictaba que "una vez que una solicitud alcanza el estado CERRADA, no es posible realizar más acciones sobre ella" se flexibiliza para permitir la acción específica de reapertura.
- **Trazabilidad obligatoria (Nueva regla)**: Se introduce la obligación de registrar automáticamente todo cambio de estado que sufra una solicitud en su ciclo de vida.

### 3. ¿Qué tests deberían romperse?
- **Tests de validación de estados**: Los tests que comprueben que una solicitud `CERRADA` lanza excepciones (o rechaza cambios) al intentar ser modificada probablemente fallen debido a las nuevas transiciones permitidas.
- **Tests unitarios del `SolicitudService`**: Aquellos tests que evalúen el comportamiento de procesar o cerrar solicitudes mediante _mocks_ estrictos (ej. verificando llamadas a repositorios) fallarán si el servicio comienza a invocar la persistencia del histórico en esos mismos flujos.

### 4. ¿Qué parte del modelo debe extenderse?
- **Nueva Entidad `EstadoHistorico`**: Se debe crear esta nueva clase en el modelo para almacenar los detalles de la transición (fecha, estados involucrados, etc.).
- **Entidad `Solicitud`**: Se debe extender añadiendo una relación (como una colección o lista) hacia sus registros de `EstadoHistorico`.

### 5. ¿Qué impacto tiene en persistencia?
- **Nueva tabla en base de datos**: Se requiere la creación de una nueva tabla correspondiente a la entidad `EstadoHistorico`, enlazada mediante una clave foránea con la tabla de las solicitudes (relación 1:N).
- **Rendimiento e indexación**: El volumen de los datos aumentará sistemáticamente. Es vital asegurar la creación de índices, en particular sobre el ID de la solicitud en la nueva tabla, para que la consulta del histórico sea eficiente.

### 6. Diseño de la estructura del histórico
**Opción seleccionada**: Entidad separada `EstadoHistorico`.
**Justificación**: 
1. **Atributos complejos**: Necesitamos almacenar no solo el estado, sino también la fecha/hora en la que ocurrió el cambio (y potencialmente el técnico que lo hizo en el futuro). Una lista interna simple o un _Value Object_ se quedarían cortos si necesitamos escalar esta información.
2. **Consultas independientes**: Al ser una entidad separada (mapeada a su propia tabla en la base de datos), permite realizar consultas analíticas o de auditoría más eficientes (ej. "¿Cuántas solicitudes pasaron a CERRADA hoy?") sin tener que cargar la entidad raíz `Solicitud`.
3. **Escalabilidad y buenas prácticas JPA**: Modela correctamente una relación 1:N donde el ciclo de vida del histórico pertenece a la solicitud, manteniendo una estructura de base de datos relacional limpia e indexable.
