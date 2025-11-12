package ar.edu.utnfc.backend.ms_recursos.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utnfc.backend.ms_recursos.dto.*;
import ar.edu.utnfc.backend.ms_recursos.model.*;
import ar.edu.utnfc.backend.ms_recursos.repository.*;

@Service
@Transactional
public class CostoService {

    @Autowired
    private CostoEstimacionRepository costoEstimacionRepository;

    @Autowired
    private CostoFinalRepository costoFinalRepository;

    @Autowired
    private TarifaRepository tarifaRepository;


    @Autowired
    private TarifaCamionRepository tarifaCamionRepository;  // ← NUEVA
    @Autowired
    private CamionRepository camionRepository;

    @Autowired
    private DepositoRepository depositoRepository;

    public CostosEstimacionResponseDTO calcularCostoEstimado(CostosEstimacionRequestDTO request) {
        // Validar que la solicitud no tenga ya una estimación
        if (costoEstimacionRepository.existsBySolicitudRef(request.solicitudId().toString())) {
            throw new RuntimeException("Ya existe una estimación para la solicitud: " + request.solicitudId());
        }

        Map<String, Object> desglose = new HashMap<>();
        BigDecimal totalEstimado = BigDecimal.ZERO;

        // Calcular costos por cada segmento
        for (SegmentoCostoDTO segmento : request.segmentos()) {
            BigDecimal costoSegmento = calcularCostoSegmento(segmento);
            totalEstimado = totalEstimado.add(costoSegmento);

            desglose.put("segmento_" + segmento.tramoId(), Map.of(
                    "costo", costoSegmento,
                    "distancia", segmento.distanciaKm(),
                    "duracion", segmento.duracionEstimadaMin()));
        }

        // Agregar costos fijos
        BigDecimal costosFijos = calcularCostosFijos(request);
        totalEstimado = totalEstimado.add(costosFijos);
        desglose.put("costos_fijos", costosFijos);

        // Crear y guardar la estimación
        CostoEstimacion estimacion = CostoEstimacion.builder()
                .solicitudRef(request.solicitudId().toString())
                .rutaPayipro(convertToJson(request.rutaRef()))
                .totalEstimado(totalEstimado)
                .moneda(Moneda.ARS)
                .despose(convertToJson(desglose))
                .build();

        CostoEstimacion estimacionGuardada = costoEstimacionRepository.save(estimacion);

        return new CostosEstimacionResponseDTO(
                estimacionGuardada.getId(),
                request.solicitudId(),
                request.rutaRef(),
                totalEstimado,
                estimacionGuardada.getMoneda(),
                desglose,
                estimacionGuardada.getCreateEn());
    }

    public CostosFinalResponseDTO calcularCostoFinal(CostosFinalRequestDTO request) {
        // Buscar estimación previa para esta ruta
        Optional<CostoEstimacion> estimacionOpt = costoEstimacionRepository
                .findByRutaPayiproContaining(request.rutaRef())
                .stream()
                .findFirst();

        BigDecimal totalFinal = BigDecimal.ZERO;
        Map<String, Object> desglose = new HashMap<>();

        if (estimacionOpt.isPresent()) {
            // Partir de la estimación y ajustar con valores reales
            CostoEstimacion estimacion = estimacionOpt.get();
            totalFinal = estimacion.getTotalEstimado();
            desglose = parseJsonToMap(estimacion.getDespose());

            // Ajustar por combustible real vs estimado
            BigDecimal costoCombustibleExtra = calcularCostoCombustibleExtra(
                    request.litrosConsumidos(), estimacion);
            totalFinal = totalFinal.add(costoCombustibleExtra);
            desglose.put("ajuste_combustible", costoCombustibleExtra);

            // Ajustar por días extra
            if (request.estadiasDias() > 0) {
                BigDecimal costoDiasExtra = calcularCostoDiasExtra(request.estadiasDias());
                totalFinal = totalFinal.add(costoDiasExtra);
                desglose.put("dias_extra", costoDiasExtra);
            }
        } else {
            // Calcular desde cero si no hay estimación
            totalFinal = calcularCostoDesdeCero(request);
            desglose.put("calculado_desde_cero", true);
        }

        // Agregar costos adicionales si los hay
        if (request.costosAdicionales() != null && !request.costosAdicionales().isEmpty()) {
            BigDecimal costosAdicionales = calcularCostosAdicionales(request.costosAdicionales());
            totalFinal = totalFinal.add(costosAdicionales);
            desglose.put("costos_adicionales", costosAdicionales);
        }

        // Crear y guardar el costo final
        CostoFinal costoFinal = CostoFinal.builder()
                .rutaRef(request.rutaRef())
                .litros(request.litrosConsumidos())
                .estadiasDias(request.estadiasDias())
                .totalFinal(totalFinal)
                .moneda(Moneda.ARS)
                .desposeJson(convertToJson(desglose))
                .build();

        CostoFinal costoFinalGuardado = costoFinalRepository.save(costoFinal);

        return new CostosFinalResponseDTO(
                costoFinalGuardado.getId(),
                request.rutaRef(),
                request.litrosConsumidos(),
                request.estadiasDias(),
                totalFinal,
                costoFinalGuardado.getMoneda(),
                desglose,
                costoFinalGuardado.getCreateEn());
    }

    public CostosEstimacionResponseDTO obtenerEstimacionPorSolicitud(UUID solicitudId) {
        CostoEstimacion estimacion = costoEstimacionRepository.findBySolicitudRef(solicitudId.toString())
                .orElseThrow(() -> new RuntimeException("Estimación no encontrada para solicitud: " + solicitudId));

        return new CostosEstimacionResponseDTO(
                estimacion.getId(),
                solicitudId,
                extractRutaRefFromJson(estimacion.getRutaPayipro()),
                estimacion.getTotalEstimado(),
                estimacion.getMoneda(),
                parseJsonToMap(estimacion.getDespose()),
                estimacion.getCreateEn());
    }

    public CostosFinalResponseDTO obtenerCostoFinalPorRuta(String rutaRef) {
        CostoFinal costoFinal = costoFinalRepository.findByRutaRef(rutaRef)
                .orElseThrow(() -> new RuntimeException("Costo final no encontrado para ruta: " + rutaRef));

        return new CostosFinalResponseDTO(
                costoFinal.getId(),
                costoFinal.getRutaRef(),
                costoFinal.getLitros(),
                costoFinal.getEstadiasDias(),
                costoFinal.getTotalFinal(),
                costoFinal.getMoneda(),
                parseJsonToMap(costoFinal.getDesposeJson()),
                costoFinal.getCreateEn());
    }

    // ========== MÉTODOS ADICIONALES DE CONSULTA ==========

    public List<CostosEstimacionResponseDTO> obtenerEstimacionesRecientes() {
        LocalDateTime haceUnaSemana = LocalDateTime.now().minusDays(7);

        return costoEstimacionRepository.findEstimacionesRecientes(haceUnaSemana).stream()
                .map(this::convertToEstimacionResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CostosFinalResponseDTO> obtenerCostosFinalesRecientes() {
        LocalDateTime haceUnaSemana = LocalDateTime.now().minusDays(7);

        return costoFinalRepository.findCostosFinalesRecientes(haceUnaSemana).stream()
                .map(this::convertToCostoFinalResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> obtenerEstadisticasCostos() {
        Object[] estadisticas = costoFinalRepository.findEstadisticasCostos();

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalCostos", estadisticas[0]);
        resultado.put("sumaTotal", estadisticas[1]);
        resultado.put("promedio", estadisticas[2]);
        resultado.put("minimo", estadisticas[3]);
        resultado.put("maximo", estadisticas[4]);

        return resultado;
    }

    public BigDecimal obtenerPromedioEstimaciones() {
        return costoEstimacionRepository.findPromedioTotalEstimado()
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal obtenerPromedioCostosFinales() {
        return costoFinalRepository.findPromedioTotalFinal()
                .orElse(BigDecimal.ZERO);
    }

    // ========== MÉTODOS PRIVADOS DE CÁLCULO (igual que antes) ==========

    private BigDecimal calcularCostoSegmento(SegmentoCostoDTO segmento) {
        // Implementación igual a la anterior...
        BigDecimal costoSegmento = BigDecimal.ZERO;

        // Obtener tarifas aplicables al camión
        List<Tarifa> tarifasCamion = obtenerTarifasParaCamion(segmento.camionId());

        // Calcular costo por distancia
        costoSegmento = costoSegmento.add(calcularCostoPorDistancia(segmento.distanciaKm(), tarifasCamion));

        // Calcular costo por tiempo
        costoSegmento = costoSegmento.add(calcularCostoPorTiempo(segmento.duracionEstimadaMin(), tarifasCamion));

        // Calcular costo operación camión
        Optional<Camion> camionOpt = camionRepository.findById(segmento.camionId());
        if (camionOpt.isPresent()) {
            BigDecimal horas = BigDecimal.valueOf(segmento.duracionEstimadaMin())
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
            BigDecimal costoOperacion = camionOpt.get().getCostoOperacionHora().multiply(horas);
            costoSegmento = costoSegmento.add(costoOperacion);
        }

        return costoSegmento;
    }

    private BigDecimal calcularCostoCombustibleExtra(BigDecimal litrosReales, CostoEstimacion estimacion) {
        // Simulación: asumimos 8 km/litro y $150 por litro
        BigDecimal litrosEstimados = new BigDecimal("100.00"); // Valor estimado por defecto
        BigDecimal precioLitro = new BigDecimal("150.00");

        BigDecimal diferenciaLitros = litrosReales.subtract(litrosEstimados);
        return diferenciaLitros.multiply(precioLitro).max(BigDecimal.ZERO);
    }

private List<Tarifa> obtenerTarifasParaCamion(UUID camionId) {
    return tarifaCamionRepository.findTarifasVigentesByCamion(camionId).stream()
            .map(tc -> tarifaRepository.findById(tc.getTarifaId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(this::esTarifaVigente)
            .collect(Collectors.toList());
}

    private BigDecimal calcularCostoPorDistancia(BigDecimal distancia, List<Tarifa> tarifas) {
        return tarifas.stream()
                .filter(t -> t.getUnidad() == UnidadTarifa.POR_KILOMETRO)
                .findFirst()
                .map(tarifa -> tarifa.getValor().multiply(distancia))
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calcularCostoPorTiempo(Integer duracionMin, List<Tarifa> tarifas) {
        // Convertir Integer a BigDecimal
        BigDecimal duracionMinBigDecimal = BigDecimal.valueOf(duracionMin);

        // CORREGIDO: Usar RoundingMode en lugar de la constante deprecada
        BigDecimal horas = duracionMinBigDecimal.divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        return tarifas.stream()
                .filter(t -> t.getUnidad() == UnidadTarifa.POR_HORA)
                .findFirst()
                .map(tarifa -> tarifa.getValor().multiply(horas))
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calcularCostosAdicionales(Map<String, Object> costosAdicionales) {
        return costosAdicionales.values().stream()
                .filter(value -> value instanceof BigDecimal)
                .map(value -> (BigDecimal) value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularCostoDesdeCero(CostosFinalRequestDTO request) {
        // Cálculo simplificado desde cero
        BigDecimal costoBase = new BigDecimal("1000.00");
        BigDecimal costoCombustible = request.litrosConsumidos().multiply(new BigDecimal("150.00"));
        BigDecimal costoDiasExtra = BigDecimal.valueOf(request.estadiasDias()).multiply(new BigDecimal("200.00"));

        return costoBase.add(costoCombustible).add(costoDiasExtra);
    }

    private BigDecimal calcularCostoDiasExtra(Integer diasExtra) {
        // Costo por día extra (depósito + otros)
        BigDecimal costoDiarioDeposito = new BigDecimal("200.00");
        return costoDiarioDeposito.multiply(BigDecimal.valueOf(diasExtra));
    }

    private BigDecimal calcularCostosFijos(CostosEstimacionRequestDTO request) {
        // Costos fijos como seguros, mantenimiento, etc.
        return new BigDecimal("500.00"); // Valor fijo por ahora
    }
        private boolean esTarifaVigente(Tarifa tarifa) {
        return tarifa.getVigenciaHasta().isAfter(LocalDateTime.now().toLocalDate()) 
            || tarifa.getVigenciaHasta().isEqual(LocalDateTime.now().toLocalDate());
    }

    // ... Los demás métodos de cálculo (calcularCostoPorDistancia,
    // calcularCostoPorTiempo, etc.)
    // se mantienen igual que en la implementación anterior

    // ========== MÉTODOS DE CONVERSIÓN ==========

    private CostosEstimacionResponseDTO convertToEstimacionResponseDTO(CostoEstimacion estimacion) {
        return new CostosEstimacionResponseDTO(
                estimacion.getId(),
                UUID.fromString(estimacion.getSolicitudRef()),
                extractRutaRefFromJson(estimacion.getRutaPayipro()),
                estimacion.getTotalEstimado(),
                estimacion.getMoneda(),
                parseJsonToMap(estimacion.getDespose()),
                estimacion.getCreateEn());
    }

    private CostosFinalResponseDTO convertToCostoFinalResponseDTO(CostoFinal costoFinal) {
        return new CostosFinalResponseDTO(
                costoFinal.getId(),
                costoFinal.getRutaRef(),
                costoFinal.getLitros(),
                costoFinal.getEstadiasDias(),
                costoFinal.getTotalFinal(),
                costoFinal.getMoneda(),
                parseJsonToMap(costoFinal.getDesposeJson()),
                costoFinal.getCreateEn());
    }

    // ========== MÉTODOS DE UTILIDAD PARA JSON ==========

    private String convertToJson(Object object) {
        // Simulación - en producción usar Jackson
        try {
            return "{\"simulated\": true, \"data\": \"json_placeholder\"}";
        } catch (Exception e) {
            return "{}";
        }
    }

    private Map<String, Object> parseJsonToMap(String json) {
        // Simulación - en producción usar Jackson
        Map<String, Object> map = new HashMap<>();
        map.put("simulated", true);
        map.put("original_json", json);
        return map;
    }

    private String extractRutaRefFromJson(String json) {
        // Simulación
        return "ruta_extracted_from_json";
    }
}