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
@Table(name = "depositos")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class Deposito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;
    
    @Column(name = "lat", nullable = false, precision = 9, scale = 6)
    private BigDecimal lat;
    
    @Column(name = "lng", nullable = false, precision = 9, scale = 6)
    private BigDecimal lng;
    
    @Column(name = "costo_diario", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoDiario;
    
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