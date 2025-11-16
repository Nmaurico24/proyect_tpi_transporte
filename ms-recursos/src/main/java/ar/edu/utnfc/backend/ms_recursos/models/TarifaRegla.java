package ar.edu.utnfc.backend.ms_recursos.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaRegla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ejemplo de regla simple: precio por km base y factores
    private Double precioKm; // p.ej. 1200 ARS/km
    private Double factorPeso; // multiplicador por tonelada
    private Double factorVolumen; // multiplicador por m3
    private Double recargoEstadia; // ARS por hora de estadía
}
