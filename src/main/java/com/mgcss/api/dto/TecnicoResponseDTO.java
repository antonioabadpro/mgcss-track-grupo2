package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para devolver la información de respuesta de un Tecnico.
 * No expone la entidad JPA directamente y sirve como contrato estable para las respuestas relacionadas con técnicos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoResponseDTO {
    @Schema(description = "ID único del técnico", example = "10")
    private Long id;
    
    @Schema(description = "Nombre completo del técnico", example = "Pepe García")
    private String nombre;
    
    @Schema(description = "Edad del técnico", example = "22")
    private int edad;
    
    @Schema(description = "Indica si el técnico está en activo", example = "true")
    private boolean esActivo;
}