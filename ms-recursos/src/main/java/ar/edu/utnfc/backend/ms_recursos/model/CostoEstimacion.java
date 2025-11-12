package ar.edu.utnfc.backend.ms_recursos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "costos_estimacion")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class CostoEstimacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "solicitud_ref", nullable = false, length = 100)
    private String solicitudRef;
    
    @Column(name = "ruta_payipro", columnDefinition = "TEXT")
    private String rutaPayipro;
    
    @Column(name = "total_estimado", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalEstimado;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 3)
    private Moneda moneda;
    
    @Column(name = "despose", columnDefinition = "TEXT")
    private String despose;
    
    @Column(name = "create_en", nullable = false, updatable = false)
    private LocalDateTime createEn;
    
    @PrePersist
    protected void onCreate() {
        if (this.createEn == null) {
            this.createEn = LocalDateTime.now();
        }
        if (this.moneda == null) {
            this.moneda = Moneda.ARS;
        }
    }
}