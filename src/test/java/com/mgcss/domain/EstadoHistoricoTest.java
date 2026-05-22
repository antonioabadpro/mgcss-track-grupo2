package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

@Tag("domain")
@SpringBootTest
class EstadoHistoricoTest {

    @Test
    void testConstructorPorDefectoYGetters() {
        EstadoHistorico historico = new EstadoHistorico();
        
        assertNull(historico.getId(), "El ID debe ser nulo por defecto.");
        assertNull(historico.getEstado(), "El estado debe ser nulo por defecto.");
        assertNull(historico.getFechaCambio(), "La fecha de cambio debe ser nula por defecto.");
    }

    @Test
    void testConstructorParametrizadoYGetters() {
        LocalDateTime ahora = LocalDateTime.now();
        EstadoHistorico historico = new EstadoHistorico(EstadoSolicitud.EN_PROCESO, ahora);
        
        assertNull(historico.getId(), "El ID debe ser nulo sin persistir.");
        assertEquals(EstadoSolicitud.EN_PROCESO, historico.getEstado(), "El estado debe ser el asignado.");
        assertEquals(ahora, historico.getFechaCambio(), "La fecha debe ser la asignada.");
    }
}
