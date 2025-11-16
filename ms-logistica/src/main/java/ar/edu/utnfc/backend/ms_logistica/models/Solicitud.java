package ar.edu.utnfc.backend.ms_logistica.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    private Integer prioridad;

    private String origenDireccion;
    private String destinoDireccion;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    private Double precioEstimado;
    private Double precioFinal;

    private Double distanciaEstimadaKm;
    private Double distanciaRealKm;

    private Integer duracionEstimadaMin;
    private Integer duracionRealMin;

    private String rutaRef;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}
