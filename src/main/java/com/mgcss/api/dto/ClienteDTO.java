package com.mgcss.api.dto;

import com.mgcss.domain.TipoCliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa un cliente en el sistema")
public class ClienteDTO {
    
    @Schema(description = "ID único del cliente generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez")
    private String nombre;
    
    @Schema(description = "Correo electrónico de contacto del cliente", example = "juan.perez@empresa.com")
    private String email;
    
    @Schema(description = "Tipo de suscripción o nivel de soporte del cliente", example = "STANDARD")
    private TipoCliente tipoCliente;
}
