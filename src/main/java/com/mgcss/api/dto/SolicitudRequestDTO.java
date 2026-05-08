package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
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
    private Long tecnicoId;
    private EstadoSolicitud estado;
}
