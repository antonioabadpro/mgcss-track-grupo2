package com.mgcss.api.controller;

import com.mgcss.api.dto.ClienteDTO;
import com.mgcss.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "ClienteController", description = "Endpoints para gestionar clientes")
@CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500" }) // Permitir CORS para que la pagina web
                                                                             // pueda ser accedida desde local desde el
                                                                             // puerto 5500 (Live Server de VSCode)
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente con los datos proporcionados. El ID del cliente es autogenerado por la base de datos")
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevoCliente = clienteService.crearCliente(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un cliente", description = "Modifica un cliente existente con los datos proporcionados")
    public ResponseEntity<ClienteDTO> modificarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        return clienteService.modificarCliente(id, clienteDTO)
                .map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un cliente", description = "Consulta un cliente existente por su ID")
    public ResponseEntity<ClienteDTO> consultarCliente(@PathVariable Long id) {
        return clienteService.consultarCliente(id)
                .map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Lista todos los clientes existentes")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
}
