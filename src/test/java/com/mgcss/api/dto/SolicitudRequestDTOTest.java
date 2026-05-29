package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SolicitudRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO();
        assertNull(dto.getDescripcion());
        assertNull(dto.getTecnicoId());
        assertNull(dto.getClienteId());
        assertNull(dto.getEstado());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO("El proyector del aula PQ 1.2 no enciende",1L, 5L, EstadoSolicitud.EN_PROCESO);
        assertEquals("El proyector del aula PQ 1.2 no enciende", dto.getDescripcion());
        assertEquals(1L, dto.getTecnicoId());
        assertEquals(5L, dto.getClienteId());
        assertEquals(EstadoSolicitud.EN_PROCESO, dto.getEstado());
    }

    @Test
    void testSetters() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO();
        dto.setDescripcion("El proyector del aula PQ 1.2 no enciende");
        dto.setTecnicoId(2L);
        dto.setClienteId(3L);
        dto.setEstado(EstadoSolicitud.CERRADA);
        
        assertEquals("El proyector del aula PQ 1.2 no enciende", dto.getDescripcion());
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(3L, dto.getClienteId());
        assertEquals(EstadoSolicitud.CERRADA, dto.getEstado());
    }

    @Test
    void testBuilder() {
        SolicitudRequestDTO dto = SolicitudRequestDTO.builder()
                .descripcion("El proyector del aula PQ 1.2 no enciende")
                .tecnicoId(3L)
                .clienteId(4L)
                .estado(EstadoSolicitud.ABIERTA)
                .build();
                
        assertEquals("El proyector del aula PQ 1.2 no enciende", dto.getDescripcion());
        assertEquals(3L, dto.getTecnicoId());
        assertEquals(4L, dto.getClienteId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
    }

    @Test
    void testEqualsAndHashCode() {
        SolicitudRequestDTO dto1 = new SolicitudRequestDTO(null, 1L, 2L, EstadoSolicitud.ABIERTA);
        SolicitudRequestDTO dto2 = new SolicitudRequestDTO(null, 1L, 2L, EstadoSolicitud.ABIERTA);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO("El proyector del aula PQ 1.2 no enciende", 1L, 2L, EstadoSolicitud.ABIERTA);
        String expected = "SolicitudRequestDTO(descripcion=El proyector del aula PQ 1.2 no enciende, tecnicoId=1, clienteId=2, estado=ABIERTA)";
        assertEquals(expected, dto.toString());
    }
}
