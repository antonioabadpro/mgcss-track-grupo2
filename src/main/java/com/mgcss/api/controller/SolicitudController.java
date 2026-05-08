package com.mgcss.api.controller;

import com.mgcss.api.dto.EstadoHistoricoDTO;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infraestructure.repository.TecnicoRepository;
import com.mgcss.service.SolicitudService;
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
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final TecnicoRepository tecnicoRepository;

    public SolicitudController(SolicitudService solicitudService, TecnicoRepository tecnicoRepository) {
        this.solicitudService = solicitudService;
        this.tecnicoRepository = tecnicoRepository;
    }

    /**
     * Endpoint para crear una solicitud.
     */
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crearSolicitud() {
        Solicitud solicitud = new Solicitud();
        Solicitud creada = solicitudService.crearSolicitud(solicitud);
        return new ResponseEntity<>(mapToResponseDTO(creada), HttpStatus.CREATED);
    }

    /**
     * Endpoint para consultar una solicitud por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> consultarSolicitud(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.consultarSolicitud(id);
        return ResponseEntity.ok(mapToResponseDTO(solicitud));
    }

    /**
     * Endpoint para asignar un técnico a una solicitud.
     */
    @PostMapping("/{id}/asignar-tecnico")
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
    public ResponseEntity<Void> reabrirSolicitud(@PathVariable Long id) {
        solicitudService.reabrirSolicitud(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para listar todas las solicitudes.
     */
    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> listarSolicitudes() {
        List<Solicitud> solicitudes = solicitudService.listarSolicitudes();
        List<SolicitudResponseDTO> dtoList = solicitudes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

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
}
