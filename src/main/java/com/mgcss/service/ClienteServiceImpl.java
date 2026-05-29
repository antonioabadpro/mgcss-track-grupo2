package com.mgcss.service;

import com.mgcss.api.dto.ClienteDTO;
import com.mgcss.domain.Cliente;
import com.mgcss.infraestructura.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        Cliente cliente = mapToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return mapToDTO(savedCliente);
    }

    @Override
    public Optional<ClienteDTO> modificarCliente(Long id, ClienteDTO clienteDTO) {
        return clienteRepository.findById(id).map(clienteExistente -> {
            if (clienteDTO.getNombre() != null) {
                clienteExistente.setNombre(clienteDTO.getNombre());
            }
            if (clienteDTO.getEmail() != null) {
                clienteExistente.setEmail(clienteDTO.getEmail());
            }
            if (clienteDTO.getTipoCliente() != null) {
                clienteExistente.setTipoCliente(clienteDTO.getTipoCliente());
            }
            Cliente updatedCliente = clienteRepository.save(clienteExistente);
            return mapToDTO(updatedCliente);
        });
    }

    @Override
    public Optional<ClienteDTO> consultarCliente(Long id) {
        return clienteRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Cliente mapToEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        if (dto.getTipoCliente() != null) {
            cliente.setTipoCliente(dto.getTipoCliente());
        }
        return cliente;
    }

    private ClienteDTO mapToDTO(Cliente entity) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setTipoCliente(entity.getTipoCliente());
        return dto;
    }
}
