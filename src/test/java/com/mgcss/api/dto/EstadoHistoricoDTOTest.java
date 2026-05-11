package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EstadoHistoricoDTOTest {

    @Test
    void testNoArgsConstructor() {
        EstadoHistoricoDTO dto = new EstadoHistoricoDTO();
        assertNull(dto.getEstado());
        assertNull(dto.getFechaCambio());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        EstadoHistoricoDTO dto = new EstadoHistoricoDTO(EstadoSolicitud.ABIERTA, now);
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
        assertEquals(now, dto.getFechaCambio());
    }

    @Test
    void testSetters() {
        EstadoHistoricoDTO dto = new EstadoHistoricoDTO();
        LocalDateTime now = LocalDateTime.now();
        dto.setEstado(EstadoSolicitud.EN_PROCESO);
        dto.setFechaCambio(now);
        
        assertEquals(EstadoSolicitud.EN_PROCESO, dto.getEstado());
        assertEquals(now, dto.getFechaCambio());
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        EstadoHistoricoDTO dto = EstadoHistoricoDTO.builder()
                .estado(EstadoSolicitud.CERRADA)
                .fechaCambio(now)
                .build();
                
        assertEquals(EstadoSolicitud.CERRADA, dto.getEstado());
        assertEquals(now, dto.getFechaCambio());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        EstadoHistoricoDTO dto1 = new EstadoHistoricoDTO(EstadoSolicitud.ABIERTA, now);
        EstadoHistoricoDTO dto2 = new EstadoHistoricoDTO(EstadoSolicitud.ABIERTA, now);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        EstadoHistoricoDTO dto = new EstadoHistoricoDTO(EstadoSolicitud.ABIERTA, now);
        String expected = "EstadoHistoricoDTO(estado=ABIERTA, fechaCambio=" + now.toString() + ")";
        assertEquals(expected, dto.toString());
    }
}
