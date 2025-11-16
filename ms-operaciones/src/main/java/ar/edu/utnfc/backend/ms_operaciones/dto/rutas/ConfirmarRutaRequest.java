package ar.edu.utnfc.backend.ms_operaciones.dto.rutas;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmarRutaRequest {
    @NotBlank
    private String solicitudRef;
}
