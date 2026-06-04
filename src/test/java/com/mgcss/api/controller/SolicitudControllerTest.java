package com.mgcss.api.controller;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.Cliente;
import com.mgcss.infraestructure.repository.TecnicoRepository;
import com.mgcss.infraestructura.repository.ClienteRepository;
import com.mgcss.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
// Utilizamos la libreria de Spring Boot para simular el comportamiento de las dependencias, NO la de Mockito directamente
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Tag("api")
@WebMvcTest(value = SolicitudController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudService solicitudService; // Mockeamos el servicio

    @MockitoBean
    private TecnicoRepository tecnicoRepository;

    @MockitoBean
    private ClienteRepository clienteRepository;

    private Solicitud solicitudDummy;
    private Tecnico tecnicoDummy;
    private Cliente clienteDummy;

    @BeforeEach
    void setUp() {
        solicitudDummy = new Solicitud();
        solicitudDummy.setId(1L);
        solicitudDummy.setEstado(EstadoSolicitud.ABIERTA);

        tecnicoDummy = new Tecnico();
        tecnicoDummy.setId(10L);

        clienteDummy = new Cliente();
        clienteDummy.setId(5L);
        
        solicitudDummy.setCliente(clienteDummy);
    }

    /**
     * Este test verifica que al crear una solicitud, el controlador devuelve un código HTTP 201 (Created) y la estructura JSON esperada.
     * @throws Exception
     */
    @Test
    void testCrearSolicitud() throws Exception {
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(clienteDummy));
        when(solicitudService.crearSolicitud(any(Solicitud.class))).thenReturn(solicitudDummy);

        // Cumple: Verificar código HTTP (201) y estructura JSON
        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clienteId\": 5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    /**
     * Este test verifica que al consultar una solicitud, el controlador devuelve un código HTTP 200 (OK) y la estructura JSON esperada.
     * @throws Exception
     */
    @Test
    void testConsultarSolicitud() throws Exception {
        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitudDummy);

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    /**
     * Este test verifica que al asignar un técnico a una solicitud, el controlador devuelve un código HTTP 200 (OK) y que el servicio de asignación es llamado correctamente.
     * @throws Exception
     */
    @Test
    void testAsignarTecnico_Valido() throws Exception {
        when(tecnicoRepository.findById(10L)).thenReturn(Optional.of(tecnicoDummy));

        mockMvc.perform(post("/api/solicitudes/1/asignar-tecnico")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tecnicoId\": 10}"))
                .andExpect(status().isOk());

        verify(solicitudService).asignarTecnico(1L, tecnicoDummy);
    }

    /**
     * Este test verifica que al intentar asignar un técnico a una solicitud sin proporcionar el ID del técnico, el controlador devuelve un código HTTP 400 (Bad Request).
     * @throws Exception
     */
    @Test
    void testAsignarTecnico_BadRequest() throws Exception {
        mockMvc.perform(post("/api/solicitudes/1/asignar-tecnico")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        verify(solicitudService, never()).asignarTecnico(anyLong(), any());
    }

    /**
     * Este test verifica que al cambiar el estado de una solicitud, el controlador devuelve un código HTTP 200 (OK) y que el servicio de cambio de estado es llamado correctamente.
     * @throws Exception
     */
    @Test
    void testCambiarEstado_Valido() throws Exception {
        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitudDummy);

        mockMvc.perform(put("/api/solicitudes/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\": \"EN_PROCESO\"}"))
                .andExpect(status().isOk());

        verify(solicitudService).cambiarEstado(solicitudDummy, EstadoSolicitud.EN_PROCESO);
    }

    /**
     * Este test verifica que al intentar cambiar el estado de una solicitud sin proporcionar el nuevo estado, el controlador devuelve un código HTTP 400 (Bad Request).
     * @throws Exception
     */
    @Test
    void testCambiarEstado_BadRequest() throws Exception {
        mockMvc.perform(put("/api/solicitudes/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Este test verifica que al reabrir una solicitud, el controlador devuelve un código HTTP 200 (OK) y que el servicio de reapertura es llamado correctamente.
     * @throws Exception
     */
    @Test
    void testReabrirSolicitud() throws Exception {
        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isOk());

        verify(solicitudService).reabrirSolicitud(1L);
    }

    /**
     * Este test verifica que al listar solicitudes, el controlador devuelve un código HTTP 200 (OK) y la estructura JSON esperada.
     * @throws Exception
     */
    @Test
    void testListarSolicitudes() throws Exception {
        when(solicitudService.listarSolicitudes()).thenReturn(Arrays.asList(solicitudDummy));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    /**
     * Este test verifica que al intentar cambiar el estado de una solicitud a un estado no permitido, el servicio lanza una IllegalStateException y el controlador devuelve un código HTTP 400 (Bad Request).
     * @throws Exception
     */
    @Test
    void testCambiarEstado_cambiarSolicitudEstadoNoPermitido() throws Exception {
        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitudDummy);
        doThrow(new IllegalStateException("No se puede cerrar una solicitud ABIERTA"))
                .when(solicitudService).cambiarEstado(solicitudDummy, EstadoSolicitud.CERRADA);

        mockMvc.perform(put("/api/solicitudes/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\": \"CERRADA\"}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Este test verifica que al intentar asignar un técnico que no existe a una solicitud, el servicio lanza una IllegalArgumentException y el controlador devuelve un código HTTP 404 (Not Found) o 400 (Bad Request) según la implementación.
     * @throws Exception
     */
    @Test
    void testAsignarTecnico_tecnicoNoExistenteEnSolicitud() throws Exception {
        when(tecnicoRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/solicitudes/1/asignar-tecnico")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tecnicoId\": 99}"))
                .andExpect(status().isBadRequest()); // o isBadRequest(), según como lo implementes
    }

    /**
     * Este test verifica que al intentar consultar una solicitud que no existe, el servicio lanza una NoSuchElementException y el controlador devuelve un código HTTP 404 (Not Found).
     * @throws Exception
     */
    @Test
    void testConsultarSolicitud_solicitudNoExistente() throws Exception {
        when(solicitudService.consultarSolicitud(99L))
                .thenThrow(new NoSuchElementException("Solicitud no encontrada"));

        mockMvc.perform(get("/api/solicitudes/99"))
                .andExpect(status().isNotFound());
    }

    /**
     * Este test verifica que se pueda crear una solicitud especificando una descripción.
     */
    @Test
    void testCrearSolicitud_ConDescripcion() throws Exception {
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(clienteDummy));
        when(solicitudService.crearSolicitud(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud input = invocation.getArgument(0);
            input.setId(1L);
            return input;
        });

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\": \"Ordenador no enciende\", \"clienteId\": 5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Ordenador no enciende"));
    }

    /**
     * Este test verifica que se pueda crear una solicitud asignando directamente un técnico existente y válido.
     */
    @Test
    void testCrearSolicitud_ConTecnicoValido() throws Exception {
        tecnicoDummy.setTecnicoActivo();
        when(tecnicoRepository.findById(10L)).thenReturn(Optional.of(tecnicoDummy));
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(clienteDummy));
        when(solicitudService.crearSolicitud(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud input = invocation.getArgument(0);
            input.setId(1L);
            return input;
        });

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tecnicoId\": 10, \"clienteId\": 5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tecnicoId").value(10L))
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    /**
     * Este test verifica que al intentar crear una solicitud asignando un técnico inexistente, devuelva 400 Bad Request.
     */
    @Test
    void testCrearSolicitud_ConTecnicoInexistente() throws Exception {
        when(tecnicoRepository.findById(99L)).thenReturn(Optional.empty());
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(clienteDummy));

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tecnicoId\": 99, \"clienteId\": 5}"))
                .andExpect(status().isBadRequest());
    }
}