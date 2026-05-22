package com.mgcss.api.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TecnicoResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        TecnicoResponseDTO dto = new TecnicoResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getNombre());
        assertEquals(0, dto.getEdad());
        assertFalse(dto.isEsActivo());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        TecnicoResponseDTO dto = new TecnicoResponseDTO(1L, "Antonio Abad Hernández", 21, true);
        assertEquals(1L, dto.getId());
        assertEquals("Antonio Abad Hernández", dto.getNombre());
        assertEquals(21, dto.getEdad());
        assertTrue(dto.isEsActivo());
    }

    @Test
    void testSetters() {
        TecnicoResponseDTO dto = new TecnicoResponseDTO();
        dto.setId(2L);
        dto.setNombre("Javier León");
        dto.setEdad(21);
        dto.setEsActivo(true);
        
        assertEquals(2L, dto.getId());
        assertEquals("Javier León", dto.getNombre());
        assertEquals(21, dto.getEdad());
        assertTrue(dto.isEsActivo());
    }

    @Test
    void testBuilder() {
        TecnicoResponseDTO dto = TecnicoResponseDTO.builder()
                .id(3L)
                .nombre("Daniel Marchena")
                .edad(23)
                .esActivo(true)
                .build();
                
        assertEquals(3L, dto.getId());
        assertEquals("Daniel Marchena", dto.getNombre());
        assertEquals(23, dto.getEdad());
        assertTrue(dto.isEsActivo());
    }

    @Test
    void testEqualsAndHashCode() {
        TecnicoResponseDTO dto1 = new TecnicoResponseDTO(1L, "Juan Pérez", 30, true);
        TecnicoResponseDTO dto2 = new TecnicoResponseDTO(1L, "Juan Pérez", 30, true);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TecnicoResponseDTO dto = new TecnicoResponseDTO(1L, "Juan Pérez", 30, true);
        String expected = "TecnicoResponseDTO(id=1, nombre=Juan Pérez, edad=30, esActivo=true)";
        assertEquals(expected, dto.toString());
    }
}