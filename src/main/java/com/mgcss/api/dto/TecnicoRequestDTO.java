package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de creación de un técnico.
 * No expone la entidad JPA directamente y sirve como contrato estable para las peticiones relacionadas con técnicos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoRequestDTO {
    @Schema(description = "Nombre completo del técnico", example = "Pepe García")
    private String nombre;
    
    @Schema(description = "Edad del técnico (debe ser mayor de edad)", example = "22")
    private int edad;
}