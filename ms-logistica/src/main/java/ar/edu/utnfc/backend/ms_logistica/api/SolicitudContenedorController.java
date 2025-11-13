package ar.edu.utnfc.backend.ms_logistica.api;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.services.SolicitudContenedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitudes/{solicitudId}/contenedores")
public class SolicitudContenedorController {

    @Autowired
    private SolicitudContenedorService solicitudContenedorService;

    // ========== ENDPOINTS DE GESTIÓN DE CONTENEDORES ==========

    @PostMapping
    public ResponseEntity<SolicitudContenedorDTO> agregarContenedor(
            @PathVariable UUID solicitudId,
            @Valid @RequestBody AgregarContenedorSolicitudDTO agregarDTO) {
        SolicitudContenedorDTO relacion = solicitudContenedorService.agregarContenedorASolicitud(solicitudId, agregarDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(relacion);
    }

    @DeleteMapping("/{contenedorId}")
    public ResponseEntity<Void> removerContenedor(
            @PathVariable UUID solicitudId,
            @PathVariable UUID contenedorId) {
        solicitudContenedorService.removerContenedorDeSolicitud(solicitudId, contenedorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> limpiarContenedores(@PathVariable UUID solicitudId) {
        solicitudContenedorService.limpiarContenedoresDeSolicitud(solicitudId);
        return ResponseEntity.noContent().build();
    }

    // ========== ENDPOINTS DE CONSULTA ==========

    @GetMapping
    public ResponseEntity<List<ContenedorListDTO>> obtenerContenedoresDeSolicitud(@PathVariable UUID solicitudId) {
        List<ContenedorListDTO> contenedores = solicitudContenedorService.obtenerContenedoresDeSolicitud(solicitudId);
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/{contenedorId}/verificar")
    public ResponseEntity<Boolean> verificarContenedorEnSolicitud(
            @PathVariable UUID solicitudId,
            @PathVariable UUID contenedorId) {
        boolean existe = solicitudContenedorService.verificarContenedorEnSolicitud(solicitudId, contenedorId);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/contar")
    public ResponseEntity<Long> contarContenedoresEnSolicitud(@PathVariable UUID solicitudId) {
        long cantidad = solicitudContenedorService.contarContenedoresEnSolicitud(solicitudId);
        return ResponseEntity.ok(cantidad);
    }

    // ========== ENDPOINTS DE GESTIÓN DE ORDEN ==========

    @PutMapping("/orden")
    public ResponseEntity<Void> actualizarOrdenContenedores(
            @PathVariable UUID solicitudId,
            @RequestBody List<UUID> contenedorIds) {
        solicitudContenedorService.actualizarOrdenContenedores(solicitudId, contenedorIds);
        return ResponseEntity.ok().build();
    }

    // ========== ENDPOINTS ADICIONALES ==========

    @GetMapping("/contenedor/{contenedorId}/solicitudes")
    public ResponseEntity<List<SolicitudListDTO>> obtenerSolicitudesDeContenedor(@PathVariable UUID contenedorId) {
        List<SolicitudListDTO> solicitudes = solicitudContenedorService.obtenerSolicitudesDeContenedor(contenedorId);
        return ResponseEntity.ok(solicitudes);
    }
}