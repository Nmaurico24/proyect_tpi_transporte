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
@Builder
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Tramo tramo;

    private String camionRef; // dominio/id recibido de ms-recursos
    private String asignadoPor;
    private Instant asignadoEn;
}
