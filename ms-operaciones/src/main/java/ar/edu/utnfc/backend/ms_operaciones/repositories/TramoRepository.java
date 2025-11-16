package ar.edu.utnfc.backend.ms_operaciones.repositories;

import ar.edu.utnfc.backend.ms_operaciones.models.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TramoRepository extends JpaRepository<Tramo, UUID> {
    List<Tramo> findByRutaIdOrderByOrdenAsc(UUID rutaId);
}
