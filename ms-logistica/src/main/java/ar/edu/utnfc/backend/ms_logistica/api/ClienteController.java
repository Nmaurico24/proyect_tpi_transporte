package ar.edu.utnfc.backend.ms_logistica.api;

import ar.edu.utnfc.backend.ms_logistica.dto.ClienteCreateDTO;
import ar.edu.utnfc.backend.ms_logistica.dto.ClienteDTO;
import ar.edu.utnfc.backend.ms_logistica.dto.ClienteListDTO;
import ar.edu.utnfc.backend.ms_logistica.dto.ClienteUpdateDTO;
import ar.edu.utnfc.backend.ms_logistica.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteCreateDTO clienteCreateDTO) {
        ClienteDTO clienteCreado = clienteService.crearCliente(clienteCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(
            @PathVariable UUID id, 
            @Valid @RequestBody ClienteUpdateDTO clienteUpdateDTO) {
        ClienteDTO clienteActualizado = clienteService.actualizarCliente(id, clienteUpdateDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable UUID id) {
        ClienteDTO cliente = clienteService.obtenerClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<ClienteDTO> obtenerClientePorNumero(@PathVariable String numero) {
        ClienteDTO cliente = clienteService.obtenerClientePorNumero(numero);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteListDTO>> listarClientes(Pageable pageable) {
        Page<ClienteListDTO> clientes = clienteService.obtenerPagina(pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ClienteListDTO>> obtenerClientesActivos() {
        List<ClienteListDTO> clientes = clienteService.obtenerClientesActivos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<ClienteListDTO>> buscarClientesPorNombre(@RequestParam String nombre) {
        List<ClienteListDTO> clientes = clienteService.buscarClientesPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar/numero")
    public ResponseEntity<List<ClienteListDTO>> buscarClientesPorNumero(@RequestParam String numero) {
        List<ClienteListDTO> clientes = clienteService.buscarClientesPorNumero(numero);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/con-solicitudes-activas")
    public ResponseEntity<List<ClienteListDTO>> obtenerClientesConSolicitudesActivas() {
        List<ClienteListDTO> clientes = clienteService.obtenerClientesConSolicitudesActivas();
        return ResponseEntity.ok(clientes);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ClienteDTO> cambiarEstadoCliente(
            @PathVariable UUID id,
            @RequestParam Boolean activo) {
        ClienteDTO clienteActualizado = clienteService.cambiarEstadoCliente(id, activo);
        return ResponseEntity.ok(clienteActualizado);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasClientes() {
        Map<String, Object> estadisticas = clienteService.obtenerEstadisticasClientes();
        return ResponseEntity.ok(estadisticas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}