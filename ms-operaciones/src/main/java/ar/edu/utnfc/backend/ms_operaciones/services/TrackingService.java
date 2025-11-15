package ar.edu.utnfc.backend.ms_operaciones.services;

import ar.edu.utnfc.backend.ms_operaciones.dto.tracking.*;
import ar.edu.utnfc.backend.ms_operaciones.models.TimelineSolicitud;
import ar.edu.utnfc.backend.ms_operaciones.models.TrackingEvento;
import ar.edu.utnfc.backend.ms_operaciones.models.Tramo;
import ar.edu.utnfc.backend.ms_operaciones.repositories.TimelineSolicitudRepository;
import ar.edu.utnfc.backend.ms_operaciones.repositories.TrackingEventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackingService {
    private final TramoService tramoService;
    private final TrackingEventoRepository trackingRepo;
    private final TimelineSolicitudRepository timelineRepo;

    @Transactional
    public void registrarEvento(UUID tramoId, TrackingEventoRequest req, String actor) {
        Tramo t = tramoService.get(tramoId);
        trackingRepo.save(TrackingEvento.builder()
                .tramo(t)
                .tipo(req.getTipo())
                .actor(actor)
                .lat(req.getLat())
                .lng(req.getLng())
                .descripcion(req.getDescripcion())
                .registradoEn(Instant.now())
                .build());

        timelineRepo.save(
                timelineRepo.findBySolicitudRef(t.getRuta().getSolicitudRef())
                        .orElse(TimelineSolicitud.builder().solicitudRef(t.getRuta().getSolicitudRef()).build())
                        .toBuilder()
                        .rutaId(t.getRuta().getId().toString())
                        .ultimoEstado(t.getRuta().getEstado().name())
                        .ultimoEvento(req.getTipo())
                        .actualizadoEn(Instant.now())
                        .build());
    }

    public TrackingLineaTiempoResponse obtenerLineaTiempo(String solicitudRef) {
        var tl = timelineRepo.findBySolicitudRef(solicitudRef)
                .orElse(TimelineSolicitud.builder()
                        .solicitudRef(solicitudRef)
                        .ultimoEstado("DESCONOCIDO")
                        .ultimoEvento("SIN_EVENTOS")
                        .actualizadoEn(Instant.now()).build());
        return TrackingLineaTiempoResponse.builder()
                .solicitudRef(tl.getSolicitudRef())
                .ultimoEstado(tl.getUltimoEstado())
                .ultimoEvento(tl.getUltimoEvento())
                .actualizadoEn(tl.getActualizadoEn())
                .build();
    }
}
