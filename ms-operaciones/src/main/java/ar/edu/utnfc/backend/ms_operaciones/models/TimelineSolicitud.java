package ar.edu.utnfc.backend.ms_operaciones.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TimelineSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String solicitudRef;
    private String rutaId; // UUID en string para consulta cruzada
    private String ultimoEstado; // texto simple
    private String ultimoEvento; // INICIO/FIN/ASIGNADO/etc.
    @Lob
    private String payloadJson; // opcional
    private Instant actualizadoEn;
}
