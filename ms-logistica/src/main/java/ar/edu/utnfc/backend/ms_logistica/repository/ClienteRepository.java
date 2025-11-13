package ar.edu.utnfc.backend.ms_logistica.repository;

import ar.edu.utnfc.backend.ms_logistica.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
    // Buscar por número exacto
    Optional<Cliente> findByNumero(String numero);
    
    // Buscar por estado activo/inactivo
    List<Cliente> findByIsActive(Boolean isActive);
    
    // Buscar por nombre (búsqueda parcial case-insensitive)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar clientes activos por nombre
    List<Cliente> findByIsActiveTrueAndNombreContainingIgnoreCase(String nombre);
    
    // Buscar por email
    Optional<Cliente> findByEmail(String email);
    
    // Verificar si existe un cliente con cierto número
    boolean existsByNumero(String numero);
    
    // Verificar si existe un cliente con cierto email
    boolean existsByEmail(String email);
    
    // Contar clientes activos
    long countByIsActiveTrue();
    
    // Buscar clientes por parte del número
    List<Cliente> findByNumeroContaining(String numero);
    
    // Consulta personalizada para buscar clientes con solicitudes activas
    @Query("SELECT DISTINCT c FROM Cliente c JOIN Solicitud s ON c.id = s.clienteId WHERE s.estado IN ('PROGRAMADA', 'EN_TRANSITO') AND c.isActive = true")
    List<Cliente> findClientesConSolicitudesActivas();
    
    // Consulta para obtener estadísticas básicas
    @Query("SELECT COUNT(c), SUM(CASE WHEN c.isActive = true THEN 1 ELSE 0 END) FROM Cliente c")
    Object[] findEstadisticasClientes();
}