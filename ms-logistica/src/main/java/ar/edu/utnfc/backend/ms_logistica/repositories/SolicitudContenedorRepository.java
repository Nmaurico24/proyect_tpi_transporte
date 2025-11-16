package ar.edu.utnfc.backend.ms_logistica.repositories;

import ar.edu.utnfc.backend.ms_logistica.models.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.models.SolicitudContenedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitudContenedorRepository extends JpaRepository<SolicitudContenedor, UUID> {
    List<SolicitudContenedor> findBySolicitud(Solicitud solicitud);
}
