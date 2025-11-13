package ar.edu.utnfc.backend.ms_logistica.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitud_eventos")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class SolicitudEvento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "solicitud_id", nullable = false)
    private UUID solicitudId;
    
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
    
    @Column(name = "detalle", length = 500)
    private String detalle;
    
    @Column(name = "actor", nullable = false, length = 50)
    private String actor;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.actor == null || this.actor.isBlank()) {
            this.actor = "SISTEMA";
        }
    }
}