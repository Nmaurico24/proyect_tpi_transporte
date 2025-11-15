package ar.edu.utnfc.backend.ms_recursos.controllers;

import ar.edu.utnfc.backend.ms_recursos.models.Camion;
import ar.edu.utnfc.backend.ms_recursos.services.CamionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
public class CamionController {

    private final CamionService service;

    public CamionController(CamionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Camion> crear(@RequestBody Camion c) {
        return ResponseEntity.ok(service.crear(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camion> actualizar(@PathVariable Long id, @RequestBody Camion c) {
        return ResponseEntity.ok(service.actualizar(id, c));
    }

    @GetMapping
    public List<Camion> listar() {
        return service.listar();
    }

    @GetMapping("/disponibles")
    public List<Camion> disponibles() {
        return service.disponibles();
    }

    @PatchMapping("/{id}/disponibilidad")
    public Camion disponibilidad(@PathVariable Long id, @RequestParam boolean value) {
        return service.disponibilidad(id, value);
    }
}
