package ar.edu.utnfc.backend.ms_operaciones.repositories;

import ar.edu.utnfc.backend.ms_operaciones.models.TrackingEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackingEventoRepository extends JpaRepository<TrackingEvento, UUID> {
    List<TrackingEvento> findByTramoIdOrderByRegistradoEnAsc(UUID tramoId);
}
