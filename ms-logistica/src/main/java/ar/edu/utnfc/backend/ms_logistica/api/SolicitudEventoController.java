package ar.edu.utnfc.backend.ms_logistica.api;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.services.SolicitudEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitudes/eventos")
public class SolicitudEventoController {

    @Autowired
    private SolicitudEventoService solicitudEventoService;

    // ========== ENDPOINTS CRUD BÁSICOS ==========

    @PostMapping
    public ResponseEntity<SolicitudEventoDTO> crearEvento(@Valid @RequestBody SolicitudEventoCreateDTO eventoCreateDTO) {
        SolicitudEventoDTO eventoCreado = solicitudEventoService.crearEvento(eventoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoCreado);
    }

    // ========== ENDPOINTS DE CONSULTA ESPECÍFICOS ==========

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<SolicitudEventoDTO>> obtenerEventosPorSolicitud(@PathVariable UUID solicitudId) {
        List<SolicitudEventoDTO> eventos = solicitudEventoService.obtenerEventosPorSolicitud(solicitudId);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/solicitud/{solicitudId}/historial")
    public ResponseEntity<HistorialSolicitudDTO> obtenerHistorialCompleto(@PathVariable UUID solicitudId) {
        HistorialSolicitudDTO historial = solicitudEventoService.obtenerHistorialCompleto(solicitudId);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/solicitud/{solicitudId}/linea-tiempo")
    public ResponseEntity<LineaTiempoDTO> obtenerLineaTiempo(@PathVariable UUID solicitudId) {
        LineaTiempoDTO lineaTiempo = solicitudEventoService.obtenerLineaTiempo(solicitudId);
        return ResponseEntity.ok(lineaTiempo);
    }

    @GetMapping("/solicitud/{solicitudId}/ultimo")
    public ResponseEntity<SolicitudEventoDTO> obtenerUltimoEvento(@PathVariable UUID solicitudId) {
        Optional<SolicitudEventoDTO> ultimoEvento = solicitudEventoService.obtenerUltimoEvento(solicitudId);
        return ultimoEvento.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<SolicitudEventoDTO>> obtenerEventosRecientes() {
        List<SolicitudEventoDTO> eventos = solicitudEventoService.obtenerEventosRecientes();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudEventoDTO>> obtenerEventosPorEstado(@PathVariable String estado) {
        List<SolicitudEventoDTO> eventos = solicitudEventoService.obtenerEventosPorEstado(estado);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/actor/{actor}")
    public ResponseEntity<List<SolicitudEventoDTO>> obtenerEventosPorActor(@PathVariable String actor) {
        List<SolicitudEventoDTO> eventos = solicitudEventoService.obtenerEventosPorActor(actor);
        return ResponseEntity.ok(eventos);
    }

    // ========== ENDPOINTS DE BÚSQUEDA CON FILTROS ==========

    @GetMapping("/buscar")
    public ResponseEntity<List<SolicitudEventoDTO>> buscarEventosConFiltros(
            @RequestParam(required = false) UUID solicitudId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta) {
        
        List<SolicitudEventoDTO> eventos = solicitudEventoService.buscarEventosConFiltros(
            solicitudId, estado, actor, fechaDesde, fechaHasta);
        return ResponseEntity.ok(eventos);
    }

    // ========== ENDPOINTS DE ANÁLISIS Y ESTADÍSTICAS ==========

    @GetMapping("/estadisticas/generales")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = solicitudEventoService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/estadisticas/actor")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasPorActor() {
        Map<String, Long> estadisticas = solicitudEventoService.obtenerEstadisticasPorActor();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/estadisticas/estado")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasPorEstado() {
        Map<String, Long> estadisticas = solicitudEventoService.obtenerEstadisticasPorEstado();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/resumen-actividad")
    public ResponseEntity<ResumenActividadDTO> obtenerResumenActividadReciente() {
        ResumenActividadDTO resumen = solicitudEventoService.obtenerResumenActividadReciente();
        return ResponseEntity.ok(resumen);
    }
}