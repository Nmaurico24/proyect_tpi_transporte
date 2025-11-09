package ar.edu.utnfc.backend.ms_recursos.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class TarifaCamion {
    private UUID id;
    private UUID tarifaId;
    private UUID camionId;
}