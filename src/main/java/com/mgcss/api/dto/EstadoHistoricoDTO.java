package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para representar el histórico de estados sin exponer la entidad JPA interna.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoHistoricoDTO {
    @Schema(description = "Estado de la solicitud en un momento dado", example = "ABIERTA")
    private EstadoSolicitud estado;
    @Schema(description = "Fecha y hora del cambio de estado", example = "11/05/2026 10:00:00")
    private LocalDateTime fechaCambio;
}
