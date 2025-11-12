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
@Table(name = "camiones")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class Camion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "dominio", nullable = false, unique = true, length = 10)
    private String dominio;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "cap_peso_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal capPesoKg;
    
    @Column(name = "cap_volume_m3", nullable = false, precision = 10, scale = 3)
    private BigDecimal capVolumeM3;
    
    @Column(name = "costo_operacion_hora", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoOperacionHora;
    
    @Column(name = "ls_active", nullable = false)
    private Boolean lsActive;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Pre-persist y pre-update para manejar timestamps automáticamente
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.lsActive == null) {
            this.lsActive = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}