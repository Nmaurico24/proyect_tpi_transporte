package ar.edu.utnfc.backend.ms_logistica.repositories;

import ar.edu.utnfc.backend.ms_logistica.models.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.models.SolicitudEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitudEventoRepository extends JpaRepository<SolicitudEvento, UUID> {
    List<SolicitudEvento> findBySolicitudOrderByCreatedAtAsc(Solicitud solicitud);
}
