package com.mgcss.api.dto;

import com.mgcss.domain.TipoCliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClienteDTOTest {

    @Test
    public void testSettersAndGetters() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(1L);
        dto.setNombre("Test");
        dto.setEmail("test@test.com");
        dto.setTipoCliente(TipoCliente.PREMIUM);

        assertEquals(1L, dto.getId());
        assertEquals("Test", dto.getNombre());
        assertEquals("test@test.com", dto.getEmail());
        assertEquals(TipoCliente.PREMIUM, dto.getTipoCliente());
    }
}
