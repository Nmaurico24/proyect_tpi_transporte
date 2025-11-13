package ar.edu.utnfc.backend.ms_logistica.api;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.services.EstimadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/estimadas")
public class EstimadaController {

    @Autowired
    private EstimadaService estimadaService;

    // ========== ENDPOINTS CRUD BÁSICOS ==========

    @PostMapping
    public ResponseEntity<EstimadaDTO> crearEstimacion(@Valid @RequestBody EstimadaCreateDTO estimadaCreateDTO) {
        EstimadaDTO estimadaCreada = estimadaService.crearEstimacion(estimadaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(estimadaCreada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstimadaDTO> obtenerEstimadaPorId(@PathVariable UUID id) {
        EstimadaDTO estimada = estimadaService.obtenerEstimadaPorId(id);
        return ResponseEntity.ok(estimada);
    }

    // ========== ENDPOINTS DE CONSULTA ESPECÍFICOS ==========

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<EstimadaDTO>> obtenerEstimacionesPorSolicitud(@PathVariable UUID solicitudId) {
        List<EstimadaDTO> estimaciones = estimadaService.obtenerEstimacionesPorSolicitud(solicitudId);
        return ResponseEntity.ok(estimaciones);
    }

    @GetMapping("/solicitud/{solicitudId}/ultima")
    public ResponseEntity<EstimadaDTO> obtenerUltimaEstimacionPorSolicitud(@PathVariable UUID solicitudId) {
        Optional<EstimadaDTO> estimada = estimadaService.obtenerUltimaEstimacionPorSolicitud(solicitudId);
        return estimada.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<EstimadaDTO>> obtenerEstimacionesRecientes() {
        List<EstimadaDTO> estimaciones = estimadaService.obtenerEstimacionesRecientes();
        return ResponseEntity.ok(estimaciones);
    }

    @GetMapping("/fuente/{fuente}")
    public ResponseEntity<List<EstimadaDTO>> obtenerEstimacionesPorFuente(@PathVariable String fuente) {
        List<EstimadaDTO> estimaciones = estimadaService.obtenerEstimacionesPorFuente(fuente);
        return ResponseEntity.ok(estimaciones);
    }

    @GetMapping("/buscar/costo")
    public ResponseEntity<List<EstimadaDTO>> obtenerEstimacionesPorRangoCosto(
            @RequestParam BigDecimal costoMin,
            @RequestParam BigDecimal costoMax) {
        List<EstimadaDTO> estimaciones = estimadaService.obtenerEstimacionesPorRangoCosto(costoMin, costoMax);
        return ResponseEntity.ok(estimaciones);
    }

    @GetMapping("/buscar/distancia")
    public ResponseEntity<List<EstimadaDTO>> obtenerEstimacionesPorRangoDistancia(
            @RequestParam BigDecimal distanciaMin,
            @RequestParam BigDecimal distanciaMax) {
        List<EstimadaDTO> estimaciones = estimadaService.obtenerEstimacionesPorRangoDistancia(distanciaMin, distanciaMax);
        return ResponseEntity.ok(estimaciones);
    }

    // ========== ENDPOINTS DE ANÁLISIS Y ESTADÍSTICAS ==========

    @GetMapping("/estadisticas/generales")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = estimadaService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/estadisticas/fuente")
    public ResponseEntity<Map<String, Map<String, Object>>> obtenerEstadisticasPorFuente() {
        Map<String, Map<String, Object>> estadisticas = estimadaService.obtenerEstadisticasPorFuente();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/analisis/precision")
    public ResponseEntity<Map<String, Object>> analizarPrecisionEstimaciones() {
        Map<String, Object> analisis = estimadaService.analizarPrecisionEstimaciones();
        return ResponseEntity.ok(analisis);
    }

    @GetMapping("/top/precisas")
    public ResponseEntity<List<EstimadaDTO>> obtenerEstimacionesMasPrecisas() {
        List<EstimadaDTO> estimaciones = estimadaService.obtenerEstimacionesMasPrecisas();
        return ResponseEntity.ok(estimaciones);
    }

    @GetMapping("/top/impresisas")
    public ResponseEntity<List<EstimadaDTO>> obtenerEstimacionesMenosPrecisas() {
        List<EstimadaDTO> estimaciones = estimadaService.obtenerEstimacionesMenosPrecisas();
        return ResponseEntity.ok(estimaciones);
    }

    @GetMapping("/costos-promedio/fuente")
    public ResponseEntity<Map<String, BigDecimal>> obtenerCostoPromedioPorFuente() {
        Map<String, BigDecimal> costosPromedio = estimadaService.obtenerCostoPromedioPorFuente();
        return ResponseEntity.ok(costosPromedio);
    }

    // ========== ENDPOINTS DE INTEGRACIÓN ==========

    @PostMapping("/desde-recursos/{solicitudId}")
    public ResponseEntity<EstimadaDTO> crearEstimacionDesdeRecursos(
            @PathVariable UUID solicitudId,
            @RequestBody CostoEstimacionResponseDTO costoEstimacion) {
        EstimadaDTO estimada = estimadaService.crearEstimacionDesdeRecursos(solicitudId, costoEstimacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(estimada);
    }
}