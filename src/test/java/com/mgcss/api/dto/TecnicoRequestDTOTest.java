package com.mgcss.api.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TecnicoRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        TecnicoRequestDTO dto = new TecnicoRequestDTO();
        assertNull(dto.getNombre());
        assertEquals(0, dto.getEdad());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        TecnicoRequestDTO dto = new TecnicoRequestDTO("Antonio Abad Hernández", 21);
        assertEquals("Antonio Abad Hernández", dto.getNombre());
        assertEquals(21, dto.getEdad());
    }

    @Test
    void testSetters() {
        TecnicoRequestDTO dto = new TecnicoRequestDTO();
        dto.setNombre("Javier León");
        dto.setEdad(21);
        
        assertEquals("Javier León", dto.getNombre());
        assertEquals(21, dto.getEdad());
    }

    @Test
    void testBuilder() {
        TecnicoRequestDTO dto = TecnicoRequestDTO.builder()
                .nombre("Daniel Marchena")
                .edad(23)
                .build();
                
        assertEquals("Daniel Marchena", dto.getNombre());
        assertEquals(23, dto.getEdad());
    }

    @Test
    void testEqualsAndHashCode() {
        TecnicoRequestDTO dto1 = new TecnicoRequestDTO("Juan Pérez", 30);
        TecnicoRequestDTO dto2 = new TecnicoRequestDTO("Juan Pérez", 30);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TecnicoRequestDTO dto = new TecnicoRequestDTO("Juan Pérez", 30);
        String expected = "TecnicoRequestDTO(nombre=Juan Pérez, edad=30)";
        assertEquals(expected, dto.toString());
    }
}