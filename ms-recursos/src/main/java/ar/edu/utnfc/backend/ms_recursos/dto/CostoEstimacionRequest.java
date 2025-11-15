package ar.edu.utnfc.backend.ms_recursos.dto;

import lombok.Data;

@Data
public class CostoEstimacionRequest {
    private double kmEstimados;
    private double pesoKg;
    private double volumenM3;
}
