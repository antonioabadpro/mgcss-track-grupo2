package com.mgcss.infraestructura.repository;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.TipoCliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("testcontainers")
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testGuardarYBuscarCliente() {
        Cliente c = new Cliente();
        c.setNombre("Cliente Test");
        c.setEmail("test@test.com");
        c.setTipoCliente(TipoCliente.PREMIUM);

        Cliente saved = clienteRepository.save(c);

        assertNotNull(saved.getId());

        Cliente found = clienteRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Cliente Test", found.getNombre());
        assertEquals("test@test.com", found.getEmail());
        assertEquals(TipoCliente.PREMIUM, found.getTipoCliente());
    }
}
