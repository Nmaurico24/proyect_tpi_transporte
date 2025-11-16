package ar.edu.utnfc.backend.ms_recursos.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String patente;

    private Double capPesoKg;
    private Double capVolM3;

    @Column(nullable = false)
    private Boolean disponible;
}
