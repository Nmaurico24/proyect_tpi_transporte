package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.model.Contenedor;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoContenedor;
import ar.edu.utnfc.backend.ms_logistica.repository.ContenedorRepository;
import ar.edu.utnfc.backend.ms_logistica.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContenedorService {

    @Autowired
    private ContenedorRepository contenedorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public ContenedorDTO crearContenedor(ContenedorCreateDTO contenedorCreateDTO) {
        // Validar que el cliente existe
        if (!clienteRepository.existsById(contenedorCreateDTO.clienteId())) {
            throw new RuntimeException("Cliente no encontrado con ID: " + contenedorCreateDTO.clienteId());
        }
        
        // Validar que no exista un contenedor con la misma etiqueta
        if (contenedorRepository.existsByEtiqueta(contenedorCreateDTO.etiqueta())) {
            throw new RuntimeException("Ya existe un contenedor con la etiqueta: " + contenedorCreateDTO.etiqueta());
        }
        
        Contenedor contenedor = Contenedor.builder()
            .clienteId(contenedorCreateDTO.clienteId())
            .etiqueta(contenedorCreateDTO.etiqueta())
            .pesoKg(contenedorCreateDTO.pesoKg())
            .volumenM3(contenedorCreateDTO.volumenM3())
            .estado(contenedorCreateDTO.estado() != null ? 
                    EstadoContenedor.valueOf(contenedorCreateDTO.estado()) : 
                    EstadoContenedor.DISPONIBLE)
            .isActive(contenedorCreateDTO.isActive() != null ? contenedorCreateDTO.isActive() : true)
            .build();
        
        Contenedor contenedorGuardado = contenedorRepository.save(contenedor);
        
        return convertToContenedorDTO(contenedorGuardado);
    }

    public ContenedorDTO actualizarContenedor(UUID id, ContenedorUpdateDTO contenedorUpdateDTO) {
        Contenedor contenedorExistente = contenedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con ID: " + id));
        
        // Validar etiqueta única si se está cambiando
        if (contenedorUpdateDTO.etiqueta() != null && 
            !contenedorUpdateDTO.etiqueta().equals(contenedorExistente.getEtiqueta())) {
            if (contenedorRepository.existsByEtiqueta(contenedorUpdateDTO.etiqueta())) {
                throw new RuntimeException("Ya existe otro contenedor con la etiqueta: " + contenedorUpdateDTO.etiqueta());
            }
            contenedorExistente.setEtiqueta(contenedorUpdateDTO.etiqueta());
        }
        
        // Actualizar campos si se proporcionan
        if (contenedorUpdateDTO.pesoKg() != null) {
            contenedorExistente.setPesoKg(contenedorUpdateDTO.pesoKg());
        }
        if (contenedorUpdateDTO.volumenM3() != null) {
            contenedorExistente.setVolumenM3(contenedorUpdateDTO.volumenM3());
        }
        if (contenedorUpdateDTO.estado() != null) {
            contenedorExistente.setEstado(EstadoContenedor.valueOf(contenedorUpdateDTO.estado()));
        }
        if (contenedorUpdateDTO.isActive() != null) {
            contenedorExistente.setIsActive(contenedorUpdateDTO.isActive());
        }
        
        Contenedor contenedorActualizado = contenedorRepository.save(contenedorExistente);
        
        return convertToContenedorDTO(contenedorActualizado);
    }

    public ContenedorDTO obtenerContenedorPorId(UUID id) {
        Contenedor contenedor = contenedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con ID: " + id));
        
        return convertToContenedorDTO(contenedor);
    }

    public ContenedorDTO obtenerContenedorPorEtiqueta(String etiqueta) {
        Contenedor contenedor = contenedorRepository.findByEtiqueta(etiqueta)
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con etiqueta: " + etiqueta));
        
        return convertToContenedorDTO(contenedor);
    }

    public Page<ContenedorListDTO> obtenerPagina(Pageable pageable) {
        Page<Contenedor> contenedoresPage = contenedorRepository.findAll(pageable);
        return contenedoresPage.map(this::convertToContenedorListDTO);
    }

    public void eliminarContenedor(UUID id) {
        Contenedor contenedor = contenedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con ID: " + id));
        
        // Eliminación lógica
        contenedor.setIsActive(false);
        contenedor.setEstado(EstadoContenedor.MANTENIMIENTO);
        contenedorRepository.save(contenedor);
    }

    public ContenedorDTO cambiarEstadoContenedor(UUID id, String estado) {
        Contenedor contenedor = contenedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con ID: " + id));
        
        contenedor.setEstado(EstadoContenedor.valueOf(estado));
        Contenedor contenedorActualizado = contenedorRepository.save(contenedor);
        
        return convertToContenedorDTO(contenedorActualizado);
    }

    // ========== MÉTODOS ADICIONALES DE CONSULTA ==========

    public List<ContenedorListDTO> obtenerContenedoresPorCliente(UUID clienteId) {
        return contenedorRepository.findByClienteId(clienteId).stream()
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public List<ContenedorListDTO> obtenerContenedoresPorEstado(String estado) {
        return contenedorRepository.findByEstado(EstadoContenedor.valueOf(estado)).stream()
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public List<ContenedorListDTO> obtenerContenedoresActivosPorCliente(UUID clienteId) {
        return contenedorRepository.findByClienteIdAndIsActiveTrue(clienteId).stream()
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public List<ContenedorListDTO> obtenerContenedoresDisponibles() {
        return contenedorRepository.findByEstadoAndIsActiveTrue(EstadoContenedor.DISPONIBLE).stream()
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public List<ContenedorListDTO> buscarContenedoresPorEtiqueta(String etiqueta) {
        return contenedorRepository.findByEtiquetaContainingIgnoreCase(etiqueta).stream()
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public List<ContenedorListDTO> obtenerContenedoresDisponiblesConCapacidad(BigDecimal pesoMin, BigDecimal volumenMin) {
        return contenedorRepository.findContenedoresDisponiblesConCapacidad(pesoMin, volumenMin).stream()
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public List<ContenedorListDTO> obtenerContenedoresEnTransito() {
        return contenedorRepository.findContenedoresEnTransito().stream()
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public Map<String, Object> obtenerEstadisticasContenedores() {
        List<Object[]> resultados = contenedorRepository.findEstadisticasPorEstado();
        Object[] capacidadTotal = contenedorRepository.findCapacidadTotalDisponible();
        
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Estadísticas por estado
        for (Object[] resultado : resultados) {
            String estado = (String) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            BigDecimal pesoPromedio = (BigDecimal) resultado[2];
            BigDecimal volumenPromedio = (BigDecimal) resultado[3];
            
            Map<String, Object> datosEstado = new HashMap<>();
            datosEstado.put("cantidad", cantidad);
            datosEstado.put("pesoPromedio", pesoPromedio);
            datosEstado.put("volumenPromedio", volumenPromedio);
            
            estadisticas.put(estado, datosEstado);
        }
        
        // Capacidad total disponible
        estadisticas.put("capacidadTotalDisponible", Map.of(
            "pesoTotal", capacidadTotal[0],
            "volumenTotal", capacidadTotal[1]
        ));
        
        return estadisticas;
    }

    public long contarContenedoresActivosPorCliente(UUID clienteId) {
        return contenedorRepository.countByClienteIdAndIsActiveTrue(clienteId);
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    private ContenedorDTO convertToContenedorDTO(Contenedor contenedor) {
        return new ContenedorDTO(
            contenedor.getId(),
            contenedor.getClienteId(),
            contenedor.getEtiqueta(),
            contenedor.getPesoKg(),
            contenedor.getVolumenM3(),
            contenedor.getEstado().toString(),
            contenedor.getIsActive(),
            contenedor.getCreatedAt(),
            contenedor.getUpdatedAt()
        );
    }

    private ContenedorListDTO convertToContenedorListDTO(Contenedor contenedor) {
        // Aquí podrías obtener el nombre del cliente si lo necesitas
        String clienteNombre = "Cliente"; // Podrías hacer una consulta para obtener el nombre real
        
        return new ContenedorListDTO(
            contenedor.getId(),
            contenedor.getClienteId(),
            clienteNombre,
            contenedor.getEtiqueta(),
            contenedor.getPesoKg(),
            contenedor.getVolumenM3(),
            contenedor.getEstado().toString(),
            contenedor.getIsActive()
        );
    }
}