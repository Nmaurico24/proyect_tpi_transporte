package ar.edu.utnfc.backend.ms_logistica.repositories;

import ar.edu.utnfc.backend.ms_logistica.models.Estimada;
import ar.edu.utnfc.backend.ms_logistica.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EstimadaRepository extends JpaRepository<Estimada, UUID> {
    List<Estimada> findBySolicitud(Solicitud solicitud);
}
