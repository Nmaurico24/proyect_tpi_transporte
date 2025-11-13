package ar.edu.utnfc.backend.ms_logistica.repository;

import ar.edu.utnfc.backend.ms_logistica.model.Contenedor;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoContenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, UUID> {
    
    // Buscar contenedores por cliente
    List<Contenedor> findByClienteId(UUID clienteId);
    
    // Buscar contenedores por estado
    List<Contenedor> findByEstado(EstadoContenedor estado);
    
    // Buscar contenedores por cliente y estado
    List<Contenedor> findByClienteIdAndEstado(UUID clienteId, EstadoContenedor estado);
    
    // Buscar contenedores activos por cliente
    List<Contenedor> findByClienteIdAndIsActiveTrue(UUID clienteId);
    
    // Buscar por etiqueta (exacta)
    Optional<Contenedor> findByEtiqueta(String etiqueta);
    
    // Buscar por etiqueta (parcial, case-insensitive)
    List<Contenedor> findByEtiquetaContainingIgnoreCase(String etiqueta);
    
    // Buscar contenedores disponibles (para asignación)
    List<Contenedor> findByEstadoAndIsActiveTrue(EstadoContenedor estado);
    
    // Buscar contenedores por rango de peso
    List<Contenedor> findByPesoKgBetween(BigDecimal pesoMin, BigDecimal pesoMax);
    
    // Buscar contenedores por rango de volumen
    List<Contenedor> findByVolumenM3Between(BigDecimal volumenMin, BigDecimal volumenMax);
    
    // Verificar si existe un contenedor con cierta etiqueta
    boolean existsByEtiqueta(String etiqueta);
    
    // Contar contenedores por estado
    long countByEstado(EstadoContenedor estado);
    
    // Contar contenedores activos por cliente
    long countByClienteIdAndIsActiveTrue(UUID clienteId);
    
    // Consulta personalizada para contenedores disponibles con capacidad mínima
    @Query("SELECT c FROM Contenedor c WHERE c.estado = 'DISPONIBLE' AND c.isActive = true AND c.pesoKg >= :pesoMin AND c.volumenM3 >= :volumenMin")
    List<Contenedor> findContenedoresDisponiblesConCapacidad(
        @Param("pesoMin") BigDecimal pesoMin,
        @Param("volumenMin") BigDecimal volumenMin
    );
    
    // Consulta para estadísticas de contenedores
    @Query("SELECT c.estado, COUNT(c), AVG(c.pesoKg), AVG(c.volumenM3) FROM Contenedor c WHERE c.isActive = true GROUP BY c.estado")
    List<Object[]> findEstadisticasPorEstado();
    
    // Consulta para capacidad total disponible
    @Query("SELECT COALESCE(SUM(c.pesoKg), 0), COALESCE(SUM(c.volumenM3), 0) FROM Contenedor c WHERE c.estado = 'DISPONIBLE' AND c.isActive = true")
    Object[] findCapacidadTotalDisponible();
    
    // Consulta para contenedores en un depósito específico (integración con ms-recursos)
    @Query("SELECT c FROM Contenedor c WHERE c.id IN (SELECT sc.contenedorId FROM SolicitudContenedor sc WHERE sc.solicitudId IN (SELECT s.id FROM Solicitud s WHERE s.estado = 'EN_TRANSITO'))")
    List<Contenedor> findContenedoresEnTransito();
}