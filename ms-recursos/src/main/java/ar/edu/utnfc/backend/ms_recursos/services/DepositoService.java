package ar.edu.utnfc.backend.ms_recursos.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utnfc.backend.ms_recursos.dto.*;
import ar.edu.utnfc.backend.ms_recursos.model.Deposito;
import ar.edu.utnfc.backend.ms_recursos.repository.DepositoRepository;

@Service
@Transactional
public class DepositoService {

    @Autowired
    private DepositoRepository depositoRepository;

    public DepositoDTO crearDeposito(DepositoCreateDTO depositoCreateDTO) {
        // Validar que no exista un depósito con el mismo nombre
        if (depositoRepository.existsByNombre(depositoCreateDTO.nombre())) {
            throw new RuntimeException("Ya existe un depósito con el nombre: " + depositoCreateDTO.nombre());
        }
        
        Deposito deposito = Deposito.builder()
            .nombre(depositoCreateDTO.nombre())
            .direccion(depositoCreateDTO.direccion())
            .lat(depositoCreateDTO.lat())
            .lng(depositoCreateDTO.lng())
            .costoDiario(depositoCreateDTO.costoDiario())
            .lsActive(depositoCreateDTO.lsActive() != null ? depositoCreateDTO.lsActive() : true)
            .build();
        
        Deposito depositoGuardado = depositoRepository.save(deposito);
        
        return convertToDepositoDTO(depositoGuardado);
    }

    public DepositoDTO actualizarDeposito(UUID id, DepositoCreateDTO depositoUpdateDTO) {
        Deposito depositoExistente = depositoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Depósito no encontrado con ID: " + id));
        
        // Validar que el nuevo nombre no esté en uso por otro depósito
        if (!depositoExistente.getNombre().equals(depositoUpdateDTO.nombre())) {
            if (depositoRepository.existsByNombre(depositoUpdateDTO.nombre())) {
                throw new RuntimeException("Ya existe otro depósito con el nombre: " + depositoUpdateDTO.nombre());
            }
            depositoExistente.setNombre(depositoUpdateDTO.nombre());
        }
        
        // Actualizar campos
        depositoExistente.setDireccion(depositoUpdateDTO.direccion());
        depositoExistente.setLat(depositoUpdateDTO.lat());
        depositoExistente.setLng(depositoUpdateDTO.lng());
        depositoExistente.setCostoDiario(depositoUpdateDTO.costoDiario());
        
        if (depositoUpdateDTO.lsActive() != null) {
            depositoExistente.setLsActive(depositoUpdateDTO.lsActive());
        }
        
        Deposito depositoActualizado = depositoRepository.save(depositoExistente);
        
        return convertToDepositoDTO(depositoActualizado);
    }

    public DepositoDTO obtenerDepositoPorId(UUID id) {
        Deposito deposito = depositoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Depósito no encontrado con ID: " + id));
        
        return convertToDepositoDTO(deposito);
    }

    public Page<DepositoListDTO> obtenerPagina(Pageable pageable) {
        Page<Deposito> depositosPage = depositoRepository.findAll(pageable);
        return depositosPage.map(this::convertToDepositoListDTO);
    }

    public List<ContenedorListResponseDTO> obtenerContenedoresEnDeposito(UUID depositoId) {
        // Validar que el depósito existe
        depositoRepository.findById(depositoId)
            .orElseThrow(() -> new RuntimeException("Depósito no encontrado con ID: " + depositoId));
        
        // TODO: Integración con ms-logistica
        // Por ahora retornamos lista vacía
        return List.of();
    }

    public void eliminarDeposito(UUID id) {
        Deposito deposito = depositoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Depósito no encontrado con ID: " + id));
        
        // Eliminación lógica
        deposito.setLsActive(false);
        depositoRepository.save(deposito);
    }

    public DepositoDTO cambiarEstadoDeposito(UUID id, Boolean activo) {
        Deposito deposito = depositoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Depósito no encontrado con ID: " + id));
        
        deposito.setLsActive(activo);
        Deposito depositoActualizado = depositoRepository.save(deposito);
        
        return convertToDepositoDTO(depositoActualizado);
    }

    // ========== MÉTODOS ADICIONALES CONSULTA ==========

    public List<DepositoListDTO> obtenerDepositosActivos() {
        return depositoRepository.findByLsActive(true).stream()
            .map(this::convertToDepositoListDTO)
            .collect(Collectors.toList());
    }

    public List<DepositoListDTO> buscarDepositosPorNombre(String nombre) {
        return depositoRepository.findByNombreContainingIgnoreCase(nombre).stream()
            .map(this::convertToDepositoListDTO)
            .collect(Collectors.toList());
    }

    public List<DepositoListDTO> obtenerDepositosPorRangoCosto(BigDecimal costoMin, BigDecimal costoMax) {
        List<Deposito> depositos;
        
        if (costoMin != null && costoMax != null) {
            depositos = depositoRepository.findByCostoDiarioBetween(costoMin, costoMax);
        } else if (costoMin != null) {
            depositos = depositoRepository.findByCostoDiarioLessThanEqual(costoMin);
        } else if (costoMax != null) {
            depositos = depositoRepository.findByCostoDiarioLessThanEqual(costoMax);
        } else {
            depositos = depositoRepository.findAll();
        }
        
        return depositos.stream()
            .map(this::convertToDepositoListDTO)
            .collect(Collectors.toList());
    }

    public List<DepositoListDTO> obtenerDepositosCercanos(BigDecimal lat, BigDecimal lng, Double radioKm) {
        List<Deposito> depositosCercanos = depositoRepository.findDepositosCercanos(lat, lng, radioKm);
        
        return depositosCercanos.stream()
            .map(this::convertToDepositoListDTO)
            .collect(Collectors.toList());
    }

    public List<DepositoListDTO> obtenerDepositosEconomicos() {
        return depositoRepository.findByLsActiveTrueOrderByCostoDiarioAsc().stream()
            .map(this::convertToDepositoListDTO)
            .collect(Collectors.toList());
    }

    public BigDecimal obtenerCostoPromedioDepositos() {
        return depositoRepository.findCostoDiarioPromedioActivos()
            .orElse(BigDecimal.ZERO);
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    private DepositoDTO convertToDepositoDTO(Deposito deposito) {
        return new DepositoDTO(
            deposito.getId(),
            deposito.getNombre(),
            deposito.getDireccion(),
            deposito.getLat(),
            deposito.getLng(),
            deposito.getCostoDiario(),
            deposito.getLsActive(),
            deposito.getCreatedAt(),
            deposito.getUpdatedAt()
        );
    }

    private DepositoListDTO convertToDepositoListDTO(Deposito deposito) {
        return new DepositoListDTO(
            deposito.getId(),
            deposito.getNombre(),
            deposito.getDireccion(),
            deposito.getCostoDiario(),
            deposito.getLsActive()
        );
    }
}