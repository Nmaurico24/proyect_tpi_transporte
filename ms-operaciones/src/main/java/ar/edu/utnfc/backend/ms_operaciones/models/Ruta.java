package ar.edu.utnfc.backend.ms_operaciones.models;

import ar.edu.utnfc.backend.ms_operaciones.models.enums.EstadoRuta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String solicitudRef; // referencia a ms-logistica
    @Enumerated(EnumType.STRING)
    private EstadoRuta estado;

    private BigDecimal distanciaTotalKm;
    private Integer duracionEstimadaMin;
    private BigDecimal costoEstimadoTotal;
    private BigDecimal costoFinalTotal;
    private String moneda;

    private Instant createdAt;
    private Instant updatedAt;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Tramo> tramos = new ArrayList<>();
}
