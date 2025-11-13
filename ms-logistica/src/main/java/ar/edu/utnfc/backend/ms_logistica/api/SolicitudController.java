package ar.edu.utnfc.backend.ms_logistica.api;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;
import ar.edu.utnfc.backend.ms_logistica.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    // ========== ENDPOINTS CRUD BÁSICOS ==========

    @PostMapping
    public ResponseEntity<SolicitudDTO> crearSolicitud(@Valid @RequestBody SolicitudCreateDTO solicitudCreateDTO) {
        SolicitudDTO solicitudCreada = solicitudService.crearSolicitud(solicitudCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDTO> actualizarSolicitud(
            @PathVariable UUID id, 
            @Valid @RequestBody SolicitudUpdateDTO solicitudUpdateDTO) {
        SolicitudDTO solicitudActualizada = solicitudService.actualizarSolicitud(id, solicitudUpdateDTO);
        return ResponseEntity.ok(solicitudActualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitudPorId(@PathVariable UUID id) {
        SolicitudDTO solicitud = solicitudService.obtenerSolicitudPorId(id);
        return ResponseEntity.ok(solicitud);
    }

    @GetMapping
    public ResponseEntity<Page<SolicitudListDTO>> listarSolicitudes(Pageable pageable) {
        Page<SolicitudListDTO> solicitudes = solicitudService.obtenerPagina(pageable);
        return ResponseEntity.ok(solicitudes);
    }

    // ========== ENDPOINTS DE GESTIÓN DE ESTADO ==========

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudDTO> cambiarEstadoSolicitud(
            @PathVariable UUID id,
            @Valid @RequestBody CambioEstadoSolicitudDTO cambioEstadoDTO) {
        SolicitudDTO solicitudActualizada = solicitudService.cambiarEstadoSolicitud(id, cambioEstadoDTO);
        return ResponseEntity.ok(solicitudActualizada);
    }

    // ========== ENDPOINTS DE CONSULTA ESPECÍFICOS ==========

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SolicitudListDTO>> obtenerSolicitudesPorCliente(@PathVariable UUID clienteId) {
        List<SolicitudListDTO> solicitudes = solicitudService.obtenerSolicitudesPorCliente(clienteId);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudListDTO>> obtenerSolicitudesPorEstado(@PathVariable EstadoSolicitud estado) {
        List<SolicitudListDTO> solicitudes = solicitudService.obtenerSolicitudesPorEstado(estado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<SolicitudListDTO>> obtenerSolicitudesActivas() {
        List<SolicitudListDTO> solicitudes = solicitudService.obtenerSolicitudesActivas();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<SolicitudListDTO>> obtenerSolicitudesRecientes() {
        List<SolicitudListDTO> solicitudes = solicitudService.obtenerSolicitudesRecientes();
        return ResponseEntity.ok(solicitudes);
    }

    // ========== ENDPOINTS DE BÚSQUEDA CON FILTROS ==========

    @GetMapping("/buscar")
    public ResponseEntity<List<SolicitudListDTO>> buscarSolicitudesConFiltros(
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(required = false) EstadoSolicitud estado,
            @RequestParam(required = false) Integer prioridadMin,
            @RequestParam(required = false) Integer prioridadMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            @RequestParam(required = false) String rutaRef) {
        
        BusquedaSolicitudesDTO filtros = new BusquedaSolicitudesDTO(
            clienteId, estado, prioridadMin, prioridadMax, fechaDesde, fechaHasta, rutaRef
        );
        
        List<SolicitudListDTO> solicitudes = solicitudService.buscarSolicitudesConFiltros(filtros);
        return ResponseEntity.ok(solicitudes);
    }

    // ========== ENDPOINTS DE ESTADÍSTICAS Y DASHBOARD ==========

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSolicitudesDTO> obtenerEstadisticasDashboard() {
        DashboardSolicitudesDTO estadisticas = solicitudService.obtenerEstadisticasDashboard();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/estadisticas/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPorEstado() {
        Map<String, Object> estadisticas = solicitudService.obtenerEstadisticasPorEstado();
        return ResponseEntity.ok(estadisticas);
    }

    // ========== ENDPOINTS DE INTEGRACIÓN CON MS-RECURSOS ==========

    @PostMapping("/{id}/calcular-costo")
    public ResponseEntity<SolicitudDTO> calcularCostoSolicitud(@PathVariable UUID id) {
        // TODO: Integración con ms-recursos para cálculo de costos
        // Por ahora solo marcamos como placeholder
        SolicitudDTO solicitud = solicitudService.obtenerSolicitudPorId(id);
        return ResponseEntity.ok(solicitud);
    }

    @PostMapping("/{id}/asignar-ruta")
    public ResponseEntity<SolicitudDTO> asignarRutaSolicitud(
            @PathVariable UUID id,
            @RequestParam String rutaRef) {
        // TODO: Integración con ms-operaciones para asignación de ruta
        SolicitudUpdateDTO updateDTO = new SolicitudUpdateDTO(
            null, null, null, null, rutaRef
        );
        SolicitudDTO solicitudActualizada = solicitudService.actualizarSolicitud(id, updateDTO);
        return ResponseEntity.ok(solicitudActualizada);
    }
}