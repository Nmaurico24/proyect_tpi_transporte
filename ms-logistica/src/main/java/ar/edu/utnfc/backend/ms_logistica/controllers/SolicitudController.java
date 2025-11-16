package ar.edu.utnfc.backend.ms_logistica.controllers;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.models.EstadoSolicitud;
import ar.edu.utnfc.backend.ms_logistica.services.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public SolicitudDetailResponse crear(@Valid @RequestBody SolicitudCreateRequest request) {
        return solicitudService.crearSolicitud(request);
    }

    @GetMapping("/{id}")
    public SolicitudDetailResponse obtener(@PathVariable UUID id) {
        return solicitudService.obtenerSolicitud(id);
    }

    @GetMapping
    public Page<SolicitudSummaryResponse> listar(
            @RequestParam(required = false) EstadoSolicitud estado,
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return solicitudService.listarSolicitudes(estado, clienteId, PageRequest.of(page, size));
    }

    @PutMapping("/{id}/cancelar")
    public void cancelar(@PathVariable UUID id,
            @RequestParam(defaultValue = "OPERADOR") String actor) {
        solicitudService.cancelarSolicitud(id, actor);
    }

    @PostMapping("/{id}/estimacion")
    public EstimacionResponse registrarEstimacion(@PathVariable UUID id,
            @Valid @RequestBody EstimacionCreateRequest request) {
        return solicitudService.registrarEstimacion(id, request);
    }

    @PostMapping("/{id}/confirmar")
    public void confirmar(@PathVariable UUID id,
            @RequestParam String rutaRef,
            @RequestParam(defaultValue = "OPERADOR") String actor) {
        solicitudService.confirmarSolicitud(id, rutaRef, actor);
    }
}
