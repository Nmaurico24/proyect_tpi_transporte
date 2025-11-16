package ar.edu.utnfc.backend.ms_logistica.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "estimaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estimada {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;

    private String fuente; // ej: "ms-operaciones+ms-recursos"
    @Lob
    private String payloadJson; // respuesta cruda

    private Double distanciaKm;
    private Integer duracionMin;
    private Double costoTotal;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
