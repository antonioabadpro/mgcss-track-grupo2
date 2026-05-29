package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SolicitudResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getEstado());
        assertNull(dto.getFechaCreacion());
        assertNull(dto.getDescripcion());
        assertNull(dto.getTecnicoId());
        assertNull(dto.getClienteId());
        assertNull(dto.getFechaCierre());
        assertNull(dto.getHistoricoEstados());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        List<EstadoHistoricoDTO> historico = Collections.singletonList(new EstadoHistoricoDTO());
        SolicitudResponseDTO dto = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", "Problema con el proyector en el aula PQ 1.2", 2L, 3L, "15/12/2023", historico);
        
        assertEquals(1L, dto.getId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
        assertEquals("12/12/2023", dto.getFechaCreacion());
        assertEquals("Problema con el proyector en el aula PQ 1.2", dto.getDescripcion());
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(3L, dto.getClienteId());
        assertEquals("15/12/2023", dto.getFechaCierre());
        assertEquals(historico, dto.getHistoricoEstados());
    }

    @Test
    void testSetters() {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        List<EstadoHistoricoDTO> historico = Collections.singletonList(new EstadoHistoricoDTO());
        
        dto.setId(1L);
        dto.setEstado(EstadoSolicitud.ABIERTA);
        dto.setFechaCreacion("12/12/2023");
        dto.setDescripcion("Problema con el proyector en el aula PQ 1.2");
        dto.setTecnicoId(2L);
        dto.setClienteId(3L);
        dto.setFechaCierre("15/12/2023");
        dto.setHistoricoEstados(historico);
        
        assertEquals(1L, dto.getId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
        assertEquals("12/12/2023", dto.getFechaCreacion());
        assertEquals("Problema con el proyector en el aula PQ 1.2", dto.getDescripcion());
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(3L, dto.getClienteId());
        assertEquals("15/12/2023", dto.getFechaCierre());
        assertEquals(historico, dto.getHistoricoEstados());
    }

    @Test
    void testBuilder() {
        List<EstadoHistoricoDTO> historico = Collections.singletonList(new EstadoHistoricoDTO());
        SolicitudResponseDTO dto = SolicitudResponseDTO.builder()
                .id(1L)
                .estado(EstadoSolicitud.ABIERTA)
                .fechaCreacion("12/12/2023")
                .descripcion("Problema con el proyector en el aula PQ 1.2")
                .tecnicoId(2L)
                .clienteId(3L)
                .fechaCierre("15/12/2023")
                .historicoEstados(historico)
                .build();
                
        assertEquals(1L, dto.getId());
        assertEquals(EstadoSolicitud.ABIERTA, dto.getEstado());
        assertEquals("12/12/2023", dto.getFechaCreacion());
        assertEquals("Problema con el proyector en el aula PQ 1.2", dto.getDescripcion());
        assertEquals(2L, dto.getTecnicoId());
        assertEquals(3L, dto.getClienteId());
        assertEquals("15/12/2023", dto.getFechaCierre());
        assertEquals(historico, dto.getHistoricoEstados());
    }

    @Test
    void testEqualsAndHashCode() {
        SolicitudResponseDTO dto1 = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", null, 2L, 3L, null, null);
        SolicitudResponseDTO dto2 = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", null, 2L, 3L, null, null);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SolicitudResponseDTO dto = new SolicitudResponseDTO(1L, EstadoSolicitud.ABIERTA, "12/12/2023", null, 2L, 3L, null, null);
        String expected = "SolicitudResponseDTO(id=1, estado=ABIERTA, fechaCreacion=12/12/2023, descripcion=null, tecnicoId=2, clienteId=3, fechaCierre=null, historicoEstados=null)";
        assertEquals(expected, dto.toString());
    }
}
