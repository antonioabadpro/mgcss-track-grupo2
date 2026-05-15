package com.mgcss.api.controller;

import com.mgcss.api.dto.TecnicoRequestDTO;
import com.mgcss.api.dto.TecnicoResponseDTO;
import com.mgcss.domain.Tecnico;
import com.mgcss.service.TecnicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tecnicos")
@Tag(name = "TecnicoController", description = "Endpoints para gestionar técnicos")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    private TecnicoResponseDTO mapToResponseDTO(Tecnico tecnico) {
        return TecnicoResponseDTO.builder()
                .id(tecnico.getId())
                .nombre(tecnico.getNombre())
                .edad(tecnico.getEdad())
                .esActivo(tecnico.isTecnicoActivo())
                .build();
    }

    @PostMapping
    @Operation(summary = "Crear técnico", description = "Da de alta a un nuevo técnico en el sistema.")
    @ApiResponse(responseCode = "201", description = "Técnico creado con éxito.")
    @ApiResponse(responseCode = "400", description = "Error de validación (ej. menor de edad).")
    public ResponseEntity<TecnicoResponseDTO> crearTecnico(@RequestBody TecnicoRequestDTO dto) {
        Tecnico tecnico = new Tecnico();
        tecnico.setNombre(dto.getNombre());
        tecnico.setEdad(dto.getEdad()); // Aquí el Dominio lanzará excepción si la edad < 18
        
        Tecnico creado = tecnicoService.crearTecnico(tecnico);
        return new ResponseEntity<>(mapToResponseDTO(creado), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar técnicos", description = "Obtiene la lista de todos los técnicos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de Técnicos recuperada con éxito.")
    public ResponseEntity<List<TecnicoResponseDTO>> listarTecnicos() {
        List<TecnicoResponseDTO> lista = tecnicoService.listarTecnicos().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}