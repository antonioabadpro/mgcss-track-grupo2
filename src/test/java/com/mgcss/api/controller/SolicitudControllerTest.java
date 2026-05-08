package com.mgcss.api.controller;

import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infraestructure.repository.TecnicoRepository;
import com.mgcss.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudControllerTest {

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private SolicitudController solicitudController;

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
    void testCrearSolicitud() {
        when(solicitudService.crearSolicitud(any(Solicitud.class))).thenReturn(solicitudDummy);

        ResponseEntity<SolicitudResponseDTO> response = solicitudController.crearSolicitud();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(solicitudService).crearSolicitud(any(Solicitud.class));
    }

    @Test
    void testConsultarSolicitud() {
        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitudDummy);

        ResponseEntity<SolicitudResponseDTO> response = solicitudController.consultarSolicitud(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(solicitudService).consultarSolicitud(1L);
    }

    @Test
    void testAsignarTecnico_Valido() {
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        request.setTecnicoId(10L);

        when(tecnicoRepository.findById(10L)).thenReturn(Optional.of(tecnicoDummy));

        ResponseEntity<Void> response = solicitudController.asignarTecnico(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(solicitudService).asignarTecnico(1L, tecnicoDummy);
    }

    @Test
    void testAsignarTecnico_BadRequest() {
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        // sin tecnicoId

        ResponseEntity<Void> response = solicitudController.asignarTecnico(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(solicitudService, never()).asignarTecnico(anyLong(), any());
    }

    @Test
    void testCambiarEstado_Valido() {
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        request.setEstado(EstadoSolicitud.EN_PROCESO);

        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitudDummy);

        ResponseEntity<Void> response = solicitudController.cambiarEstado(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(solicitudService).cambiarEstado(solicitudDummy, EstadoSolicitud.EN_PROCESO);
    }

    @Test
    void testCambiarEstado_BadRequest() {
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        // sin estado

        ResponseEntity<Void> response = solicitudController.cambiarEstado(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(solicitudService, never()).cambiarEstado(any(), any());
    }

    @Test
    void testReabrirSolicitud() {
        ResponseEntity<Void> response = solicitudController.reabrirSolicitud(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(solicitudService).reabrirSolicitud(1L);
    }

    @Test
    void testListarSolicitudes() {
        when(solicitudService.listarSolicitudes()).thenReturn(Arrays.asList(solicitudDummy, new Solicitud()));

        ResponseEntity<List<SolicitudResponseDTO>> response = solicitudController.listarSolicitudes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(solicitudService).listarSolicitudes();
    }
}
