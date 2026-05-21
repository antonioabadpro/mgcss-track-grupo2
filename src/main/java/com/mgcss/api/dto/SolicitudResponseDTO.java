package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para devolver la información de una Solicitud.
 * No expone la entidad JPA, ni colecciones internas mutables de la entidad.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudResponseDTO {
    @Schema(description = "ID de la solicitud", example = "1")
    private Long id;
    @Schema(description = "Estado actual de la solicitud", example = "ABIERTA")
    private EstadoSolicitud estado;
    @Schema(description = "Fecha de creación de la solicitud", example = "11/05/2026 10:00:00")
    private String fechaCreacion;
    @Schema(description = "Descripción del problema", example = "El equipo del profesor del aula IM 1.8 no enciende")
    private String descripcion;
    @Schema(description = "ID del técnico asignado a la solicitud", example = "10")
    private Long tecnicoId;
    @Schema(description = "Lista que contiene el histórico de estados que ha tenido la solicitud")
    private List<EstadoHistoricoDTO> historicoEstados;
}
