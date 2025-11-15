package ar.edu.utnfc.backend.ms_operaciones.repositories;

import ar.edu.utnfc.backend.ms_operaciones.models.TimelineSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TimelineSolicitudRepository extends JpaRepository<TimelineSolicitud, UUID> {
    Optional<TimelineSolicitud> findBySolicitudRef(String solicitudRef);
}
