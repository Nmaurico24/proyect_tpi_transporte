package ar.edu.utnfc.backend.ms_recursos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tarifa_camion")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class TarifaCamion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "tarifa_id", nullable = false)
    private UUID tarifaId;
    
    @Column(name = "camion_id", nullable = false)
    private UUID camionId;
}
