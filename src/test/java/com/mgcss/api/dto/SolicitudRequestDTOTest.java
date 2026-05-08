package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SolicitudRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO();
        assertNull(dto.getTecnicoId());
        assertNull(dto.getEstado());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO(1L, EstadoSolicitud.EN_PROCESO);
        assertEquals(1L, dto.getTecnicoId());
        assertEquals(EstadoSolicitud.EN_PROCESO, dto.getEstado());
    }

    @Test
    void testSetters() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO();
        dto.setTecnicoId(2L);
        dto.setEstado(EstadoSolicitud.CERRADA);
        
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(EstadoSolicitud.CERRADA, dto.getEstado());
    }

    @Test
    void testBuilder() {
        SolicitudRequestDTO dto = SolicitudRequestDTO.builder()
                .tecnicoId(3L)
                .estado(EstadoSolicitud.ABIERTA)
                .build();
                
        assertEquals(3L, dto.getTecnicoId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
    }

    @Test
    void testEqualsAndHashCode() {
        SolicitudRequestDTO dto1 = new SolicitudRequestDTO(1L, EstadoSolicitud.ABIERTA);
        SolicitudRequestDTO dto2 = new SolicitudRequestDTO(1L, EstadoSolicitud.ABIERTA);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO(1L, EstadoSolicitud.ABIERTA);
        String expected = "SolicitudRequestDTO(tecnicoId=1, estado=ABIERTA)";
        assertEquals(expected, dto.toString());
    }
}
