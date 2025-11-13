package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.dto.ClienteCreateDTO;
import ar.edu.utnfc.backend.ms_logistica.dto.ClienteDTO;
import ar.edu.utnfc.backend.ms_logistica.dto.ClienteListDTO;
import ar.edu.utnfc.backend.ms_logistica.dto.ClienteUpdateDTO;
import ar.edu.utnfc.backend.ms_logistica.model.Cliente;
import ar.edu.utnfc.backend.ms_logistica.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteDTO crearCliente(ClienteCreateDTO clienteCreateDTO) {
        // Validar que no exista un cliente con el mismo número
        if (clienteRepository.existsByNumero(clienteCreateDTO.numero())) {
            throw new RuntimeException("Ya existe un cliente con el número: " + clienteCreateDTO.numero());
        }
        
        // Validar email único si se proporciona
        if (clienteCreateDTO.email() != null && !clienteCreateDTO.email().isBlank()) {
            if (clienteRepository.existsByEmail(clienteCreateDTO.email())) {
                throw new RuntimeException("Ya existe un cliente con el email: " + clienteCreateDTO.email());
            }
        }
        
        Cliente cliente = Cliente.builder()
            .numero(clienteCreateDTO.numero())
            .nombre(clienteCreateDTO.nombre())
            .telefono(clienteCreateDTO.telefono())
            .email(clienteCreateDTO.email())
            .isActive(clienteCreateDTO.isActive() != null ? clienteCreateDTO.isActive() : true)
            .build();
        
        Cliente clienteGuardado = clienteRepository.save(cliente);
        
        return convertToClienteDTO(clienteGuardado);
    }

    public ClienteDTO actualizarCliente(UUID id, ClienteUpdateDTO clienteUpdateDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        // Validar email único si se está cambiando
        if (clienteUpdateDTO.email() != null && 
            !clienteUpdateDTO.email().equals(clienteExistente.getEmail())) {
            if (clienteRepository.existsByEmail(clienteUpdateDTO.email())) {
                throw new RuntimeException("Ya existe otro cliente con el email: " + clienteUpdateDTO.email());
            }
            clienteExistente.setEmail(clienteUpdateDTO.email());
        }
        
        // Actualizar campos si se proporcionan
        if (clienteUpdateDTO.nombre() != null) {
            clienteExistente.setNombre(clienteUpdateDTO.nombre());
        }
        if (clienteUpdateDTO.telefono() != null) {
            clienteExistente.setTelefono(clienteUpdateDTO.telefono());
        }
        if (clienteUpdateDTO.isActive() != null) {
            clienteExistente.setIsActive(clienteUpdateDTO.isActive());
        }
        
        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        
        return convertToClienteDTO(clienteActualizado);
    }

    public ClienteDTO obtenerClientePorId(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        return convertToClienteDTO(cliente);
    }

    public ClienteDTO obtenerClientePorNumero(String numero) {
        Cliente cliente = clienteRepository.findByNumero(numero)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con número: " + numero));
        
        return convertToClienteDTO(cliente);
    }

    public Page<ClienteListDTO> obtenerPagina(Pageable pageable) {
        Page<Cliente> clientesPage = clienteRepository.findAll(pageable);
        return clientesPage.map(this::convertToClienteListDTO);
    }

    public void eliminarCliente(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        // Eliminación lógica
        cliente.setIsActive(false);
        clienteRepository.save(cliente);
    }

    public ClienteDTO cambiarEstadoCliente(UUID id, Boolean activo) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        cliente.setIsActive(activo);
        Cliente clienteActualizado = clienteRepository.save(cliente);
        
        return convertToClienteDTO(clienteActualizado);
    }

    // ========== MÉTODOS ADICIONALES DE CONSULTA ==========

    public List<ClienteListDTO> obtenerClientesActivos() {
        return clienteRepository.findByIsActive(true).stream()
            .map(this::convertToClienteListDTO)
            .collect(Collectors.toList());
    }

    public List<ClienteListDTO> buscarClientesPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
            .map(this::convertToClienteListDTO)
            .collect(Collectors.toList());
    }

    public List<ClienteListDTO> buscarClientesPorNumero(String numero) {
        return clienteRepository.findByNumeroContaining(numero).stream()
            .map(this::convertToClienteListDTO)
            .collect(Collectors.toList());
    }

    public List<ClienteListDTO> obtenerClientesConSolicitudesActivas() {
        return clienteRepository.findClientesConSolicitudesActivas().stream()
            .map(this::convertToClienteListDTO)
            .collect(Collectors.toList());
    }

    public long contarClientesActivos() {
        return clienteRepository.countByIsActiveTrue();
    }

    public Map<String, Object> obtenerEstadisticasClientes() {
        Object[] estadisticas = clienteRepository.findEstadisticasClientes();
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalClientes", estadisticas[0]);
        resultado.put("clientesActivos", estadisticas[1]);
        
        return resultado;
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    private ClienteDTO convertToClienteDTO(Cliente cliente) {
        return new ClienteDTO(
            cliente.getId(),
            cliente.getNumero(),
            cliente.getNombre(),
            cliente.getTelefono(),
            cliente.getEmail(),
            cliente.getIsActive(),
            cliente.getCreatedAt(),
            cliente.getUpdatedAt()
        );
    }

    private ClienteListDTO convertToClienteListDTO(Cliente cliente) {
        return new ClienteListDTO(
            cliente.getId(),
            cliente.getNumero(),
            cliente.getNombre(),
            cliente.getTelefono(),
            cliente.getEmail(),
            cliente.getIsActive()
        );
    }
}