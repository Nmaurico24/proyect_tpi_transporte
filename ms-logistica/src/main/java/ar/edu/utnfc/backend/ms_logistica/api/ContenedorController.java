package ar.edu.utnfc.backend.ms_logistica.api;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.services.ContenedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/contenedores")
public class ContenedorController {

    @Autowired
    private ContenedorService contenedorService;

    // ========== ENDPOINTS CRUD BÁSICOS ==========

    @PostMapping
    public ResponseEntity<ContenedorDTO> crearContenedor(@Valid @RequestBody ContenedorCreateDTO contenedorCreateDTO) {
        ContenedorDTO contenedorCreado = contenedorService.crearContenedor(contenedorCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contenedorCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContenedorDTO> actualizarContenedor(
            @PathVariable UUID id, 
            @Valid @RequestBody ContenedorUpdateDTO contenedorUpdateDTO) {
        ContenedorDTO contenedorActualizado = contenedorService.actualizarContenedor(id, contenedorUpdateDTO);
        return ResponseEntity.ok(contenedorActualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContenedorDTO> obtenerContenedorPorId(@PathVariable UUID id) {
        ContenedorDTO contenedor = contenedorService.obtenerContenedorPorId(id);
        return ResponseEntity.ok(contenedor);
    }

    @GetMapping("/etiqueta/{etiqueta}")
    public ResponseEntity<ContenedorDTO> obtenerContenedorPorEtiqueta(@PathVariable String etiqueta) {
        ContenedorDTO contenedor = contenedorService.obtenerContenedorPorEtiqueta(etiqueta);
        return ResponseEntity.ok(contenedor);
    }

    @GetMapping
    public ResponseEntity<Page<ContenedorListDTO>> listarContenedores(Pageable pageable) {
        Page<ContenedorListDTO> contenedores = contenedorService.obtenerPagina(pageable);
        return ResponseEntity.ok(contenedores);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContenedor(@PathVariable UUID id) {
        contenedorService.eliminarContenedor(id);
        return ResponseEntity.noContent().build();
    }

    // ========== ENDPOINTS DE GESTIÓN DE ESTADO ==========

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ContenedorDTO> cambiarEstadoContenedor(
            @PathVariable UUID id,
            @RequestParam String estado) {
        ContenedorDTO contenedorActualizado = contenedorService.cambiarEstadoContenedor(id, estado);
        return ResponseEntity.ok(contenedorActualizado);
    }

    // ========== ENDPOINTS DE CONSULTA ESPECÍFICOS ==========

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ContenedorListDTO>> obtenerContenedoresPorCliente(@PathVariable UUID clienteId) {
        List<ContenedorListDTO> contenedores = contenedorService.obtenerContenedoresPorCliente(clienteId);
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/cliente/{clienteId}/activos")
    public ResponseEntity<List<ContenedorListDTO>> obtenerContenedoresActivosPorCliente(@PathVariable UUID clienteId) {
        List<ContenedorListDTO> contenedores = contenedorService.obtenerContenedoresActivosPorCliente(clienteId);
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ContenedorListDTO>> obtenerContenedoresPorEstado(@PathVariable String estado) {
        List<ContenedorListDTO> contenedores = contenedorService.obtenerContenedoresPorEstado(estado);
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<ContenedorListDTO>> obtenerContenedoresDisponibles() {
        List<ContenedorListDTO> contenedores = contenedorService.obtenerContenedoresDisponibles();
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/en-transito")
    public ResponseEntity<List<ContenedorListDTO>> obtenerContenedoresEnTransito() {
        List<ContenedorListDTO> contenedores = contenedorService.obtenerContenedoresEnTransito();
        return ResponseEntity.ok(contenedores);
    }

    // ========== ENDPOINTS DE BÚSQUEDA ==========

    @GetMapping("/buscar/etiqueta")
    public ResponseEntity<List<ContenedorListDTO>> buscarContenedoresPorEtiqueta(@RequestParam String etiqueta) {
        List<ContenedorListDTO> contenedores = contenedorService.buscarContenedoresPorEtiqueta(etiqueta);
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/disponibles/capacidad")
    public ResponseEntity<List<ContenedorListDTO>> obtenerContenedoresDisponiblesConCapacidad(
            @RequestParam(required = false) BigDecimal pesoMin,
            @RequestParam(required = false) BigDecimal volumenMin) {
        
        List<ContenedorListDTO> contenedores = contenedorService
            .obtenerContenedoresDisponiblesConCapacidad(pesoMin, volumenMin);
        return ResponseEntity.ok(contenedores);
    }

    // ========== ENDPOINTS DE ESTADÍSTICAS ==========

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasContenedores() {
        Map<String, Object> estadisticas = contenedorService.obtenerEstadisticasContenedores();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/cliente/{clienteId}/contar-activos")
    public ResponseEntity<Long> contarContenedoresActivosPorCliente(@PathVariable UUID clienteId) {
        long cantidad = contenedorService.contarContenedoresActivosPorCliente(clienteId);
        return ResponseEntity.ok(cantidad);
    }

    // ========== ENDPOINTS DE INTEGRACIÓN CON MS-RECURSOS ==========

    @GetMapping("/cliente/{clienteId}/en-deposito")
    public ResponseEntity<List<ContenedorListResponseDTO>> obtenerContenedoresEnDeposito(@PathVariable UUID clienteId) {
        // TODO: Integración con ms-recursos para obtener contenedores en depósitos
        // Por ahora retornamos lista vacía como placeholder
        return ResponseEntity.ok(List.of());
    }
}