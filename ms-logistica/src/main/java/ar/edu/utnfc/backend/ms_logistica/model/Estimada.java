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
@Table(name = "estimadas")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class Estimada {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "solicitud_id", nullable = false)
    private UUID solicitudId;
    
    @Column(name = "fuente", nullable = false, length = 50)
    private String fuente;
    
    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;
    
    @Column(name = "distancia_km", precision = 10, scale = 2)
    private BigDecimal distanciaKm;
    
    @Column(name = "duracion_min")
    private Integer duracionMin;
    
    @Column(name = "costo_total", precision = 12, scale = 2)
    private BigDecimal costoTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", length = 3)
    private Moneda moneda;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.moneda == null) {
            this.moneda = Moneda.ARS;
        }
        if (this.fuente == null) {
            this.fuente = "SISTEMA";
        }
    }
}