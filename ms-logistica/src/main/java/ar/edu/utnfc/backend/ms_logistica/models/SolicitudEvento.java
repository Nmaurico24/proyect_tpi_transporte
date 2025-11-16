package ar.edu.utnfc.backend.ms_logistica.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "solicitud_eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudEvento {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;

    private String estado;
    private String detalle;
    private String actor; // CLIENTE / OPERADOR / SISTEMA

    @Builder.Default
    private Instant createdAt = Instant.now();
}
