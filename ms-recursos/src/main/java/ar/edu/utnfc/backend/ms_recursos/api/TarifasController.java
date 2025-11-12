package ar.edu.utnfc.backend.ms_recursos.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import ar.edu.utnfc.backend.ms_recursos.dto.*;
import ar.edu.utnfc.backend.ms_recursos.model.UnidadTarifa;
import ar.edu.utnfc.backend.ms_recursos.services.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarifas")
@Validated
public class TarifasController {

    @Autowired
    private TarifaService tarifaService;

    // PUT /tarifas - Actualiza reglas tarifarias (actualización masiva)
    @PutMapping
    public ResponseEntity<List<TarifaDTO>> actualizarTarifas(
            @Valid @RequestBody List<TarifaRuleDTO> tarifaRules) {
        List<TarifaDTO> tarifasActualizadas = tarifaService.actualizarTarifas(tarifaRules);
        return ResponseEntity.ok(tarifasActualizadas);
    }

    // POST /tarifas/reglas - Crea regla con vigencia/alcance
    @PostMapping("/reglas")
    public ResponseEntity<TarifaDTO> crearReglaTarifaria(
            @Valid @RequestBody TarifaRuleDTO tarifaRuleDTO) {
        TarifaDTO tarifaCreada = tarifaService.crearReglaTarifaria(tarifaRuleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaCreada);
    }

    // GET /tarifas - Lista todas las tarifas vigentes
    @GetMapping
    public ResponseEntity<List<TarifaDTO>> listarTarifasVigentes() {
        List<TarifaDTO> tarifas = tarifaService.obtenerTarifasVigentes();
        return ResponseEntity.ok(tarifas);
    }

    // GET /tarifas/{id} - Obtener tarifa por ID
    @GetMapping("/{id}")
    public ResponseEntity<TarifaDTO> obtenerTarifaPorId(@PathVariable UUID id) {
        TarifaDTO tarifa = tarifaService.obtenerTarifaPorId(id);
        return ResponseEntity.ok(tarifa);
    }

    // DELETE /tarifas/{id} - Desactivar tarifa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarTarifa(@PathVariable UUID id) {
        tarifaService.desactivarTarifa(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/vigentes")
    public ResponseEntity<List<TarifaDTO>> obtenerTarifasVigentes() {
        List<TarifaDTO> tarifas = tarifaService.obtenerTarifasVigentes();
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/unidad/{unidad}")
    public ResponseEntity<List<TarifaDTO>> obtenerTarifasPorUnidad(@PathVariable UnidadTarifa unidad) {
        List<TarifaDTO> tarifas = tarifaService.obtenerTarifasPorUnidad(unidad);
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/camion/{camionId}")
    public ResponseEntity<List<TarifaDTO>> obtenerTarifasPorCamion(@PathVariable UUID camionId) {
        List<TarifaDTO> tarifas = tarifaService.obtenerTarifasPorCamion(camionId);
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/deposito/{depositoId}")
    public ResponseEntity<List<TarifaDTO>> obtenerTarifasPorDeposito(@PathVariable UUID depositoId) {
        List<TarifaDTO> tarifas = tarifaService.obtenerTarifasPorDeposito(depositoId);
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/unidades")
    public ResponseEntity<List<UnidadTarifa>> obtenerUnidadesDisponibles() {
        List<UnidadTarifa> unidades = tarifaService.obtenerUnidadesTarifariasDisponibles();
        return ResponseEntity.ok(unidades);
    }
}