package ar.edu.utnfc.backend.ms_operaciones.dto.tramos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TramoResponse {
    private UUID id;
    private Integer orden;
    private String tipo;
    private String estado;
}
