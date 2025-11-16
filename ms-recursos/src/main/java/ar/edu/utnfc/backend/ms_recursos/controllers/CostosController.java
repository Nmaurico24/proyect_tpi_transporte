package ar.edu.utnfc.backend.ms_recursos.controllers;

import ar.edu.utnfc.backend.ms_recursos.dto.CostoEstimacionRequest;
import ar.edu.utnfc.backend.ms_recursos.dto.CostoEstimacionResponse;
import ar.edu.utnfc.backend.ms_recursos.dto.CostoFinalRequest;
import ar.edu.utnfc.backend.ms_recursos.dto.CostoFinalResponse;
import ar.edu.utnfc.backend.ms_recursos.models.TarifaRegla;
import ar.edu.utnfc.backend.ms_recursos.services.TarifaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/costos")
public class CostosController {

    private final TarifaService service;

    public CostosController(TarifaService service) {
        this.service = service;
    }

    @PostMapping("/reglas")
    public ResponseEntity<TarifaRegla> crearRegla(@RequestBody TarifaRegla regla) {
        return ResponseEntity.ok(service.guardarRegla(regla));
    }

    @GetMapping("/reglas")
    public List<TarifaRegla> listarReglas() {
        return service.listarReglas();
    }

    // llamado por ms-logistica
    @PostMapping("/estimacion")
    public CostoEstimacionResponse estimacion(@RequestBody CostoEstimacionRequest req) {
        return service.estimar(req);
    }

    // llamado por ms-operaciones al finalizar
    @PostMapping("/final")
    public CostoFinalResponse costoFinal(@RequestBody CostoFinalRequest req) {
        return service.costoFinal(req);
    }
}
