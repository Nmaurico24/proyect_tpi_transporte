package ar.edu.utnfc.backend.ms_logistica.repositories;

import ar.edu.utnfc.backend.ms_logistica.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
