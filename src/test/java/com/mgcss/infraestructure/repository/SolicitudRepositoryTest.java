package com.mgcss.infraestructure.repository;

import com.mgcss.domain.EstadoHistorico;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
@DataJpaTest
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;

    /**
     * Este test verifica que una solicitud pueda ser guardada y recuperada correctamente de la base de datos.
     */
    @Test
    void testGuardarYRecuperarSolicitud() {
        // Guardar entidad
        Solicitud solicitud = new Solicitud();
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
        
        // Verificar integridad de que se guardó y tiene ID asignado
        assertNotNull(solicitudGuardada.getId(), "El ID no debería ser nulo tras guardar");

        // Recuperarla
        Optional<Solicitud> solicitudRecuperada = solicitudRepository.findById(solicitudGuardada.getId());
        
        // Verificar integridad
        assertTrue(solicitudRecuperada.isPresent(), "La solicitud debería encontrarse en la base de datos");
        assertEquals(solicitudGuardada.getId(), solicitudRecuperada.get().getId(), "Los IDs deben coincidir");
        assertEquals(solicitudGuardada.getEstado(), solicitudRecuperada.get().getEstado(), "Los estados deben coincidir");
        assertEquals(solicitudGuardada.getFechaCreacion(), solicitudRecuperada.get().getFechaCreacion(), "Las fechas deben coincidir");
    }

    /**
     * Este test verifica que el historial de estados de una solicitud se persista y recupere correctamente.
     */
    @Test
    void testGuardarYRecuperarHistoricoEstados() {
        // Guardar entidad
        Solicitud solicitud = new Solicitud(); // Crea histórico con estado ABIERTA
        solicitud.procesarSolicitud();         // Añade estado EN_PROCESO
        solicitud.cerrarSolicitud();           // Añade estado CERRADA
        
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
        
        // Recuperarla
        Optional<Solicitud> solicitudRecuperada = solicitudRepository.findById(solicitudGuardada.getId());
        
        // Verificar integridad del histórico
        assertTrue(solicitudRecuperada.isPresent(), "La solicitud debería encontrarse en la base de datos");
        
        List<EstadoHistorico> historico = solicitudRecuperada.get().getHistoricoEstados();
        assertNotNull(historico, "El histórico no debería ser nulo");
        assertEquals(3, historico.size(), "Debería haber 3 estados en el histórico (ABIERTA, EN_PROCESO, CERRADA)");
        
        // Verificar el orden y los estados
        assertEquals(EstadoSolicitud.ABIERTA, historico.get(0).getEstado(), "El primer estado debe ser ABIERTA");
        assertEquals(EstadoSolicitud.EN_PROCESO, historico.get(1).getEstado(), "El segundo estado debe ser EN_PROCESO");
        assertEquals(EstadoSolicitud.CERRADA, historico.get(2).getEstado(), "El tercer estado debe ser CERRADA");
        
        // Verificar que se haya guardado la fecha de cambio
        assertNotNull(historico.get(0).getFechaCambio(), "La fecha de cambio no debería ser nula");
        assertNotNull(historico.get(1).getFechaCambio(), "La fecha de cambio no debería ser nula");
        assertNotNull(historico.get(2).getFechaCambio(), "La fecha de cambio no debería ser nula");
    }
}
