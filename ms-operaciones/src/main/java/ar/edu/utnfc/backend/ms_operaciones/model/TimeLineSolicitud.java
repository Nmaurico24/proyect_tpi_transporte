package ar.edu.utnfc.backend.ms_operaciones.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class TimeLineSolicitud {
    private UUID id;
    private String solicitudRef;
    private UUID rutaId;
    private String ultimoEstado;
    private LocalDateTime ultimoEventoEn;
    private String payloadJson; // JSON como String
    private LocalDateTime actualizadoEn;
}
