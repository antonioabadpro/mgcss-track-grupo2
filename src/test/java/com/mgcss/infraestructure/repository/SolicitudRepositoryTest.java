package com.mgcss.infraestructure.repository;

import com.mgcss.domain.Solicitud;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

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
    public void testGuardarYRecuperarSolicitud() {
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
}
