package ar.edu.utnfc.backend.ms_operaciones.models;

import ar.edu.utnfc.backend.ms_operaciones.models.enums.EstadoTramo;
import ar.edu.utnfc.backend.ms_operaciones.models.enums.TipoTramo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Ruta ruta;

    private Integer orden;

    @Enumerated(EnumType.STRING)
    private TipoTramo tipo;

    @Enumerated(EnumType.STRING)
    private EstadoTramo estado;

    // Orígenes/destinos resumidos
    private String origenRef;
    private String origenDir;
    private BigDecimal origenLat;
    private BigDecimal origenLng;
    private String destinoRef;
    private String destinoDir;
    private BigDecimal destinoLat;
    private BigDecimal destinoLng;

    private BigDecimal distanciaKm;
    private Integer duracionEstimadaMin;
    private BigDecimal costoEstimado;
    private BigDecimal costoFinal;

    private Instant createdAt;
    private Instant updatedAt;
}
