package com.mgcss.api.dto;

import com.mgcss.domain.EstadoSolicitud;
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
    private Long id;
    private EstadoSolicitud estado;
    private String fechaCreacion;
    private Long tecnicoId;
    private List<EstadoHistoricoDTO> historicoEstados;
}
