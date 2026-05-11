package com.mgcss.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.infraestructure.repository.SolicitudRepository;

@Tag("service")
@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private SolicitudService solicitudService;

    /**
     * Este test verifica que un técnico pueda ser asignado a una solicitud
     * correctamente.
     */
    @Test
    void testAsignarTecnico_Valido() {
        // 1. Crear mock de SolicitudRepository (ya hecho con @Mock)
        Long solicitudId = 1L;
        Solicitud solicitud = new Solicitud();
        solicitud.setId(solicitudId);
        solicitud.setEstado(EstadoSolicitud.ABIERTA);

        Tecnico tecnico = new Tecnico();
        tecnico.setId(10L);
        tecnico.setTecnicoActivo();

        // 2. Simular findById
        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitud));

        // 3. Ejecutar método del servicio
        solicitudService.asignarTecnico(solicitudId, tecnico);

        // 4. Verificar:
        // a. Que save() fue invocado.
        verify(solicitudRepository).save(solicitud);
        // b. Que el estado cambió correctamente
        assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstado());
        assertEquals(tecnico, solicitud.getTecnico());
    }

    /**
     * Este test verifica que no se pueda asignar un técnico a una solicitud que no
     * existe.
     */
    @Test
    void testAsignarTecnico_SolicitudNoExiste_LanzaExcepcion() {
        Long solicitudId = 99L;
        Tecnico tecnico = new Tecnico();
        tecnico.setId(10L);
        tecnico.setTecnicoActivo();

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.asignarTecnico(solicitudId, tecnico);
        });

        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    /**
     * Este test verifica que crearSolicitud guarda y retorna la solicitud.
     */
    @Test
    void testCrearSolicitud() {
        Solicitud solicitud = new Solicitud();
        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);

        Solicitud resultado = solicitudService.crearSolicitud(solicitud);

        assertEquals(solicitud, resultado, "La solicitud devuelta debe ser la misma que se guardó");
        verify(solicitudRepository).save(solicitud);
    }

    /**
     * Este test verifica que cambiarEstado actualiza el estado de la solicitud y
     * llama a save.
     */
    @Test
    void testCambiarEstado() {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(EstadoSolicitud.ABIERTA);

        solicitudService.cambiarEstado(solicitud, EstadoSolicitud.EN_PROCESO);

        assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstado(), "El estado debe haber cambiado a EN_PROCESO");
        verify(solicitudRepository).save(solicitud);
    }

    @Test
    void testConsultarSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        Solicitud resultado = solicitudService.consultarSolicitud(1L);

        assertEquals(solicitud, resultado);
        verify(solicitudRepository).findById(1L);
    }

    @Test
    void testConsultarSolicitud_NoExiste() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> solicitudService.consultarSolicitud(1L));
    }

    @Test
    void testListarSolicitudes() {
        java.util.List<Solicitud> lista = java.util.Arrays.asList(new Solicitud(), new Solicitud());
        when(solicitudRepository.findAll()).thenReturn(lista);

        java.util.List<Solicitud> resultado = solicitudService.listarSolicitudes();

        assertEquals(2, resultado.size());
        verify(solicitudRepository).findAll();
    }

    @Test
    void testReabrirSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEstado(EstadoSolicitud.CERRADA);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        solicitudService.reabrirSolicitud(1L);

        assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstado());
        verify(solicitudRepository).save(solicitud);
    }
}