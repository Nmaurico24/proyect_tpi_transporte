package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.model.SolicitudEvento;
import ar.edu.utnfc.backend.ms_logistica.model.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;
import ar.edu.utnfc.backend.ms_logistica.repository.SolicitudEventoRepository;
import ar.edu.utnfc.backend.ms_logistica.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudEventoService {

    @Autowired
    private SolicitudEventoRepository solicitudEventoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    public SolicitudEventoDTO crearEvento(SolicitudEventoCreateDTO eventoCreateDTO) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(eventoCreateDTO.solicitudId())) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + eventoCreateDTO.solicitudId());
        }
        
        SolicitudEvento evento = SolicitudEvento.builder()
            .solicitudId(eventoCreateDTO.solicitudId())
            .estado(eventoCreateDTO.estado())
            .detalle(eventoCreateDTO.detalle())
            .actor(eventoCreateDTO.actor() != null ? eventoCreateDTO.actor() : "SISTEMA")
            .build();
        
        SolicitudEvento eventoGuardado = solicitudEventoRepository.save(evento);
        
        return convertToSolicitudEventoDTO(eventoGuardado);
    }

    public SolicitudEventoDTO crearEventoAutomatico(UUID solicitudId, String estado, String detalle) {
        return crearEvento(new SolicitudEventoCreateDTO(
            solicitudId, estado, detalle, "SISTEMA"
        ));
    }

    public List<SolicitudEventoDTO> obtenerEventosPorSolicitud(UUID solicitudId) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + solicitudId);
        }
        
        return solicitudEventoRepository.findBySolicitudIdOrderByCreatedAtDesc(solicitudId).stream()
            .map(this::convertToSolicitudEventoDTO)
            .collect(Collectors.toList());
    }

    public HistorialSolicitudDTO obtenerHistorialCompleto(UUID solicitudId) {
        // Validar que la solicitud existe
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));
        
        List<SolicitudEvento> eventos = solicitudEventoRepository.findHistorialCompletoBySolicitudId(solicitudId);
        List<SolicitudEventoDTO> eventosDTO = eventos.stream()
            .map(this::convertToSolicitudEventoDTO)
            .collect(Collectors.toList());
        
        return new HistorialSolicitudDTO(solicitudId, eventosDTO);
    }

    public LineaTiempoDTO obtenerLineaTiempo(UUID solicitudId) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + solicitudId);
        }
        
        List<SolicitudEvento> eventos = solicitudEventoRepository.findLineaTiempoBySolicitudId(solicitudId);
        List<EventoLineaTiempoDTO> eventosLinea = eventos.stream()
            .map(this::convertToEventoLineaTiempoDTO)
            .collect(Collectors.toList());
        
        return new LineaTiempoDTO(solicitudId, eventosLinea);
    }

    public Optional<SolicitudEventoDTO> obtenerUltimoEvento(UUID solicitudId) {
        return solicitudEventoRepository.findUltimoEventoBySolicitudId(solicitudId)
            .map(this::convertToSolicitudEventoDTO);
    }

    public List<SolicitudEventoDTO> obtenerEventosRecientes() {
        LocalDateTime haceUnaSemana = LocalDateTime.now().minusDays(7);
        return solicitudEventoRepository.findEventosRecientes(haceUnaSemana).stream()
            .map(this::convertToSolicitudEventoDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudEventoDTO> obtenerEventosPorEstado(String estado) {
        return solicitudEventoRepository.findByEstado(estado).stream()
            .map(this::convertToSolicitudEventoDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudEventoDTO> obtenerEventosPorActor(String actor) {
        return solicitudEventoRepository.findByActor(actor).stream()
            .map(this::convertToSolicitudEventoDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudEventoDTO> obtenerEventosPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        return solicitudEventoRepository.findByCreatedAtBetween(desde, hasta).stream()
            .map(this::convertToSolicitudEventoDTO)
            .collect(Collectors.toList());
    }

    // ========== MÉTODOS DE ANÁLISIS Y ESTADÍSTICAS ==========

    public Map<String, Object> obtenerEstadisticasGenerales() {
        long totalEventos = solicitudEventoRepository.count();
        long totalSolicitudes = solicitudRepository.count();
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalEventos", totalEventos);
        estadisticas.put("totalSolicitudes", totalSolicitudes);
        estadisticas.put("promedioEventosPorSolicitud", 
            totalSolicitudes > 0 ? (double) totalEventos / totalSolicitudes : 0);
        
        return estadisticas;
    }

    public Map<String, Long> obtenerEstadisticasPorActor() {
        List<Object[]> resultados = solicitudEventoRepository.findEstadisticasPorActor();
        
        Map<String, Long> estadisticas = new HashMap<>();
        for (Object[] resultado : resultados) {
            String actor = (String) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            estadisticas.put(actor, cantidad);
        }
        
        return estadisticas;
    }

    public Map<String, Long> obtenerEstadisticasPorEstado() {
        List<SolicitudEvento> todosEventos = solicitudEventoRepository.findAll();
        
        return todosEventos.stream()
            .collect(Collectors.groupingBy(
                SolicitudEvento::getEstado,
                Collectors.counting()
            ));
    }

    public ResumenActividadDTO obtenerResumenActividadReciente() {
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        List<SolicitudEvento> eventosRecientes = solicitudEventoRepository.findEventosRecientes(hace24Horas);
        
        long totalEventos = eventosRecientes.size();
        long eventosSistema = eventosRecientes.stream()
            .filter(e -> "SISTEMA".equals(e.getActor()))
            .count();
        long eventosUsuario = totalEventos - eventosSistema;
        
        // Obtener los estados más comunes
        Map<String, Long> estadosFrecuentes = eventosRecientes.stream()
            .collect(Collectors.groupingBy(
                SolicitudEvento::getEstado,
                Collectors.counting()
            ));
        
        String estadoMasFrecuente = estadosFrecuentes.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
        
        return new ResumenActividadDTO(
            totalEventos,
            eventosSistema,
            eventosUsuario,
            estadoMasFrecuente,
            eventosRecientes.stream()
                .map(this::convertToSolicitudEventoDTO)
                .limit(10)
                .collect(Collectors.toList())
        );
    }

    // ========== MÉTODOS DE BÚSQUEDA AVANZADA ==========

    public List<SolicitudEventoDTO> buscarEventosConFiltros(
            UUID solicitudId, String estado, String actor, 
            LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        
        List<SolicitudEvento> eventos;
        
        if (solicitudId != null) {
            eventos = solicitudEventoRepository.findBySolicitudId(solicitudId);
        } else {
            eventos = solicitudEventoRepository.findAll();
        }
        
        // Aplicar filtros
        return eventos.stream()
            .filter(e -> estado == null || e.getEstado().equals(estado))
            .filter(e -> actor == null || e.getActor().equals(actor))
            .filter(e -> fechaDesde == null || e.getCreatedAt().isAfter(fechaDesde))
            .filter(e -> fechaHasta == null || e.getCreatedAt().isBefore(fechaHasta))
            .map(this::convertToSolicitudEventoDTO)
            .collect(Collectors.toList());
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    private SolicitudEventoDTO convertToSolicitudEventoDTO(SolicitudEvento evento) {
        return new SolicitudEventoDTO(
            evento.getId(),
            evento.getSolicitudId(),
            evento.getEstado(),
            evento.getDetalle(),
            evento.getActor(),
            evento.getCreatedAt()
        );
    }

    private EventoLineaTiempoDTO convertToEventoLineaTiempoDTO(SolicitudEvento evento) {
        return new EventoLineaTiempoDTO(
            evento.getId(),
            evento.getEstado(),
            evento.getDetalle(),
            evento.getActor(),
            evento.getCreatedAt()
        );
    }
}