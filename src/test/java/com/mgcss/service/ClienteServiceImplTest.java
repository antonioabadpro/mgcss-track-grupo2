package com.mgcss.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import com.mgcss.api.dto.ClienteDTO;
import com.mgcss.domain.Cliente;
import com.mgcss.domain.TipoCliente;
import com.mgcss.infraestructura.repository.ClienteRepository;

@Tag("service")
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void testCrearCliente() {
        ClienteDTO inputDto = new ClienteDTO();
        inputDto.setNombre("Juan");
        inputDto.setEmail("juan@test.com");
        inputDto.setTipoCliente(TipoCliente.STANDARD);

        Cliente savedEntity = new Cliente();
        savedEntity.setId(1L);
        savedEntity.setNombre("Juan");
        savedEntity.setEmail("juan@test.com");
        savedEntity.setTipoCliente(TipoCliente.STANDARD);

        when(clienteRepository.save(any(Cliente.class))).thenReturn(savedEntity);

        ClienteDTO result = clienteService.crearCliente(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getNombre());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testModificarCliente() {
        Cliente existingEntity = new Cliente();
        existingEntity.setId(1L);
        existingEntity.setNombre("Juan");

        ClienteDTO inputDto = new ClienteDTO();
        inputDto.setNombre("Juan Updated");
        inputDto.setEmail("updated@test.com");
        inputDto.setTipoCliente(TipoCliente.PREMIUM);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Optional<ClienteDTO> result = clienteService.modificarCliente(1L, inputDto);

        assertTrue(result.isPresent());
        assertEquals("Juan Updated", result.get().getNombre());
        assertEquals("updated@test.com", result.get().getEmail());
        assertEquals(TipoCliente.PREMIUM, result.get().getTipoCliente());
    }

    @Test
    void testModificarClienteNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ClienteDTO> result = clienteService.modificarCliente(1L, new ClienteDTO());

        assertFalse(result.isPresent());
    }

    @Test
    void testConsultarCliente() {
        Cliente existingEntity = new Cliente();
        existingEntity.setId(1L);
        existingEntity.setNombre("Juan");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

        Optional<ClienteDTO> result = clienteService.consultarCliente(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testConsultarClienteNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ClienteDTO> result = clienteService.consultarCliente(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testListarClientes() {
        Cliente existingEntity = new Cliente();
        existingEntity.setId(1L);
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(existingEntity));

        List<ClienteDTO> result = clienteService.listarClientes();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}
