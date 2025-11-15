package ar.edu.utnfc.backend.ms_recursos.controllers;

import ar.edu.utnfc.backend.ms_recursos.models.Deposito;
import ar.edu.utnfc.backend.ms_recursos.services.DepositoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    private final DepositoService service;

    public DepositoController(DepositoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Deposito> crear(@RequestBody Deposito d) {
        return ResponseEntity.ok(service.crear(d));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposito> actualizar(@PathVariable Long id, @RequestBody Deposito d) {
        return ResponseEntity.ok(service.actualizar(id, d));
    }

    @GetMapping
    public List<Deposito> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Deposito obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}
