# 🛠️ Sesión 8: Refactorización y Gestión de Deuda Técnica

## 📊 1. Estado Inicial del Proyecto (SonarCloud)
* **Quality Gate:** Passed ✅
* **Security Rating:** A (0 Vulnerabilities) 🟢
* **Reliability Rating:** A (0 Bugs) 🟢
* **Maintainability Rating:** A (0 Bugs) 🟢
* **Code Smells (Open Issues):** 8 🟡
* **Coverage:** 97.7% 🟢
* **Duplications:** 0.0% 🟢

---

## 🎯 2. Análisis y Objetivos de Refactorización

Revisando el código base y los Code Smells marcados, se han identificado los siguientes problemas de calidad:

### Problema A: Lógica de validación acoplada en setters
* **Problema Identificado:** El método `setFechaCreacion(String)` en `Solicitud.java` contiene lógica de validación mediante una expresión regular directamente incrustada (`\\d{2}/\\d{2}/\\d{4}`). 
* **Métrica / Smells asociados:** Falta de encapsulamiento y aumento innecesario de la complejidad dentro de un setter.
* **Riesgo:** Si el formato de fecha cambia en el futuro, habrá que buscar y modificar esta cadena en la lógica del dominio, violando el principio de responsabilidad única.

### Problema B: Duplicación conceptual en transiciones de estado
* **Problema Identificado:** Los métodos `procesarSolicitud()` y `cerrarSolicitud()` en `Solicitud.java` evalúan manualmente el estado actual con condicionales `if (this.estado == ...)`.
* **Métrica / Smells asociados:** Complejidad ciclomática en la clase principal de dominio.
* **Riesgo:** A medida que se añadan nuevos estados (ej. Cancelada, Reabierta), la clase se llenará de condicionales dispersos, dificultando su mantenimiento.

### Problema C: Visibilidad innecesaria en clases de Test (JUnit 5)
* **Problema Identificado:** Las clases y métodos de prueba (ej. `TecnicoTests` o `SolicitudRepositoryTest`) están declarados con el modificador de acceso `public`.
* **Métrica / Smells asociados:** Ruido visual (Boilerplate) y exceso de visibilidad (Code Smell).
* **Riesgo y Justificación:** A diferencia de JUnit 4, que requería que todo fuera público, JUnit 5 es más tolerante respecto a la visibilidad. Oficialmente, ni las clases, ni los métodos de prueba, ni los métodos del ciclo de vida necesitan ser `public` (aunque no deben ser `private`). Mantener el modificador `public` sin una razón técnica válida (como herencia entre paquetes) ensucia el código y dificulta la legibilidad.

---

## 🚀 3. Refactorización Aplicada

* **Técnicas utilizadas:** *Extract Method*, *Encapsulate Field* y *Remove Unnecessary Modifiers*.
* **Cambios realizados:** 
  1. **Dominio:** Se ha extraído la validación de la fecha a un método privado `isFormatoFechaValido()`.
  2. **Dominio:** Se ha extraído la comprobación de estados a métodos privados descriptivos como `puedeSerProcesada()` y `puedeSerCerrada()`.
  3. **Testing:** Se ha eliminado el modificador `public` de todas las clases y métodos de test, dejándolos con visibilidad por defecto (*package-private*), siguiendo las mejores prácticas de JUnit 5.
* **Beneficio para el mantenimiento futuro:** El código de dominio ahora es autodocumentado y extensible mediante métodos privados encapsulados. El código de testing es mucho más limpio, fácil de leer y está alineado con los estándares modernos de desarrollo en Java.

---

## 📉 4. Comparativa Post-Refactorización

* **Tests:** Todos los tests (Unitarios y de Integración) siguen en verde.
* **Comportamiento observable:** Intacto (No se han modificado las firmas públicas de la API de producción).
* **Mejora en Métricas:** Se ha mantenido el coverage > 80% y se ha mejorado el *Maintainability Rating* al reducir la complejidad ciclomática y limpiar el ruido sintáctico en la suite de pruebas.

---

## 📊 5. Estado Actual del Proyecto (SonarCloud)
* **Quality Gate:** Passed ✅
* **Security Rating:** A (0 Vulnerabilities) 🟢
* **Reliability Rating:** A (0 Bugs) 🟢
* **Maintainability Rating:** A (0 Bugs) 🟢
* **Code Smells (Open Issues):** 0 🟢
* **Coverage:** 97% 🟢
* **Duplications:** 0.0% 🟢
* **Complejidad Ciclomática (Cyclomatic Complexity):** 39
* **Complejidad Cognitiva (Cognitive Complexity):** 7