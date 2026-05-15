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

    /**
     * Método auxiliar para mapear un objeto del dominio Tecnico a un DTO de respuesta TecnicoResponseDTO, que se utiliza para devolver la información del técnico en las respuestas de los endpoints.
     * @param tecnico El objeto del dominio Tecnico que se desea mapear a un DTO de respuesta.
     * @return Un objeto TecnicoResponseDTO con la información del técnico mapeada desde el objeto del dominio.
     */
    private TecnicoResponseDTO mapToResponseDTO(Tecnico tecnico) {
        return TecnicoResponseDTO.builder()
                .id(tecnico.getId())
                .nombre(tecnico.getNombre())
                .edad(tecnico.getEdad())
                .esActivo(tecnico.isTecnicoActivo())
                .build();
    }

    /**
     * Endpoint para crear un nuevo técnico. El técnico se activa automáticamente al crearse. Si se intenta crear un técnico menor de edad, se lanzará una excepción que será manejada para devolver un error 400.
     * @param dto DTO con los datos necesarios para crear el técnico (nombre y edad).
     * @return ResponseEntity con el técnico creado y código 201, o error 400 si la validación falla.
     */
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

    /**
     * Endpoint para listar todos los técnicos registrados en el sistema. Devuelve una lista de técnicos con su información básica.
     * @return ResponseEntity con la lista de técnicos y código 200.
     */ 
    @GetMapping
    @Operation(summary = "Listar técnicos", description = "Obtiene la lista de todos los técnicos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de Técnicos recuperada con éxito.")
    public ResponseEntity<List<TecnicoResponseDTO>> listarTecnicos() {
        List<TecnicoResponseDTO> lista = tecnicoService.listarTecnicos().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    /**
     * @return ResponseEntity con código 200 si el estado se modificó correctamente, o error 404 si el técnico no existe.
     */
    @PatchMapping("/{id}/toggle-estado")
    @Operation(summary = "Cambiar estado del técnico", description = "Alterna el estado del técnico (de activo a inactivo y viceversa).")
    @ApiResponse(responseCode = "200", description = "Estado modificado correctamente.")
    public ResponseEntity<Void> alternarEstadoTecnico(@PathVariable Long id) {
        Tecnico tecnico = tecnicoService.consultarTecnico(id);
        // Le pasamos el estado contrario al que tiene actualmente
        tecnicoService.cambiarEstado(tecnico, !tecnico.isTecnicoActivo());
        return ResponseEntity.ok().build();
    }

    /**
     * Manejador de excepciones para IllegalArgumentException, que se lanza cuando se intenta crear un técnico menor de edad. Devuelve un mensaje de error con código 400.
     * @param ex La excepción lanzada.
     * @return ResponseEntity con el mensaje de error y código 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}