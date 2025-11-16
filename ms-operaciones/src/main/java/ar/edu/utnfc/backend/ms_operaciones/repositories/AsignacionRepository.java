package ar.edu.utnfc.backend.ms_operaciones.repositories;

import ar.edu.utnfc.backend.ms_operaciones.models.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AsignacionRepository extends JpaRepository<Asignacion, UUID> {
}
