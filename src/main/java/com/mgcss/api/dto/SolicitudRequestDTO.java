package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las peticiones relacionadas con Solicitud.
 * No expone la entidad directamente y sirve como contrato estable.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudRequestDTO {
    @Schema(description = "Descripción del problema a reportar", example = "El equipo del profesor del aula IM 1.8 no enciende")
    private String descripcion;
    @Schema(description = "ID del técnico asignado a la solicitud", example = "10")
    private Long tecnicoId;
    @Schema(description = "Estado de la solicitud que enviamos en la petición", example = "EN_PROCESO")
    private EstadoSolicitud estado;
}
