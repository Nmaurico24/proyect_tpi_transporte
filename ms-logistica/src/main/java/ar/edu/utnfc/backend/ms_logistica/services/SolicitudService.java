package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.exceptions.NotFoundException;
import ar.edu.utnfc.backend.ms_logistica.models.*;
import ar.edu.utnfc.backend.ms_logistica.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final SolicitudContenedorRepository solicitudContenedorRepository;
    private final EstimadaRepository estimadaRepository;
    private final SolicitudEventoRepository solicitudEventoRepository;

    public SolicitudService(SolicitudRepository solicitudRepository,
            ClienteRepository clienteRepository,
            ContenedorRepository contenedorRepository,
            SolicitudContenedorRepository solicitudContenedorRepository,
            EstimadaRepository estimadaRepository,
            SolicitudEventoRepository solicitudEventoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
        this.contenedorRepository = contenedorRepository;
        this.solicitudContenedorRepository = solicitudContenedorRepository;
        this.estimadaRepository = estimadaRepository;
        this.solicitudEventoRepository = solicitudEventoRepository;
    }

    public SolicitudDetailResponse crearSolicitud(SolicitudCreateRequest req) {
        Cliente cliente = clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        Solicitud solicitud = Solicitud.builder()
                .cliente(cliente)
                .estado(EstadoSolicitud.BORRADOR)
                .prioridad(req.prioridad())
                .origenDireccion(req.origenDireccion())
                .destinoDireccion(req.destinoDireccion())
                .moneda(req.moneda())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        solicitud = solicitudRepository.save(solicitud);

        if (req.contenedoresIds() != null) {
            for (UUID contId : req.contenedoresIds()) {
                Contenedor contenedor = contenedorRepository.findById(contId)
                        .orElseThrow(() -> new NotFoundException("Contenedor no encontrado"));
                SolicitudContenedor sc = SolicitudContenedor.builder()
                        .solicitud(solicitud)
                        .contenedor(contenedor)
                        .build();
                solicitudContenedorRepository.save(sc);
            }
        }

        registrarEvento(solicitud, "CREADA", "Solicitud creada", "OPERADOR");
        return toDetailResponse(solicitud);
    }

    public SolicitudDetailResponse obtenerSolicitud(UUID id) {
        Solicitud sol = solicitudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        return toDetailResponse(sol);
    }

    public Page<SolicitudSummaryResponse> listarSolicitudes(EstadoSolicitud estado,
            UUID clienteId,
            Pageable pageable) {
        Page<Solicitud> page;
        if (estado != null && clienteId != null) {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
            page = solicitudRepository.findByEstadoAndCliente(estado, cliente, pageable);
        } else if (estado != null) {
            page = solicitudRepository.findByEstado(estado, pageable);
        } else if (clienteId != null) {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
            page = solicitudRepository.findByCliente(cliente, pageable);
        } else {
            page = solicitudRepository.findAll(pageable);
        }

        return page.map(this::toSummaryResponse);
    }

    public void cancelarSolicitud(UUID id, String actor) {
        Solicitud sol = solicitudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        sol.setEstado(EstadoSolicitud.CANCELADA);
        sol.setUpdatedAt(Instant.now());
        solicitudRepository.save(sol);
        registrarEvento(sol, "CANCELADA", "Solicitud cancelada", actor);
    }

    public EstimacionResponse registrarEstimacion(UUID solicitudId, EstimacionCreateRequest req) {
        Solicitud sol = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        Estimada est = Estimada.builder()
                .solicitud(sol)
                .fuente(req.fuente())
                .payloadJson(req.payloadJson())
                .distanciaKm(req.distanciaKm())
                .duracionMin(req.duracionMin())
                .costoTotal(req.costoTotal())
                .moneda(req.moneda())
                .createdAt(Instant.now())
                .build();

        est = estimadaRepository.save(est);

        // Actualizamos resumen estimado en la solicitud
        sol.setPrecioEstimado(req.costoTotal());
        sol.setDistanciaEstimadaKm(req.distanciaKm());
        sol.setDuracionEstimadaMin(req.duracionMin());
        sol.setUpdatedAt(Instant.now());
        solicitudRepository.save(sol);

        registrarEvento(sol, "ESTIMADA", "Se registró una estimación de costo", "OPERADOR");

        return new EstimacionResponse(
                est.getId(),
                est.getDistanciaKm(),
                est.getDuracionMin(),
                est.getCostoTotal(),
                est.getMoneda(),
                est.getFuente());
    }

    public void confirmarSolicitud(UUID solicitudId, String rutaRef, String actor) {
        Solicitud sol = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        sol.setEstado(EstadoSolicitud.PROGRAMADA);
        sol.setRutaRef(rutaRef);
        sol.setUpdatedAt(Instant.now());
        solicitudRepository.save(sol);

        registrarEvento(sol, "PROGRAMADA", "Solicitud confirmada y programada", actor);
    }

    // Helpers de mapeo

    private SolicitudSummaryResponse toSummaryResponse(Solicitud s) {
        return new SolicitudSummaryResponse(
                s.getId(),
                s.getCliente().getId(),
                s.getEstado(),
                s.getOrigenDireccion(),
                s.getDestinoDireccion(),
                s.getPrecioEstimado(),
                s.getMoneda());
    }

    private SolicitudDetailResponse toDetailResponse(Solicitud s) {
        List<SolicitudContenedor> links = solicitudContenedorRepository.findBySolicitud(s);
        List<UUID> contIds = links.stream()
                .map(sc -> sc.getContenedor().getId())
                .toList();

        return new SolicitudDetailResponse(
                s.getId(),
                s.getCliente().getId(),
                s.getEstado(),
                s.getPrioridad(),
                s.getOrigenDireccion(),
                s.getDestinoDireccion(),
                s.getPrecioEstimado(),
                s.getPrecioFinal(),
                s.getDistanciaEstimadaKm(),
                s.getDistanciaRealKm(),
                s.getDuracionEstimadaMin(),
                s.getDuracionRealMin(),
                s.getMoneda(),
                s.getRutaRef(),
                contIds,
                s.getCreatedAt());
    }

    private void registrarEvento(Solicitud s, String estado, String detalle, String actor) {
        SolicitudEvento ev = SolicitudEvento.builder()
                .solicitud(s)
                .estado(estado)
                .detalle(detalle)
                .actor(actor)
                .createdAt(Instant.now())
                .build();
        solicitudEventoRepository.save(ev);
    }
}
