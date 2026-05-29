package com.mgcss.service;

import com.mgcss.api.dto.ClienteDTO;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    ClienteDTO crearCliente(ClienteDTO clienteDTO);
    Optional<ClienteDTO> modificarCliente(Long id, ClienteDTO clienteDTO);
    Optional<ClienteDTO> consultarCliente(Long id);
    List<ClienteDTO> listarClientes();
}
