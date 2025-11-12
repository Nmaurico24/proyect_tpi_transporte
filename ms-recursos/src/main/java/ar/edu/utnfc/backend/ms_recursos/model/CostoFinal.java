package ar.edu.utnfc.backend.ms_recursos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "costos_final")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class CostoFinal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "ruta_ref", nullable = false, length = 100)
    private String rutaRef;
    
    @Column(name = "litros", precision = 10, scale = 2)
    private BigDecimal litros;
    
    @Column(name = "estadias_dias")
    private Integer estadiasDias;
    
    @Column(name = "total_final", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalFinal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 3)
    private Moneda moneda;
    
    @Column(name = "despose_json", columnDefinition = "TEXT")
    private String desposeJson;
    
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