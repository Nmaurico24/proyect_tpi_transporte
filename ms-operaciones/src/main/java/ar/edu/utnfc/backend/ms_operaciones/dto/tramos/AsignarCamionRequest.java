package ar.edu.utnfc.backend.ms_operaciones.dto.tramos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AsignarCamionRequest {
    @NotBlank
    private String camionRef; // dominio/id de ms-recursos
}
