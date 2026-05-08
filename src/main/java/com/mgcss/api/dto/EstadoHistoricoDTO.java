package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
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
    private EstadoSolicitud estado;
    private LocalDateTime fechaCambio;
}
