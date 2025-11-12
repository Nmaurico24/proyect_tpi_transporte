package ar.edu.utnfc.backend.ms_recursos.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ar.edu.utnfc.backend.ms_recursos.dto.*;
import ar.edu.utnfc.backend.ms_recursos.services.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/costos")
@Validated
public class CostoController {

    @Autowired
    private CostoService costoService;

    // POST /costos/estimacion - Calcula costo estimado (llama a ms-logistica)
    @PostMapping("/estimacion")
    public ResponseEntity<CostosEstimacionResponseDTO> calcularCostoEstimado(
            @Valid @RequestBody CostosEstimacionRequestDTO request) {
        CostosEstimacionResponseDTO respuesta = costoService.calcularCostoEstimado(request);
        return ResponseEntity.ok(respuesta);
    }

    // POST /costos/final - Calcula costo final (llama a ms-operaciones)
    @PostMapping("/final")
    public ResponseEntity<CostosFinalResponseDTO> calcularCostoFinal(
            @Valid @RequestBody CostosFinalRequestDTO request) {
        CostosFinalResponseDTO respuesta = costoService.calcularCostoFinal(request);
        return ResponseEntity.ok(respuesta);
    }

    // GET /costos/estimacion/{solicitudId} - Obtener estimación por solicitud
    @GetMapping("/estimacion/{solicitudId}")
    public ResponseEntity<CostosEstimacionResponseDTO> obtenerEstimacionPorSolicitud(
            @PathVariable UUID solicitudId) {
        CostosEstimacionResponseDTO estimacion = costoService.obtenerEstimacionPorSolicitud(solicitudId);
        return ResponseEntity.ok(estimacion);
    }

    // GET /costos/final/{rutaRef} - Obtener costo final por ruta
    @GetMapping("/final/{rutaRef}")
    public ResponseEntity<CostosFinalResponseDTO> obtenerCostoFinalPorRuta(
            @PathVariable String rutaRef) {
        CostosFinalResponseDTO costoFinal = costoService.obtenerCostoFinalPorRuta(rutaRef);
        return ResponseEntity.ok(costoFinal);
    }


    // NUEVOS ENDPOINTS
    @GetMapping("/estimaciones/recientes")
    public ResponseEntity<List<CostosEstimacionResponseDTO>> obtenerEstimacionesRecientes() {
        List<CostosEstimacionResponseDTO> estimaciones = costoService.obtenerEstimacionesRecientes();
        return ResponseEntity.ok(estimaciones);
    }

    @GetMapping("/finales/recientes")
    public ResponseEntity<List<CostosFinalResponseDTO>> obtenerCostosFinalesRecientes() {
        List<CostosFinalResponseDTO> costosFinales = costoService.obtenerCostosFinalesRecientes();
        return ResponseEntity.ok(costosFinales);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasCostos() {
        Map<String, Object> estadisticas = costoService.obtenerEstadisticasCostos();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/promedio/estimaciones")
    public ResponseEntity<BigDecimal> obtenerPromedioEstimaciones() {
        BigDecimal promedio = costoService.obtenerPromedioEstimaciones();
        return ResponseEntity.ok(promedio);
    }

    @GetMapping("/promedio/finales")
    public ResponseEntity<BigDecimal> obtenerPromedioCostosFinales() {
        BigDecimal promedio = costoService.obtenerPromedioCostosFinales();
        return ResponseEntity.ok(promedio);
    }
}