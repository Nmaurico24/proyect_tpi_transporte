package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.model.Estimada;
import ar.edu.utnfc.backend.ms_logistica.model.Moneda;
import ar.edu.utnfc.backend.ms_logistica.model.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.repository.EstimadaRepository;
import ar.edu.utnfc.backend.ms_logistica.repository.SolicitudRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EstimadaService {

    @Autowired
    private EstimadaRepository estimadaRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public EstimadaDTO crearEstimacion(EstimadaCreateDTO estimadaCreateDTO) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(estimadaCreateDTO.solicitudId())) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + estimadaCreateDTO.solicitudId());
        }
        
        // Convertir payload a JSON si es un objeto
        String payloadJson = null;
        if (estimadaCreateDTO.payloadJson() != null) {
            try {
                payloadJson = objectMapper.writeValueAsString(estimadaCreateDTO.payloadJson());
            } catch (JsonProcessingException e) {
                payloadJson = estimadaCreateDTO.payloadJson(); // Usar como string si falla la conversión
            }
        }
        
        Estimada estimada = Estimada.builder()
            .solicitudId(estimadaCreateDTO.solicitudId())
            .fuente(estimadaCreateDTO.fuente() != null ? estimadaCreateDTO.fuente() : "SISTEMA")
            .payloadJson(payloadJson)
            .distanciaKm(estimadaCreateDTO.distanciaKm())
            .duracionMin(estimadaCreateDTO.duracionMin())
            .costoTotal(estimadaCreateDTO.costoTotal())
            .moneda(estimadaCreateDTO.moneda() != null ? estimadaCreateDTO.moneda() : Moneda.ARS)
            .build();
        
        Estimada estimadaGuardada = estimadaRepository.save(estimada);
        
        return convertToEstimadaDTO(estimadaGuardada);
    }

    public EstimadaDTO obtenerEstimadaPorId(UUID id) {
        Estimada estimada = estimadaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Estimación no encontrada con ID: " + id));
        
        return convertToEstimadaDTO(estimada);
    }

    public List<EstimadaDTO> obtenerEstimacionesPorSolicitud(UUID solicitudId) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + solicitudId);
        }
        
        return estimadaRepository.findBySolicitudId(solicitudId).stream()
            .map(this::convertToEstimadaDTO)
            .collect(Collectors.toList());
    }

    public Optional<EstimadaDTO> obtenerUltimaEstimacionPorSolicitud(UUID solicitudId) {
        return estimadaRepository.findUltimaEstimacionBySolicitudId(solicitudId)
            .map(this::convertToEstimadaDTO);
    }

    public List<EstimadaDTO> obtenerEstimacionesRecientes() {
        LocalDateTime haceUnMes = LocalDateTime.now().minusMonths(1);
        return estimadaRepository.findEstimacionesRecientes(haceUnMes).stream()
            .map(this::convertToEstimadaDTO)
            .collect(Collectors.toList());
    }

    public List<EstimadaDTO> obtenerEstimacionesPorFuente(String fuente) {
        return estimadaRepository.findByFuente(fuente).stream()
            .map(this::convertToEstimadaDTO)
            .collect(Collectors.toList());
    }

    public List<EstimadaDTO> obtenerEstimacionesPorRangoCosto(BigDecimal costoMin, BigDecimal costoMax) {
        return estimadaRepository.findByCostoTotalBetween(costoMin, costoMax).stream()
            .map(this::convertToEstimadaDTO)
            .collect(Collectors.toList());
    }

    public List<EstimadaDTO> obtenerEstimacionesPorRangoDistancia(BigDecimal distanciaMin, BigDecimal distanciaMax) {
        return estimadaRepository.findByDistanciaKmBetween(distanciaMin, distanciaMax).stream()
            .map(this::convertToEstimadaDTO)
            .collect(Collectors.toList());
    }

    // ========== MÉTODOS DE ANÁLISIS Y ESTADÍSTICAS ==========

    public Map<String, Object> obtenerEstadisticasGenerales() {
        Object[] estadisticas = estimadaRepository.findEstadisticasGenerales();
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalEstimaciones", ((Number) estadisticas[0]).longValue());
        resultado.put("costoPromedio", estadisticas[1]);
        resultado.put("distanciaPromedio", estadisticas[2]);
        resultado.put("duracionPromedio", estadisticas[3]);
        
        return resultado;
    }

    public Map<String, Map<String, Object>> obtenerEstadisticasPorFuente() {
        List<Object[]> resultados = estimadaRepository.findEstadisticasPorFuente();
        
        Map<String, Map<String, Object>> estadisticas = new HashMap<>();
        for (Object[] resultado : resultados) {
            String fuente = (String) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            BigDecimal costoPromedio = (BigDecimal) resultado[2];
            BigDecimal distanciaPromedio = (BigDecimal) resultado[3];
            
            Map<String, Object> datosFuente = new HashMap<>();
            datosFuente.put("cantidad", cantidad);
            datosFuente.put("costoPromedio", costoPromedio);
            datosFuente.put("distanciaPromedio", distanciaPromedio);
            
            estadisticas.put(fuente, datosFuente);
        }
        
        return estadisticas;
    }

    public List<EstimadaDTO> obtenerEstimacionesMasPrecisas() {
        return estimadaRepository.findEstimacionesMasPrecisas().stream()
            .map(this::convertToEstimadaDTO)
            .limit(10) // Top 10 más precisas
            .collect(Collectors.toList());
    }

    public List<EstimadaDTO> obtenerEstimacionesMenosPrecisas() {
        return estimadaRepository.findEstimacionesMenosPrecisas().stream()
            .map(this::convertToEstimadaDTO)
            .limit(10) // Top 10 menos precisas
            .collect(Collectors.toList());
    }

    public Map<String, Object> analizarPrecisionEstimaciones() {
        List<Estimada> estimacionesConCostoFinal = estimadaRepository.findEstimacionesMasPrecisas();
        
        if (estimacionesConCostoFinal.isEmpty()) {
            return Map.of("mensaje", "No hay datos suficientes para analizar la precisión");
        }
        
        BigDecimal diferenciaTotal = BigDecimal.ZERO;
        int contador = 0;
        
        for (Estimada estimada : estimacionesConCostoFinal) {
            Optional<Solicitud> solicitudOpt = solicitudRepository.findById(estimada.getSolicitudId());
            if (solicitudOpt.isPresent() && solicitudOpt.get().getCostoFinal() != null) {
                Solicitud solicitud = solicitudOpt.get();
                BigDecimal diferencia = estimada.getCostoTotal().subtract(solicitud.getCostoFinal()).abs();
                diferenciaTotal = diferenciaTotal.add(diferencia);
                contador++;
            }
        }
        
        BigDecimal diferenciaPromedio = contador > 0 ? 
            diferenciaTotal.divide(BigDecimal.valueOf(contador), 2, BigDecimal.ROUND_HALF_UP) : 
            BigDecimal.ZERO;
        
        Map<String, Object> analisis = new HashMap<>();
        analisis.put("totalComparaciones", contador);
        analisis.put("diferenciaPromedio", diferenciaPromedio);
        analisis.put("precisionPromedio", calcularPorcentajePrecision(diferenciaPromedio));
        
        return analisis;
    }

    public Map<String, BigDecimal> obtenerCostoPromedioPorFuente() {
        List<Object[]> resultados = estimadaRepository.findCostoPromedioPorFuente();
        
        Map<String, BigDecimal> costosPromedio = new HashMap<>();
        for (Object[] resultado : resultados) {
            String fuente = (String) resultado[0];
            BigDecimal costoPromedio = (BigDecimal) resultado[1];
            costosPromedio.put(fuente, costoPromedio);
        }
        
        return costosPromedio;
    }

    // ========== MÉTODOS DE INTEGRACIÓN CON MS-RECURSOS ==========

    public EstimadaDTO crearEstimacionDesdeRecursos(UUID solicitudId, CostoEstimacionResponseDTO costoEstimacion) {
        // Crear estimación basada en la respuesta de ms-recursos
        EstimadaCreateDTO estimadaCreateDTO = new EstimadaCreateDTO(
            solicitudId,
            "MS-RECURSOS",
            convertMapToJsonString(costoEstimacion.desglose()),
            null, // distancia se puede calcular o obtener de otra fuente
            null, // duración se puede calcular o obtener de otra fuente
            costoEstimacion.totalEstimado(),
            costoEstimacion.moneda()
        );
        
        return crearEstimacion(estimadaCreateDTO);
    }

    // ========== MÉTODOS PRIVADOS ==========

private EstimadaDTO convertToEstimadaDTO(Estimada estimada) {
    return new EstimadaDTO(
        estimada.getId(),
        estimada.getSolicitudId(),
        estimada.getFuente(),
        estimada.getPayloadJson(),  // Directamente como String
        estimada.getDistanciaKm(),
        estimada.getDuracionMin(),
        estimada.getCostoTotal(),
        estimada.getMoneda(),
        estimada.getCreatedAt()
    );
}

    private String convertMapToJsonString(Map<String, Object> map) {
        if (map == null) return null;
        
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return map.toString();
        }
    }

    private BigDecimal calcularPorcentajePrecision(BigDecimal diferenciaPromedio) {
        // Suponiendo que una diferencia promedio de $1000 es 0% de precisión
        // y $0 es 100% de precisión (esto es un ejemplo, ajustar según el negocio)
        BigDecimal diferenciaMaxima = new BigDecimal("1000.00");
        if (diferenciaPromedio.compareTo(diferenciaMaxima) >= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal precision = BigDecimal.ONE
            .subtract(diferenciaPromedio.divide(diferenciaMaxima, 4, BigDecimal.ROUND_HALF_UP))
            .multiply(new BigDecimal("100"));
        
        return precision.max(BigDecimal.ZERO);
    }
}