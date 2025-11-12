package ar.edu.utnfc.backend.ms_recursos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// Asume que Moneda.java y UnidadTarifa.java son Enums o Entities en el mismo paquete o importables.

@Entity // Clase crítica para el mapeo a la base de datos
@Table(name = "tarifas")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class Tarifa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    // --- Campos de la versión nueva (simplificados y renombrados) ---
    
    @Column(name = "descripcion", nullable = false)
    private String description;
    
    // Mapeo de ENUM: asume que UnidadTarifa es un enum
    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_tarifa", nullable = false)
    private UnidadTarifa unidad; 
    
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
    
    // Mapeo de ENUM: asume que Moneda es un enum
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda; 
    
    @Column(name = "vigencia_hasta")
    private LocalDate vigenciaHasta; // Renombrado de vigenciaHasta 
    
    @Version // Anotación para manejo optimista de concurrencia
    @Column(name = "version")
    private Integer version;
    
    // --- Campos de la versión original (eliminados los redundantes) ---
    // Se eliminan precioPorHora, precioPorKm, y tipoServicio al tener description, valor, y unidad.
    
    @Column(name = "ls_active")
    private Boolean lsActive = true;
    
    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // --- Lifecycle Callbacks (PrePersist y PreUpdate) ---
    
    @PrePersist
    protected void onCreate() {
        // Inicializa solo si es la primera vez que se persiste
        if (id == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}