package ar.edu.utnfc.backend.ms_operaciones.repositories;

import ar.edu.utnfc.backend.ms_operaciones.models.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RutaRepository extends JpaRepository<Ruta, UUID> {
    List<Ruta> findBySolicitudRef(String solicitudRef);
}
