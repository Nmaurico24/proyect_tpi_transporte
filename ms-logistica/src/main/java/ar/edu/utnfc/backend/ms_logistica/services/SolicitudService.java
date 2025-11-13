package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.model.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;
import ar.edu.utnfc.backend.ms_logistica.model.SolicitudEvento;
import ar.edu.utnfc.backend.ms_logistica.repository.SolicitudRepository;
import ar.edu.utnfc.backend.ms_logistica.repository.ClienteRepository;
import ar.edu.utnfc.backend.ms_logistica.repository.SolicitudEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private SolicitudEventoRepository solicitudEventoRepository;

    public SolicitudDTO crearSolicitud(SolicitudCreateDTO solicitudCreateDTO) {
        // Validar que el cliente existe
        if (!clienteRepository.existsById(solicitudCreateDTO.clienteId())) {
            throw new RuntimeException("Cliente no encontrado con ID: " + solicitudCreateDTO.clienteId());
        }
        
        Solicitud solicitud = Solicitud.builder()
            .clienteId(solicitudCreateDTO.clienteId())
            .estado(solicitudCreateDTO.estado() != null ? solicitudCreateDTO.estado() : EstadoSolicitud.BORRADOR)
            .prioridad(solicitudCreateDTO.prioridad() != null ? solicitudCreateDTO.prioridad() : 3)
            .origenDireccion(solicitudCreateDTO.origenDireccion())
            .origenLat(solicitudCreateDTO.origenLat())
            .origenLng(solicitudCreateDTO.origenLng())
            .destinoDireccion(solicitudCreateDTO.destinoDireccion())
            .destinoLat(solicitudCreateDTO.destinoLat())
            .destinoLng(solicitudCreateDTO.destinoLng())
            .costoEstimado(solicitudCreateDTO.costoEstimado())
            .costoFinal(solicitudCreateDTO.costoFinal())
            .distanciaEstimadaKm(solicitudCreateDTO.distanciaEstimadaKm())
            .duracionEstimadaMin(solicitudCreateDTO.duracionEstimadaMin())
            .rutaRef(solicitudCreateDTO.rutaRef())
            .build();
        
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
        
        // Crear evento de creación
        crearEventoSolicitud(solicitudGuardada.getId(), 
                           solicitudGuardada.getEstado().toString(), 
                           "Solicitud creada", 
                           "SISTEMA");
        
        return convertToSolicitudDTO(solicitudGuardada);
    }

    public SolicitudDTO actualizarSolicitud(UUID id, SolicitudUpdateDTO solicitudUpdateDTO) {
        Solicitud solicitudExistente = solicitudRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        
        EstadoSolicitud estadoAnterior = solicitudExistente.getEstado();
        
        // Actualizar campos si se proporcionan
        if (solicitudUpdateDTO.estado() != null) {
            solicitudExistente.setEstado(solicitudUpdateDTO.estado());
        }
        if (solicitudUpdateDTO.prioridad() != null) {
            solicitudExistente.setPrioridad(solicitudUpdateDTO.prioridad());
        }
        if (solicitudUpdateDTO.costoFinal() != null) {
            solicitudExistente.setCostoFinal(solicitudUpdateDTO.costoFinal());
        }
        if (solicitudUpdateDTO.duracionRealMin() != null) {
            solicitudExistente.setDuracionRealMin(solicitudUpdateDTO.duracionRealMin());
        }
        if (solicitudUpdateDTO.rutaRef() != null) {
            solicitudExistente.setRutaRef(solicitudUpdateDTO.rutaRef());
        }
        
        Solicitud solicitudActualizada = solicitudRepository.save(solicitudExistente);
        
        // Crear evento si cambió el estado
        if (solicitudUpdateDTO.estado() != null && !solicitudUpdateDTO.estado().equals(estadoAnterior)) {
            crearEventoSolicitud(id, 
                               solicitudUpdateDTO.estado().toString(), 
                               "Estado actualizado: " + estadoAnterior + " → " + solicitudUpdateDTO.estado(), 
                               "SISTEMA");
        }
        
        return convertToSolicitudDTO(solicitudActualizada);
    }

    public SolicitudDTO cambiarEstadoSolicitud(UUID id, CambioEstadoSolicitudDTO cambioEstadoDTO) {
        Solicitud solicitud = solicitudRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        
        EstadoSolicitud estadoAnterior = solicitud.getEstado();
        solicitud.setEstado(cambioEstadoDTO.estado());
        
        Solicitud solicitudActualizada = solicitudRepository.save(solicitud);
        
        // Crear evento del cambio de estado
        crearEventoSolicitud(id, 
                           cambioEstadoDTO.estado().toString(), 
                           cambioEstadoDTO.detalle() != null ? cambioEstadoDTO.detalle() : 
                               "Estado cambiado: " + estadoAnterior + " → " + cambioEstadoDTO.estado(), 
                           cambioEstadoDTO.actor() != null ? cambioEstadoDTO.actor() : "SISTEMA");
        
        return convertToSolicitudDTO(solicitudActualizada);
    }

    public SolicitudDTO obtenerSolicitudPorId(UUID id) {
        Solicitud solicitud = solicitudRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        
        return convertToSolicitudDTO(solicitud);
    }

    public Page<SolicitudListDTO> obtenerPagina(Pageable pageable) {
        Page<Solicitud> solicitudesPage = solicitudRepository.findAll(pageable);
        return solicitudesPage.map(this::convertToSolicitudListDTO);
    }

    // ========== MÉTODOS ADICIONALES DE CONSULTA ==========

    public List<SolicitudListDTO> obtenerSolicitudesPorCliente(UUID clienteId) {
        return solicitudRepository.findByClienteId(clienteId).stream()
            .map(this::convertToSolicitudListDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudListDTO> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado).stream()
            .map(this::convertToSolicitudListDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudListDTO> obtenerSolicitudesActivas() {
        return solicitudRepository.findSolicitudesActivas().stream()
            .map(this::convertToSolicitudListDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudListDTO> obtenerSolicitudesRecientes() {
        LocalDateTime haceUnaSemana = LocalDateTime.now().minusDays(7);
        return solicitudRepository.findSolicitudesRecientes(haceUnaSemana).stream()
            .map(this::convertToSolicitudListDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudListDTO> buscarSolicitudesConFiltros(BusquedaSolicitudesDTO filtros) {
        List<Solicitud> solicitudes = solicitudRepository.findSolicitudesConFiltros(
            filtros.clienteId(),
            filtros.estado(),
            filtros.prioridadMin(),
            filtros.prioridadMax(),
            filtros.fechaDesde(),
            filtros.fechaHasta(),
            filtros.rutaRef()
        );
        
        return solicitudes.stream()
            .map(this::convertToSolicitudListDTO)
            .collect(Collectors.toList());
    }

    public DashboardSolicitudesDTO obtenerEstadisticasDashboard() {
        Object[] estadisticas = solicitudRepository.findEstadisticasDashboard();
        
        return new DashboardSolicitudesDTO(
            ((Number) estadisticas[0]).longValue(),
            ((Number) estadisticas[1]).longValue(),
            ((Number) estadisticas[2]).longValue(),
            ((Number) estadisticas[3]).longValue(),
            (BigDecimal) estadisticas[4],
            (BigDecimal) estadisticas[5]
        );
    }

    public Map<String, Object> obtenerEstadisticasPorEstado() {
        List<Object[]> resultados = solicitudRepository.findEstadisticasPorEstado();
        
        Map<String, Object> estadisticas = new HashMap<>();
        for (Object[] resultado : resultados) {
            String estado = (String) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            BigDecimal costoPromedio = (BigDecimal) resultado[2];
            BigDecimal costoTotal = (BigDecimal) resultado[3];
            
            Map<String, Object> datosEstado = new HashMap<>();
            datosEstado.put("cantidad", cantidad);
            datosEstado.put("costoPromedio", costoPromedio);
            datosEstado.put("costoTotal", costoTotal);
            
            estadisticas.put(estado, datosEstado);
        }
        
        return estadisticas;
    }

    // ========== MÉTODOS PRIVADOS ==========

    private void crearEventoSolicitud(UUID solicitudId, String estado, String detalle, String actor) {
        SolicitudEvento evento = SolicitudEvento.builder()
            .solicitudId(solicitudId)
            .estado(estado)
            .detalle(detalle)
            .actor(actor)
            .build();
        
        solicitudEventoRepository.save(evento);
    }

    private SolicitudDTO convertToSolicitudDTO(Solicitud solicitud) {
        return new SolicitudDTO(
            solicitud.getId(),
            solicitud.getClienteId(),
            solicitud.getEstado(),
            solicitud.getPrioridad(),
            solicitud.getOrigenDireccion(),
            solicitud.getOrigenLat(),
            solicitud.getOrigenLng(),
            solicitud.getDestinoDireccion(),
            solicitud.getDestinoLat(),
            solicitud.getDestinoLng(),
            solicitud.getCostoEstimado(),
            solicitud.getCostoFinal(),
            solicitud.getDistanciaEstimadaKm(),
            solicitud.getDuracionEstimadaMin(),
            solicitud.getDuracionRealMin(),
            solicitud.getRutaRef(),
            solicitud.getCreatedAt(),
            solicitud.getUpdatedAt()
        );
    }

    private SolicitudListDTO convertToSolicitudListDTO(Solicitud solicitud) {
        // Aquí podrías obtener el nombre del cliente si lo necesitas
        String clienteNombre = "Cliente"; // Podrías hacer una consulta para obtener el nombre real
        
        return new SolicitudListDTO(
            solicitud.getId(),
            solicitud.getClienteId(),
            clienteNombre,
            solicitud.getEstado(),
            solicitud.getPrioridad(),
            solicitud.getOrigenDireccion(),
            solicitud.getDestinoDireccion(),
            solicitud.getCostoEstimado(),
            solicitud.getRutaRef(),
            solicitud.getCreatedAt()
        );
    }
}