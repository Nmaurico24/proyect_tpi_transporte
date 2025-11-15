package ar.edu.utnfc.backend.ms_logistica.repositories;

import ar.edu.utnfc.backend.ms_logistica.models.Contenedor;
import ar.edu.utnfc.backend.ms_logistica.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContenedorRepository extends JpaRepository<Contenedor, UUID> {
    List<Contenedor> findByCliente(Cliente cliente);
}
