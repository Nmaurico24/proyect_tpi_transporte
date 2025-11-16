package ar.edu.utnfc.backend.ms_recursos.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostoFinalResponse {
    private double kmReales;
    private double precioKm;
    private double litros;
    private double horasEstadia;
    private double recargoEstadia;
    private double totalFinal;
}
