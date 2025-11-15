package ar.edu.utnfc.backend.ms_operaciones.models;

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
public class TrackingEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Tramo tramo;

    private String tipo; // INICIO / FIN
    private String actor; // transportista/operador
    private BigDecimal lat;
    private BigDecimal lng;
    private String descripcion;
    private Instant registradoEn;
}
