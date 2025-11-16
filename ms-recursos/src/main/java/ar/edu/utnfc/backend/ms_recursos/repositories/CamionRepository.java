package ar.edu.utnfc.backend.ms_recursos.repositories;

import ar.edu.utnfc.backend.ms_recursos.models.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CamionRepository extends JpaRepository<Camion, Long> {
    List<Camion> findByDisponibleTrue();
}
