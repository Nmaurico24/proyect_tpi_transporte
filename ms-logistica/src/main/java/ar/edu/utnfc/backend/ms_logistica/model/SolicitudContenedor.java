package ar.edu.utnfc.backend.ms_logistica.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "solicitud_contenedores")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class SolicitudContenedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "solicitud_id", nullable = false)
    private UUID solicitudId;
    
    @Column(name = "contenedor_id", nullable = false)
    private UUID contenedorId;
    
    // Podemos agregar campos adicionales si es necesario
    @Column(name = "orden_carga")
    private Integer ordenCarga;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
}