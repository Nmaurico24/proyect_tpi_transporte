package ar.edu.utnfc.backend.ms_operaciones.controllers;

import ar.edu.utnfc.backend.ms_operaciones.dto.rutas.*;
import ar.edu.utnfc.backend.ms_operaciones.services.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {
    private final RutaService rutaService;

    @PostMapping("/tentativas")
    public ResponseEntity<RutaResponse> tentativas(@Valid @RequestBody RutaTentativaRequest req) {
        return ResponseEntity.ok(rutaService.generarTentativa(req));
    }

    @PostMapping("/{rutaId}/confirmar")
    public ResponseEntity<RutaResponse> confirmar(@PathVariable UUID rutaId,
            @Valid @RequestBody ConfirmarRutaRequest req) {
        return ResponseEntity.ok(rutaService.confirmar(rutaId, req));
    }

    @GetMapping("/{rutaId}")
    public ResponseEntity<RutaResponse> get(@PathVariable UUID rutaId) {
        var ruta = rutaService.findOrThrow(rutaId);
        return ResponseEntity.ok(
                RutaResponse.builder()
                        .id(ruta.getId())
                        .estado(ruta.getEstado().name())
                        .distanciaKm(ruta.getDistanciaTotalKm())
                        .duracionMin(ruta.getDuracionEstimadaMin())
                        .build());
    }
}
