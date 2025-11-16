package ar.edu.utnfc.backend.ms_logistica.repositories;

import ar.edu.utnfc.backend.ms_logistica.models.EstadoSolicitud;
import ar.edu.utnfc.backend.ms_logistica.models.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {

    Page<Solicitud> findByEstadoAndCliente(EstadoSolicitud estado, Cliente cliente, Pageable pageable);

    Page<Solicitud> findByEstado(EstadoSolicitud estado, Pageable pageable);

    Page<Solicitud> findByCliente(Cliente cliente, Pageable pageable);
}
