package com.mgcss.api.controller;

import com.mgcss.api.dto.EstadoHistoricoDTO;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infraestructure.repository.TecnicoRepository;
import com.mgcss.service.SolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las solicitudes.
 */
@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "SolicitudController", description = "Endpoints para gestionar solicitudes")
@CrossOrigin(origins = "*") // Permitir CORS para que la pagina web pueda ser accedida desde cualquier origen
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final TecnicoRepository tecnicoRepository;

    /**
     * Mapper manual para evitar dependencias innecesarias y cumplir el patrón DTO.
     */
    private SolicitudResponseDTO mapToResponseDTO(Solicitud solicitud) {
        List<EstadoHistoricoDTO> historicoDTOs = null;
        if (solicitud.getHistoricoEstados() != null) {
            historicoDTOs = solicitud.getHistoricoEstados().stream()
                    .map(h -> new EstadoHistoricoDTO(h.getEstado(), h.getFechaCambio()))
                    .collect(Collectors.toList());
        }

        return SolicitudResponseDTO.builder()
                .id(solicitud.getId())
                .estado(solicitud.getEstado())
                .fechaCreacion(solicitud.getFechaCreacion())
                .tecnicoId(solicitud.getTecnico() != null ? solicitud.getTecnico().getId() : null)
                .historicoEstados(historicoDTOs)
                .build();
    }

    public SolicitudController(SolicitudService solicitudService, TecnicoRepository tecnicoRepository) {
        this.solicitudService = solicitudService;
        this.tecnicoRepository = tecnicoRepository;
    }

    /**
     * Endpoint para crear una solicitud.
     */
    @PostMapping
    @Operation(summary = "Crear solicitud", description = "Crea una nueva solicitud vacía con estado ABIERTA.")
    @ApiResponse(responseCode = "201", description = "Solicitud creada con éxito.")
    @ApiResponse(responseCode = "400", description = "Error al crear la solicitud.")
    public ResponseEntity<SolicitudResponseDTO> crearSolicitud() {
        Solicitud solicitud = new Solicitud();
        Solicitud creada = solicitudService.crearSolicitud(solicitud);
        return new ResponseEntity<>(mapToResponseDTO(creada), HttpStatus.CREATED);
    }

    /**
     * Endpoint para consultar una solicitud por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar solicitud", description = "Obtiene los detalles de una solicitud mediante el ID de dicha solicitud.")
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada con éxito.")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada.")
    @ApiResponse(responseCode = "500", description = "No existe la solicitud con el ID introducido.")
    public ResponseEntity<SolicitudResponseDTO> consultarSolicitud(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.consultarSolicitud(id);
        return ResponseEntity.ok(mapToResponseDTO(solicitud));
    }

    /**
     * Endpoint para asignar un técnico a una solicitud.
     */
    @PostMapping("/{id}/asignar-tecnico")
    @Operation(summary = "Asignar técnico", description = "Asigna un técnico a una solicitud existente.")
    @ApiResponse(responseCode = "200", description = "Técnico asignado con éxito.")
    @ApiResponse(responseCode = "400", description = "Solicitud no encontrada o técnico inválido.")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada.")
    @ApiResponse(responseCode = "500", description = "No existe la solicitud con el ID introducido.")
    public ResponseEntity<Void> asignarTecnico(@PathVariable Long id, @RequestBody SolicitudRequestDTO requestDTO) {
        if (requestDTO.getTecnicoId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Tecnico tecnico = tecnicoRepository.findById(requestDTO.getTecnicoId())
                .orElseThrow(() -> new IllegalArgumentException("El técnico no existe"));
        
        solicitudService.asignarTecnico(id, tecnico);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para cambiar el estado de una solicitud.
     */
    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado", description = "Cambia el estado de una solicitud existente.")
    @ApiResponse(responseCode = "200", description = "Estado cambiado con éxito.")
    @ApiResponse(responseCode = "400", description = "Solicitud no encontrada o estado inválido.")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada.")
    @ApiResponse(responseCode = "500", description = "No existe la Solicitud o el Técnico con el ID introducido.")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody SolicitudRequestDTO requestDTO) {
        if (requestDTO.getEstado() == null) {
            return ResponseEntity.badRequest().build();
        }
        Solicitud solicitud = solicitudService.consultarSolicitud(id);
        solicitudService.cambiarEstado(solicitud, requestDTO.getEstado());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para reabrir una solicitud.
     */
    @PatchMapping("/{id}/reabrir")
    @Operation(summary = "Reabrir solicitud", description = "Reabre una solicitud que se encuentra en estado CERRADA.")
    @ApiResponse(responseCode = "200", description = "Solicitud reabierta con éxito.)")
    @ApiResponse(responseCode = "400", description = "Solicitud no encontrada o estado no permitido para reabrir.")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada.")
    @ApiResponse(responseCode = "500", description = "No existe la solicitud con el ID introducido.")
    public ResponseEntity<Void> reabrirSolicitud(@PathVariable Long id) {
        solicitudService.reabrirSolicitud(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para listar todas las solicitudes.
     */
    @GetMapping
    @Operation(summary = "Listar solicitudes", description = "Obtiene una lista de todas las solicitudes.")
    @ApiResponse(responseCode = "200", description = "Solicitudes listadas con éxito.")
    @ApiResponse(responseCode = "500", description = "No hay ninguna solicitud en el Servidor.")
    public ResponseEntity<List<SolicitudResponseDTO>> listarSolicitudes() {
        List<Solicitud> solicitudes = solicitudService.listarSolicitudes();
        List<SolicitudResponseDTO> dtoList = solicitudes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // --- Control de Excepciones para que los TESTS pasen correctamente ---
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(java.util.NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
