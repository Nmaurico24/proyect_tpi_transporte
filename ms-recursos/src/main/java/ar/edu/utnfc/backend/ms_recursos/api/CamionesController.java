package ar.edu.utnfc.backend.ms_recursos.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;


import ar.edu.utnfc.backend.ms_recursos.dto.*;
import ar.edu.utnfc.backend.ms_recursos.services.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/camiones")
@Validated
public class CamionesController {

    @Autowired
    private CamionService camionService;

    // POST /camiones - Crear camión
    @PostMapping
    public ResponseEntity<CamionDTO> crearCamion(@Valid @RequestBody CamionCreateDTO camionCreateDTO) {
        CamionDTO camionCreado = camionService.crearCamion(camionCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(camionCreado);
    }

    // PUT /camiones/{id} - Actualizar camión completo
    @PutMapping("/{id}")
    public ResponseEntity<CamionDTO> actualizarCamion(
            @PathVariable UUID id, 
            @Valid @RequestBody CamionUpdateDTO camionUpdateDTO) {
        CamionDTO camionActualizado = camionService.actualizarCamion(id, camionUpdateDTO);
        return ResponseEntity.ok(camionActualizado);
    }

    // PATCH /camiones/{id}/disponibilidad - Bloquea/libera disponibilidad
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<DisponibilidadResponseDTO> actualizarDisponibilidad(
            @PathVariable UUID id,
            @Valid @RequestBody DisponibilidadRequestDTO disponibilidadRequest) {
        DisponibilidadResponseDTO respuesta = camionService.actualizarDisponibilidad(id, disponibilidadRequest);
        return ResponseEntity.ok(respuesta);
    }

    // GET /camiones/disponibles - Consulta disponibilidad por ventana y capacidad
    @GetMapping("/disponibles")
    public ResponseEntity<Page<CamionListDTO>> consultarDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(required = false) BigDecimal capPeso,
            @RequestParam(required = false) BigDecimal capVol,
            Pageable pageable) {
        
        Page<CamionListDTO> camionesDisponibles = camionService.consultarDisponibles(
            desde, hasta, capPeso, capVol, pageable);
        return ResponseEntity.ok(camionesDisponibles);
    }

    // GET /camiones - Listado paginado (como mencionaste)
    @GetMapping
    public ResponseEntity<Page<CamionListDTO>> listarCamiones(Pageable pageable) {
        Page<CamionListDTO> camiones = camionService.obtenerPagina(pageable);
        return ResponseEntity.ok(camiones);
    }

    // GET /camiones/{id} - Obtener camión por ID
    @GetMapping("/{id}")
    public ResponseEntity<CamionDTO> obtenerCamionPorId(@PathVariable UUID id) {
        CamionDTO camion = camionService.obtenerCamionPorId(id);
        return ResponseEntity.ok(camion);
    }

    // DELETE /camiones/{id} - Eliminar camión (lógico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCamion(@PathVariable UUID id) {
        camionService.eliminarCamion(id);
        return ResponseEntity.noContent().build();
    }
}


// GET /api/camiones?page=0&size=10&sort=nombre,asc
// GET /api/camiones/disponibles?desde=2024-01-01T00:00:00&hasta=2024-12-31T23:59:59&page=0&size=5