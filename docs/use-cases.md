# 🧪 Casos de Uso y Validación desde Swagger UI

Este documento detalla las pruebas realizadas desde la interfaz de OpenAPI (Swagger UI) para validar el contrato de la API y el cumplimiento de las reglas de negocio del dominio.

---

## Caso 1: Crear solicitud (Caso de éxito)
* **Objetivo:** Validar la inicialización correcta de una solicitud.
* **Endpoint:** `POST /api/solicitudes`
* **Request Body:** `{}` *(Cuerpo vacío)*
* **Resultado Esperado:** Código HTTP `201` y un JSON con la solicitud en estado `ABIERTA`.
  * **Resultado Obtenido:** Código HTTP: `201`
  * **Body Devuelto:** 
  
    ```
       {
        "id": 1,
        "estado": "ABIERTA",
        "fechaCreacion": "11/05/2026",
        "tecnicoId": null,
        "historicoEstados": [
          {
            "estado": "ABIERTA",
            "fechaCambio": "2026-05-11T16:49:31.2271552"
          }
        ]
      }
---

## Caso 2: Listar solicitudes (Caso de éxito)
* **Objetivo:** Comprobar que se recupera la lista de solicitudes del sistema.
* **Precondición:** Haber ejecutado el Caso 1 al menos una vez.
* **Endpoint:** `GET /api/solicitudes`
* **Resultado Esperado:** Código HTTP `200 OK` y un Array JSON `[...]` con al menos una solicitud.
  * **Resultado Obtenido:** Código HTTP: `200`
  * **Body Devuelto:** 
  
    ```
    [
      {
        "id": 1,
        "estado": "ABIERTA",
        "fechaCreacion": "11/05/2026",
        "tecnicoId": null,
        "historicoEstados": [
          {
            "estado": "ABIERTA",
            "fechaCambio": "2026-05-11T16:49:31.227155"
          }
        ]
      },
      {
        "id": 2,
        "estado": "ABIERTA",
        "fechaCreacion": "11/05/2026",
        "tecnicoId": null,
        "historicoEstados": [
          {
            "estado": "ABIERTA",
            "fechaCambio": "2026-05-11T16:53:10.437999"
          }
        ]
      },
      {
        "id": 3,
        "estado": "ABIERTA",
        "fechaCreacion": "11/05/2026",
        "tecnicoId": null,
        "historicoEstados": [
          {
            "estado": "ABIERTA",
            "fechaCambio": "2026-05-11T16:53:13.005562"
          }
        ]
      },
      {
        "id": 4,
        "estado": "ABIERTA",
        "fechaCreacion": "11/05/2026",
        "tecnicoId": null,
        "historicoEstados": [
          {
            "estado": "ABIERTA",
            "fechaCambio": "2026-05-11T16:53:13.460662"
          }
        ]
      }
    ]
---

## Caso 3: Consultar solicitud específica (Caso de éxito)
* **Objetivo:** Recuperar una solicitud concreta por su ID.
* **Precondición:** Haber ejecutado el Caso 1 al menos una vez.
* **Endpoint:** `GET /api/solicitudes/{id}`
* **Parámetro (id):** `1`
* **Resultado Esperado:** Código HTTP `200 OK` y el detalle de la solicitud número 2.
  * **Resultado Obtenido:** Código HTTP: `200`
  * **Body Devuelto:** 
  
    ```
    {
      "id": 2,
      "estado": "ABIERTA",
      "fechaCreacion": "11/05/2026",
      "tecnicoId": null,
      "historicoEstados": [
        {
          "estado": "ABIERTA",
          "fechaCambio": "2026-05-11T16:53:10.437999"
        }
      ]
    }

---

## Caso 4: Asignar Técnico (Caso de error)
* **Objetivo:** Comprobar que se puede vincular un técnico a una solicitud.
* **Precondición:** No existe un técnico válido con el id introducido en la base de datos
* **Endpoint:** `POST /api/solicitudes/{id}/asignar-tecnico`
* **Parámetro (id):** `10`
  * **Request Body:**
  
  ```
    {
      "tecnicoId": 10,
      "estado": "EN_PROCESO"
    }
    ```
    
  * **Resultado Obtenido:** Código HTTP: `500`
  * **Body Devuelto:** 
  
    ```
    {
      "timestamp": "2026-05-11T15:07:14.384+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "trace": "java.lang.IllegalArgumentException: El técnico no existe\r\n\tat com.mgcss.api.controller.SolicitudController.lambda$1(SolicitudController.java:98)\r\n\tat java.base/java.util.Optional.orElseThrow(Optional.java:403)\r\n\tat com.mgcss.api.controller.SolicitudController.asignarTecnico(SolicitudController.java:98)\r\n\tat java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)\r\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:580)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:258)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:191)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:991)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:896)\r\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)\r\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)\r\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:914)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590)\r\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:138)\r\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:162)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:138)\r\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:162)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:138)\r\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:162)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:138)\r\n\tat org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:110)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:162)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:138)\r\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:162)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:138)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:165)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:88)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:492)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:113)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:83)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:72)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)\r\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:399)\r\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:903)\r\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1797)\r\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:973)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:491)\r\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)\r\n\tat java.base/java.lang.Thread.run(Thread.java:1583)\r\n",
      "message": "El técnico no existe",
      "path": "/api/solicitudes/3/asignar-tecnico"
    }

---

## Caso 5: Cambiar estado de la solicitud (Pendiente de implementar)
* **Objetivo:** Validar la inicialización correcta de una solicitud.
* **Endpoint:** `POST /api/solicitudes/{id}/estado`
  * **Request Body:**
  
  ```
    {
      "tecnicoId": 5,
      "estado": "EN_PROCESO"
    }
    ```
* **Resultado Esperado:** Código HTTP `201` y un JSON con la solicitud en el estado indicado en el Request Body.
  * **Resultado Obtenido:** Código HTTP: `-`
  * **Body Devuelto:** 
  
    ```
    {}
---

## Caso 6: Reabrir solicitud (Pendiente de implementar)
* **Objetivo:** Validar la correcta reapertura de una solicitud que se encuentra en estado `CERRADA`.
* **Precondición:** Debe haber al menos 1 solicitud en estado `CERRADA`.
* **Endpoint:** `POST /api/solicitudes/{id}/reabrir`
* **Request Body:** `{}` *(Cuerpo vacío)*
* **Resultado Esperado:** Código HTTP `201` y un JSON con la solicitud en estado `ABIERTA`.
  * **Resultado Obtenido:** Código HTTP: `-`
  * **Body Devuelto:** 
  
    ```
    {}
---
