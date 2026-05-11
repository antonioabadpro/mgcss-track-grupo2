package com.mgcss.api.controller;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infraestructure.repository.TecnicoRepository;
import com.mgcss.service.SolicitudService;
import com.mgcss.api.controller.SolicitudController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
// Utilizamos la libreria de Spring Boot para simular el comportamiento de las dependencias, NO la de Mockito directamente
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("api")
@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudService solicitudService; // Mockeamos el servicio

    @MockitoBean
    private TecnicoRepository tecnicoRepository;

    private Solicitud solicitudDummy;
    private Tecnico tecnicoDummy;

    @BeforeEach
    void setUp() {
        solicitudDummy = new Solicitud();
        solicitudDummy.setId(1L);
        solicitudDummy.setEstado(EstadoSolicitud.ABIERTA);

        tecnicoDummy = new Tecnico();
        tecnicoDummy.setId(10L);
    }

    @Test
    void testCrearSolicitud() throws Exception {
        when(solicitudService.crearSolicitud(any(Solicitud.class))).thenReturn(solicitudDummy);

        // Cumple: Verificar código HTTP (201) y estructura JSON
        mockMvc.perform(post("/api/solicitudes"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    @Test
    void testConsultarSolicitud() throws Exception {
        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitudDummy);

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

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
     * Manejamos los errores sin utilizar 'tecnicoId' en el request, lo que debería devolver un Bad Request (400).
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

    @Test
    void testCambiarEstado_Valido() throws Exception {
        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitudDummy);

        mockMvc.perform(put("/api/solicitudes/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\": \"EN_PROCESO\"}"))
                .andExpect(status().isOk());

        verify(solicitudService).cambiarEstado(solicitudDummy, EstadoSolicitud.EN_PROCESO);
    }

    @Test
    void testCambiarEstado_BadRequest() throws Exception {
        mockMvc.perform(put("/api/solicitudes/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReabrirSolicitud() throws Exception {
        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isOk());

        verify(solicitudService).reabrirSolicitud(1L);
    }

    @Test
    void testListarSolicitudes() throws Exception {
        when(solicitudService.listarSolicitudes()).thenReturn(Arrays.asList(solicitudDummy));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}