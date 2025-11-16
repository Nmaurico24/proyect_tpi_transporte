package ar.edu.utnfc.backend.ms_recursos.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostoEstimacionResponse {
    private double km;
    private double precioKm;
    private double detallePeso;
    private double detalleVolumen;
    private double totalEstimado;
}
