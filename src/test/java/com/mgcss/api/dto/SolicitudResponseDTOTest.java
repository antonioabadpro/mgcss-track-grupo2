package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SolicitudResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getEstado());
        assertNull(dto.getFechaCreacion());
        assertNull(dto.getTecnicoId());
        assertNull(dto.getHistoricoEstados());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        List<EstadoHistoricoDTO> historico = Collections.singletonList(new EstadoHistoricoDTO());
        SolicitudResponseDTO dto = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", 2L, historico);
        
        assertEquals(1L, dto.getId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
        assertEquals("12/12/2023", dto.getFechaCreacion());
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(historico, dto.getHistoricoEstados());
    }

    @Test
    void testSetters() {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        List<EstadoHistoricoDTO> historico = Collections.singletonList(new EstadoHistoricoDTO());
        
        dto.setId(1L);
        dto.setEstado(EstadoSolicitud.ABIERTA);
        dto.setFechaCreacion("12/12/2023");
        dto.setTecnicoId(2L);
        dto.setHistoricoEstados(historico);
        
        assertEquals(1L, dto.getId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
        assertEquals("12/12/2023", dto.getFechaCreacion());
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(historico, dto.getHistoricoEstados());
    }

    @Test
    void testBuilder() {
        List<EstadoHistoricoDTO> historico = Collections.singletonList(new EstadoHistoricoDTO());
        SolicitudResponseDTO dto = SolicitudResponseDTO.builder()
                .id(1L)
                .estado(EstadoSolicitud.ABIERTA)
                .fechaCreacion("12/12/2023")
                .tecnicoId(2L)
                .historicoEstados(historico)
                .build();
                
        assertEquals(1L, dto.getId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
        assertEquals("12/12/2023", dto.getFechaCreacion());
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(historico, dto.getHistoricoEstados());
    }

    @Test
    void testEqualsAndHashCode() {
        SolicitudResponseDTO dto1 = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", 2L, null);
        SolicitudResponseDTO dto2 = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", 2L, null);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SolicitudResponseDTO dto = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", 2L, null);
        String expected = "SolicitudResponseDTO(id=1, estado=ABIERTA, fechaCreacion=12/12/2023, tecnicoId=2, historicoEstados=null)";
        assertEquals(expected, dto.toString());
    }
}
