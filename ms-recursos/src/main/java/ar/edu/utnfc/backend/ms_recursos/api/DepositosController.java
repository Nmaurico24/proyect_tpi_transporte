package ar.edu.utnfc.backend.ms_recursos.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

import ar.edu.utnfc.backend.ms_recursos.dto.*;
import ar.edu.utnfc.backend.ms_recursos.services.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/depositos")
@Validated
public class DepositosController {

    @Autowired
    private DepositoService depositoService;

    // POST /depositos - Crear depósito
    @PostMapping
    public ResponseEntity<DepositoDTO> crearDeposito(@Valid @RequestBody DepositoCreateDTO depositoCreateDTO) {
        DepositoDTO depositoCreado = depositoService.crearDeposito(depositoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(depositoCreado);
    }

    // PUT /depositos/{id} - Actualizar depósito completo
    @PutMapping("/{id}")
    public ResponseEntity<DepositoDTO> actualizarDeposito(
            @PathVariable UUID id, 
            @Valid @RequestBody DepositoCreateDTO depositoUpdateDTO) {
        DepositoDTO depositoActualizado = depositoService.actualizarDeposito(id, depositoUpdateDTO);
        return ResponseEntity.ok(depositoActualizado);
    }

    // GET /depositos - Lista depósitos (paginado)
    @GetMapping
    public ResponseEntity<Page<DepositoListDTO>> listarDepositos(Pageable pageable) {
        Page<DepositoListDTO> depositos = depositoService.obtenerPagina(pageable);
        return ResponseEntity.ok(depositos);
    }

    // GET /depositos/{id} - Obtener depósito por ID
    @GetMapping("/{id}")
    public ResponseEntity<DepositoDTO> obtenerDepositoPorId(@PathVariable UUID id) {
        DepositoDTO deposito = depositoService.obtenerDepositoPorId(id);
        return ResponseEntity.ok(deposito);
    }

    // GET /depositos/{id}/contenedores - Contenedores en depósito (integración con ms-logistica)
    @GetMapping("/{id}/contenedores")
    public ResponseEntity<List<ContenedorListResponseDTO>> obtenerContenedoresEnDeposito(@PathVariable UUID id) {
        List<ContenedorListResponseDTO> contenedores = depositoService.obtenerContenedoresEnDeposito(id);
        return ResponseEntity.ok(contenedores);
    }

    // DELETE /depositos/{id} - Eliminar depósito (lógico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDeposito(@PathVariable UUID id) {
        depositoService.eliminarDeposito(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /depositos/{id}/estado - Activar/desactivar depósito
    @PatchMapping("/{id}/estado")
    public ResponseEntity<DepositoDTO> cambiarEstadoDeposito(
            @PathVariable UUID id,
            @RequestParam Boolean activo) {
        DepositoDTO depositoActualizado = depositoService.cambiarEstadoDeposito(id, activo);
        return ResponseEntity.ok(depositoActualizado);
    }
}