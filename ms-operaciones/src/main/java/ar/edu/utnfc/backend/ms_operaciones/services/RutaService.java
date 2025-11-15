package ar.edu.utnfc.backend.ms_operaciones.services;

import ar.edu.utnfc.backend.ms_operaciones.clients.GoogleDistanceClient;
import ar.edu.utnfc.backend.ms_operaciones.clients.dto.GoogleDistanceMatrixResponse;
import ar.edu.utnfc.backend.ms_operaciones.clients.dto.GoogleDistanceMatrixResponse.Element;
import ar.edu.utnfc.backend.ms_operaciones.clients.dto.GoogleDistanceMatrixResponse.Row;
import ar.edu.utnfc.backend.ms_operaciones.dto.rutas.ConfirmarRutaRequest;
import ar.edu.utnfc.backend.ms_operaciones.dto.rutas.RutaResponse;
import ar.edu.utnfc.backend.ms_operaciones.dto.rutas.RutaTentativaRequest;
import ar.edu.utnfc.backend.ms_operaciones.exceptions.NotFoundException;
import ar.edu.utnfc.backend.ms_operaciones.models.Ruta;
import ar.edu.utnfc.backend.ms_operaciones.models.enums.EstadoRuta;
import ar.edu.utnfc.backend.ms_operaciones.repositories.RutaRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RutaService {

  private static final BigDecimal MIL = BigDecimal.valueOf(1000);

  private final RutaRepository rutaRepository;
  private final GoogleDistanceClient distanceClient;

  public Ruta findOrThrow(UUID id) {
    return rutaRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Ruta no encontrada: " + id));
  }

  @Transactional
  public RutaResponse generarTentativa(RutaTentativaRequest req) {
    MetricData metricas = resolverMetricas(req.getOrigen(), req.getDestino());

    Ruta ruta = Ruta.builder()
        .solicitudRef(StringUtils.hasText(req.getSolicitudRef()) ? req.getSolicitudRef() : null)
        .estado(EstadoRuta.TENTATIVA)
        .distanciaTotalKm(metricas.distanciaKm())
        .duracionEstimadaMin(metricas.duracionMin())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();

    ruta = rutaRepository.save(ruta);
    return toResponse(ruta);
  }

  @Transactional
  public RutaResponse confirmar(UUID rutaId, ConfirmarRutaRequest req) {
    Ruta ruta = findOrThrow(rutaId);
    ruta.setEstado(EstadoRuta.CONFIRMADA);
    if (StringUtils.hasText(req.getSolicitudRef())) {
      ruta.setSolicitudRef(req.getSolicitudRef());
    }
    ruta.setUpdatedAt(Instant.now());
    ruta = rutaRepository.save(ruta);
    return toResponse(ruta);
  }

  private RutaResponse toResponse(Ruta ruta) {
    return RutaResponse.builder()
        .id(ruta.getId())
        .estado(ruta.getEstado() != null ? ruta.getEstado().name() : null)
        .distanciaKm(ruta.getDistanciaTotalKm())
        .duracionMin(ruta.getDuracionEstimadaMin())
        .build();
  }

  private MetricData resolverMetricas(String origen, String destino) {
    double[] origenCoord = parseLatLng(origen);
    double[] destinoCoord = parseLatLng(destino);

    if (origenCoord == null || destinoCoord == null) {
      return MetricData.empty();
    }

    try {
      GoogleDistanceMatrixResponse response = distanceClient.calcularDistancia(
          origenCoord[0], origenCoord[1], destinoCoord[0], destinoCoord[1]);
      return extraerMetricas(response);
    } catch (Exception ex) {
      log.warn("No se pudo obtener distancia desde Google: {}", ex.getMessage());
      return MetricData.empty();
    }
  }

  private MetricData extraerMetricas(GoogleDistanceMatrixResponse response) {
    if (response == null) {
      return MetricData.empty();
    }

    List<Row> rows = response.getRows();
    if (rows == null || rows.isEmpty()) {
      return MetricData.empty();
    }

    List<Element> elements = rows.get(0).getElements();
    if (elements == null || elements.isEmpty()) {
      return MetricData.empty();
    }

    Element element = elements.get(0);
    if (element == null || !"OK".equalsIgnoreCase(element.getStatus())) {
      return MetricData.empty();
    }

    long distanciaMetros = element.getDistance() != null ? element.getDistance().getValue() : 0;
    long duracionSegundos = element.getDuration() != null ? element.getDuration().getValue() : 0;

    BigDecimal distanciaKm = BigDecimal.valueOf(distanciaMetros)
        .divide(MIL, 2, RoundingMode.HALF_UP);
    int duracionMin = (int) Math.round(duracionSegundos / 60.0);
    return new MetricData(distanciaKm, duracionMin);
  }

  private double[] parseLatLng(String raw) {
    if (!StringUtils.hasText(raw) || !raw.contains(",")) {
      log.debug("Coordenada inválida: {}", raw);
      return null;
    }
    String[] parts = raw.split(",");
    if (parts.length != 2) {
      log.debug("Coordenada inválida: {}", raw);
      return null;
    }
    try {
      double lat = Double.parseDouble(parts[0].trim());
      double lng = Double.parseDouble(parts[1].trim());
      return new double[]{lat, lng};
    } catch (NumberFormatException ex) {
      log.debug("No se pudo parsear coordenada {}: {}", raw, ex.getMessage());
      return null;
    }
  }

  private record MetricData(BigDecimal distanciaKm, Integer duracionMin) {

    private static MetricData empty() {
      return new MetricData(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), 0);
    }
  }
}
