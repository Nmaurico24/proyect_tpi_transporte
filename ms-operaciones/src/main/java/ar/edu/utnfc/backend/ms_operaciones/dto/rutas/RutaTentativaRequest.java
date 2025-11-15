package ar.edu.utnfc.backend.ms_operaciones.dto.rutas;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RutaTentativaRequest {
    @NotBlank
    private String origen;
    @NotBlank
    private String destino;
    private Integer depositos; // opcional: cantidad intermedios
    private String solicitudRef;
}
