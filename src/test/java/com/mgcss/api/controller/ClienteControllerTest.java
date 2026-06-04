package com.mgcss.api.controller;

import com.mgcss.api.dto.ClienteDTO;
import com.mgcss.domain.TipoCliente;
import com.mgcss.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Tag("api")
@WebMvcTest(value = ClienteController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan");
        clienteDTO.setEmail("juan@example.com");
        clienteDTO.setTipoCliente(TipoCliente.STANDARD);
    }

    @Test
    void testCrearCliente() throws Exception {
        when(clienteService.crearCliente(any(ClienteDTO.class))).thenReturn(clienteDTO);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Juan\", \"email\": \"juan@example.com\", \"tipoCliente\": \"STANDARD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testModificarCliente() throws Exception {
        when(clienteService.modificarCliente(eq(1L), any(ClienteDTO.class))).thenReturn(Optional.of(clienteDTO));

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Juan Modificado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testModificarClienteNotFound() throws Exception {
        when(clienteService.modificarCliente(eq(1L), any(ClienteDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Juan Modificado\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testConsultarCliente() throws Exception {
        when(clienteService.consultarCliente(1L)).thenReturn(Optional.of(clienteDTO));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testConsultarClienteNotFound() throws Exception {
        when(clienteService.consultarCliente(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarClientes() throws Exception {
        when(clienteService.listarClientes()).thenReturn(Arrays.asList(clienteDTO));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
