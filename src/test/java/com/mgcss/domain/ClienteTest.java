package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClienteTest {

    @Test
    public void testClienteDefaultConstructor() {
        Cliente cliente = new Cliente();
        assertEquals(TipoCliente.STANDARD, cliente.getTipoCliente());
        assertNull(cliente.getNombre());
    }

    @Test
    public void testClienteConstructorWithArgs() {
        Cliente cliente = new Cliente("Test", "test@test.com", TipoCliente.PREMIUM);
        assertEquals("Test", cliente.getNombre());
        assertEquals("test@test.com", cliente.getEmail());
        assertEquals(TipoCliente.PREMIUM, cliente.getTipoCliente());
    }

    @Test
    public void testClienteConstructorWithArgsNullType() {
        Cliente cliente = new Cliente("Test", "test@test.com", null);
        assertEquals(TipoCliente.STANDARD, cliente.getTipoCliente());
    }

    @Test
    public void testSettersAndGetters() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("ACME");
        cliente.setEmail("acme@test.com");
        cliente.setTipoCliente(TipoCliente.PREMIUM);

        assertEquals(1L, cliente.getId());
        assertEquals("ACME", cliente.getNombre());
        assertEquals("acme@test.com", cliente.getEmail());
        assertEquals(TipoCliente.PREMIUM, cliente.getTipoCliente());
    }
}
