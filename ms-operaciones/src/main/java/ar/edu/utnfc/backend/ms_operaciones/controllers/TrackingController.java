package ar.edu.utnfc.backend.ms_operaciones.controllers;

import ar.edu.utnfc.backend.ms_operaciones.dto.tracking.TrackingEventoRequest;
import ar.edu.utnfc.backend.ms_operaciones.dto.tracking.TrackingLineaTiempoResponse;
import ar.edu.utnfc.backend.ms_operaciones.services.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingService trackingService;

    @PostMapping("/tramos/{tramoId}/evento")
    public ResponseEntity<Void> registrar(@PathVariable UUID tramoId,
            @Valid @RequestBody TrackingEventoRequest req,
            Authentication auth) {
        String actor = auth != null ? auth.getName() : "transportista";
        trackingService.registrarEvento(tramoId, req, actor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/solicitudes/{solicitudRef}")
    public ResponseEntity<TrackingLineaTiempoResponse> linea(@PathVariable String solicitudRef) {
        return ResponseEntity.ok(trackingService.obtenerLineaTiempo(solicitudRef));
    }
}
