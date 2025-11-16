package ar.edu.utnfc.backend.ms_operaciones.services;

import ar.edu.utnfc.backend.ms_operaciones.dto.tramos.AsignarCamionRequest;
import ar.edu.utnfc.backend.ms_operaciones.dto.tramos.TramoResponse;
import ar.edu.utnfc.backend.ms_operaciones.models.Asignacion;
import ar.edu.utnfc.backend.ms_operaciones.models.Tramo;
import ar.edu.utnfc.backend.ms_operaciones.models.enums.EstadoTramo;
import ar.edu.utnfc.backend.ms_operaciones.repositories.AsignacionRepository;
import ar.edu.utnfc.backend.ms_operaciones.repositories.TramoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TramoService {
    private final TramoRepository tramoRepo;
    private final AsignacionRepository asigRepo;

    public Tramo get(UUID id) {
        return tramoRepo.findById(id).orElseThrow(() -> new RuntimeException("Tramo no encontrado"));
    }

    @Transactional
    public TramoResponse asignarCamion(UUID tramoId, AsignarCamionRequest req, String actor) {
        Tramo t = get(tramoId);
        t.setEstado(EstadoTramo.ASIGNADO);
        t.setUpdatedAt(Instant.now());

        asigRepo.save(Asignacion.builder()
                .tramo(t)
                .camionRef(req.getCamionRef())
                .asignadoPor(actor)
                .asignadoEn(Instant.now())
                .build());

        return TramoResponse.builder()
                .id(t.getId()).orden(t.getOrden())
                .tipo(t.getTipo().name()).estado(t.getEstado().name())
                .build();
    }
}
