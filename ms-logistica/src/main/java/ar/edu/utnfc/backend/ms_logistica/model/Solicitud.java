package ar.edu.utnfc.backend.ms_logistica.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitudes")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class Solicitud {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitud estado;
    
    @Column(name = "prioridad", nullable = false)
    private Integer prioridad;
    
    @Column(name = "origen_direccion", nullable = false, length = 200)
    private String origenDireccion;
    
    @Column(name = "origen_lat", nullable = false, precision = 9, scale = 6)
    private BigDecimal origenLat;
    
    @Column(name = "origen_lng", nullable = false, precision = 9, scale = 6)
    private BigDecimal origenLng;
    
    @Column(name = "destino_direccion", nullable = false, length = 200)
    private String destinoDireccion;
    
    @Column(name = "destino_lat", nullable = false, precision = 9, scale = 6)
    private BigDecimal destinoLat;
    
    @Column(name = "destino_lng", nullable = false, precision = 9, scale = 6)
    private BigDecimal destinoLng;
    
    @Column(name = "costo_estimado", precision = 12, scale = 2)
    private BigDecimal costoEstimado;
    
    @Column(name = "costo_final", precision = 12, scale = 2)
    private BigDecimal costoFinal;
    
    @Column(name = "distancia_estimada_km", precision = 10, scale = 2)
    private BigDecimal distanciaEstimadaKm;
    
    @Column(name = "duracion_estimada_min")
    private Integer duracionEstimadaMin;
    
    @Column(name = "duracion_real_min")
    private Integer duracionRealMin;
    
    @Column(name = "ruta_ref", length = 100)
    private String rutaRef;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoSolicitud.BORRADOR;
        }
        if (this.prioridad == null) {
            this.prioridad = 3; // Prioridad media por defecto
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}