package ar.edu.utnfc.backend.ms_recursos.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class DisponibilidadCamionDTO {
    private UUID id;
    private UUID camionId;
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private EstadoDisponibilidad estado; // Asumo que es un enum
    private String motivo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}