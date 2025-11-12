package ar.edu.utnfc.backend.ms_recursos.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utnfc.backend.ms_recursos.dto.TarifaDTO;
import ar.edu.utnfc.backend.ms_recursos.dto.TarifaRuleDTO;
import ar.edu.utnfc.backend.ms_recursos.model.Tarifa;
import ar.edu.utnfc.backend.ms_recursos.model.TarifaCamion;
import ar.edu.utnfc.backend.ms_recursos.model.TarifaDeposito;
import ar.edu.utnfc.backend.ms_recursos.model.UnidadTarifa;
import ar.edu.utnfc.backend.ms_recursos.repository.TarifaCamionRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.TarifaDepositoRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.TarifaRepository;

@Service
@Transactional
public class TarifaService {

    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    private TarifaCamionRepository tarifaCamionRepository;

    @Autowired
    private TarifaDepositoRepository tarifaDepositoRepository;

public List<TarifaDTO> actualizarTarifas(List<TarifaRuleDTO> tarifaRules) {
    List<TarifaDTO> tarifasActualizadas = new ArrayList<>();
    
    for (TarifaRuleDTO ruleDTO : tarifaRules) {
        try {
            Tarifa tarifaProcesada;
            
            // Validar datos requeridos
            if (ruleDTO.description() == null || ruleDTO.description().trim().isEmpty()) {
                throw new RuntimeException("La descripción de la tarifa es requerida");
            }
            
            if (ruleDTO.version() == null) {
                throw new RuntimeException("La versión de la tarifa es requerida");
            }
            
            // Buscar si ya existe una tarifa con misma descripción y versión
            java.util.Optional<Tarifa> tarifaExistenteOpt = tarifaRepository.findByDescriptionAndVersion(
                ruleDTO.description().trim(), ruleDTO.version());
            
            if (tarifaExistenteOpt.isPresent()) {
                // Actualizar tarifa existente
                Tarifa tarifaExistente = tarifaExistenteOpt.get();
                actualizarTarifaExistente(tarifaExistente, ruleDTO);
                tarifaProcesada = tarifaRepository.save(tarifaExistente);
            } else {
                // Crear nueva tarifa
                Tarifa nuevaTarifa = crearNuevaTarifa(ruleDTO);
                tarifaProcesada = tarifaRepository.save(nuevaTarifa);
                
                // Guardar relaciones para nueva tarifa
                guardarRelacionesTarifa(tarifaProcesada, ruleDTO);
            }
            
            tarifasActualizadas.add(convertToTarifaDTO(tarifaProcesada));
            
        } catch (Exception e) {
            // Log del error y continuar con las siguientes tarifas
            System.err.println("Error procesando tarifa: " + ruleDTO.description() + " - " + e.getMessage());
            // Podrías lanzar una excepción personalizada aquí si prefieres
        }
    }
    
    return tarifasActualizadas;
}

    public TarifaDTO crearReglaTarifaria(TarifaRuleDTO tarifaRuleDTO) {
        // Validar que no exista una tarifa igual activa
        boolean existeTarifaSimilar = tarifaRepository.findTarifasVigentes().stream()
            .anyMatch(t -> t.getDescription().equals(tarifaRuleDTO.description())
                    && t.getUnidad().equals(tarifaRuleDTO.unidad()));
        
        if (existeTarifaSimilar) {
            throw new RuntimeException("Ya existe una tarifa activa con la misma descripción y unidad");
        }
        
        Tarifa nuevaTarifa = crearNuevaTarifa(tarifaRuleDTO);
        Tarifa tarifaGuardada = tarifaRepository.save(nuevaTarifa);
        
        // Guardar relaciones si se especificaron
        guardarRelacionesTarifa(tarifaGuardada, tarifaRuleDTO);
        
        return convertToTarifaDTO(tarifaGuardada);
    }

    public List<TarifaDTO> obtenerTarifasVigentes() {
        return tarifaRepository.findTarifasVigentes().stream()
            .map(this::convertToTarifaDTO)
            .collect(Collectors.toList());
    }

    public TarifaDTO obtenerTarifaPorId(UUID id) {
        Tarifa tarifa = tarifaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));
        
        return convertToTarifaDTO(tarifa);
    }

    public void desactivarTarifa(UUID id) {
        Tarifa tarifa = tarifaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));
        
        // Establecer vigencia pasada
        tarifa.setVigenciaHasta(LocalDate.now().minusDays(1));
        tarifaRepository.save(tarifa);
    }

    public List<TarifaDTO> obtenerTarifasPorUnidad(UnidadTarifa unidad) {
        return tarifaRepository.findTarifasVigentesByUnidad(unidad).stream()
            .map(this::convertToTarifaDTO)
            .collect(Collectors.toList());
    }

    public List<TarifaDTO> buscarTarifasPorDescripcion(String descripcion) {
        return tarifaRepository.findByDescriptionContainingIgnoreCase(descripcion).stream()
            .map(this::convertToTarifaDTO)
            .collect(Collectors.toList());
    }

    public List<UnidadTarifa> obtenerUnidadesTarifariasDisponibles() {
        return tarifaRepository.findUnidadesTarifariasVigentes();
    }

    public TarifaDTO obtenerUltimaVersionPorDescripcion(String descripcion) {
        Tarifa tarifa = tarifaRepository.findUltimaVersionByDescription(descripcion)
            .orElseThrow(() -> new RuntimeException("No se encontró tarifa con descripción: " + descripcion));
        
        return convertToTarifaDTO(tarifa);
    }

    // ========== MÉTODOS PRIVADOS DE APOYO ==========

    private Tarifa crearNuevaTarifa(TarifaRuleDTO ruleDTO) {
        return Tarifa.builder()
            .description(ruleDTO.description())
            .unidad(ruleDTO.unidad())
            .valor(ruleDTO.valor())
            .moneda(ruleDTO.moneda())
            .vigenciaHasta(ruleDTO.vigenciaHasta ())
            .version(ruleDTO.version() != null ? ruleDTO.version() : 1)
            .build();
    }

    private void actualizarTarifaExistente(Tarifa tarifa, TarifaRuleDTO ruleDTO) {
        tarifa.setUnidad(ruleDTO.unidad());
        tarifa.setValor(ruleDTO.valor());
        tarifa.setMoneda(ruleDTO.moneda());
        tarifa.setVigenciaHasta(ruleDTO.vigenciaHasta ());
        
        // Eliminar relaciones existentes y crear nuevas
        eliminarRelacionesExistentes(tarifa.getId());
        
        // Guardar nuevas relaciones
        guardarRelacionesTarifa(tarifa, ruleDTO);
    }

    private void guardarRelacionesTarifa(Tarifa tarifa, TarifaRuleDTO ruleDTO) {
        // Guardar relaciones con camiones si se especificaron
        if (ruleDTO.camionesIds() != null && !ruleDTO.camionesIds().isEmpty()) {
            for (UUID camionId : ruleDTO.camionesIds()) {
                if (!tarifaCamionRepository.existsByTarifaIdAndCamionId(tarifa.getId(), camionId)) {
                    TarifaCamion tarifaCamion = TarifaCamion.builder()
                        .tarifaId(tarifa.getId())
                        .camionId(camionId)
                        .build();
                    tarifaCamionRepository.save(tarifaCamion);
                }
            }
        }
        
        // Guardar relaciones con depósitos si se especificaron
        if (ruleDTO.depositosIds() != null && !ruleDTO.depositosIds().isEmpty()) {
            for (UUID depositoId : ruleDTO.depositosIds()) {
                if (!tarifaDepositoRepository.existsByTarifaIdAndDepositoId(tarifa.getId(), depositoId)) {
                    TarifaDeposito tarifaDeposito = TarifaDeposito.builder()
                        .tarifaId(tarifa.getId())
                        .depositoId(depositoId)
                        .build();
                    tarifaDepositoRepository.save(tarifaDeposito);
                }
            }
        }
    }

    private void eliminarRelacionesExistentes(UUID tarifaId) {
        tarifaCamionRepository.deleteByTarifaId(tarifaId);
        tarifaDepositoRepository.deleteByTarifaId(tarifaId);
    }

    private TarifaDTO convertToTarifaDTO(Tarifa tarifa) {
        // Obtener camiones aplicables para esta tarifa
        List<UUID> camionesAplicables = tarifaCamionRepository.findByTarifaId(tarifa.getId()).stream()
            .map(TarifaCamion::getCamionId)
            .collect(Collectors.toList());
        
        // Obtener depósitos aplicables para esta tarifa
        List<UUID> depositosAplicables = tarifaDepositoRepository.findByTarifaId(tarifa.getId()).stream()
            .map(TarifaDeposito::getDepositoId)
            .collect(Collectors.toList());
        
        return new TarifaDTO(
            tarifa.getId(),
            tarifa.getDescription(),
            tarifa.getUnidad(),
            tarifa.getValor(),
            tarifa.getMoneda(),
            tarifa.getVigenciaHasta(),
            tarifa.getVersion(),
            camionesAplicables,
            depositosAplicables,
            tarifa.getCreatedAt(),
            tarifa.getUpdatedAt()
        );
    }

    // ========== MÉTODOS PARA OBTENER TARIFAS POR RECURSO ==========

    public List<TarifaDTO> obtenerTarifasPorCamion(UUID camionId) {
        List<TarifaCamion> relaciones = tarifaCamionRepository.findTarifasVigentesByCamion(camionId);
        
        return relaciones.stream()
            .map(rel -> tarifaRepository.findById(rel.getTarifaId()))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .map(this::convertToTarifaDTO)
            .collect(Collectors.toList());
    }

    public List<TarifaDTO> obtenerTarifasPorDeposito(UUID depositoId) {
        List<TarifaDeposito> relaciones = tarifaDepositoRepository.findTarifasVigentesByDeposito(depositoId);
        
        return relaciones.stream()
            .map(rel -> tarifaRepository.findById(rel.getTarifaId()))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .map(this::convertToTarifaDTO)
            .collect(Collectors.toList());
    }

    // ========== MÉTODOS DE VALIDACIÓN ==========

    public boolean validarTarifaVigente(UUID tarifaId) {
        return tarifaRepository.findById(tarifaId)
            .map(tarifa -> tarifa.getVigenciaHasta().isAfter(LocalDate.now().minusDays(1)))
            .orElse(false);
    }

    public BigDecimal obtenerValorTarifaVigente(UnidadTarifa unidad) {
        return tarifaRepository.findTarifasVigentesByUnidad(unidad).stream()
            .findFirst()
            .map(Tarifa::getValor)
            .orElse(BigDecimal.ZERO);
    }
}