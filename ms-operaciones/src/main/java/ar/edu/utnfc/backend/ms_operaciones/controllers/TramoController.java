package ar.edu.utnfc.backend.ms_operaciones.controllers;

import ar.edu.utnfc.backend.ms_operaciones.dto.tramos.AsignarCamionRequest;
import ar.edu.utnfc.backend.ms_operaciones.dto.tramos.TramoResponse;
import ar.edu.utnfc.backend.ms_operaciones.services.TramoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tramos")
@RequiredArgsConstructor
public class TramoController {
    private final TramoService tramoService;

    @PostMapping("/{tramoId}/asignar-camion")
    public ResponseEntity<TramoResponse> asignar(@PathVariable UUID tramoId,
            @Valid @RequestBody AsignarCamionRequest req,
            Authentication auth) {
        String actor = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(tramoService.asignarCamion(tramoId, req, actor));
    }

    @GetMapping("/{tramoId}")
    public ResponseEntity<TramoResponse> get(@PathVariable UUID tramoId) {
        var t = tramoService.get(tramoId);
        return ResponseEntity.ok(TramoResponse.builder()
                .id(t.getId()).orden(t.getOrden()).tipo(t.getTipo().name()).estado(t.getEstado().name()).build());
    }
}
