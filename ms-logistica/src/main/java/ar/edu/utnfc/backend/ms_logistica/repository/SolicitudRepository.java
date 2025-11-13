package ar.edu.utnfc.backend.ms_logistica.repository;

import ar.edu.utnfc.backend.ms_logistica.model.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {
    
    // Buscar solicitudes por cliente
    List<Solicitud> findByClienteId(UUID clienteId);
    
    // Buscar solicitudes por estado
    List<Solicitud> findByEstado(EstadoSolicitud estado);
    
    // Buscar solicitudes por cliente y estado
    List<Solicitud> findByClienteIdAndEstado(UUID clienteId, EstadoSolicitud estado);
    
    // Buscar solicitudes por rango de prioridad
    List<Solicitud> findByPrioridadBetween(Integer minPrioridad, Integer maxPrioridad);
    
    // Buscar solicitudes por rango de fechas
    List<Solicitud> findByCreatedAtBetween(LocalDateTime desde, LocalDateTime hasta);
    
    // Buscar solicitudes por referencia de ruta
    List<Solicitud> findByRutaRefContaining(String rutaRef);
    
    // Contar solicitudes por estado
    long countByEstado(EstadoSolicitud estado);
    
    // Consulta personalizada para solicitudes activas (no completadas ni canceladas)
    @Query("SELECT s FROM Solicitud s WHERE s.estado IN ('BORRADOR', 'PROGRAMADA', 'EN_TRANSITO') ORDER BY s.prioridad DESC, s.createdAt ASC")
    List<Solicitud> findSolicitudesActivas();
    
    // Consulta para solicitudes recientes
    @Query("SELECT s FROM Solicitud s WHERE s.createdAt >= :fecha ORDER BY s.createdAt DESC")
    List<Solicitud> findSolicitudesRecientes(@Param("fecha") LocalDateTime fecha);
    
    // Consulta para estadísticas de solicitudes
    @Query("SELECT s.estado, COUNT(s), AVG(s.costoFinal), SUM(s.costoFinal) FROM Solicitud s GROUP BY s.estado")
    List<Object[]> findEstadisticasPorEstado();
    
    // Consulta para dashboard
    @Query("SELECT COUNT(s), " +
           "SUM(CASE WHEN s.estado = 'BORRADOR' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN s.estado = 'PROGRAMADA' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN s.estado = 'EN_TRANSITO' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN s.estado = 'ENTREGADA' THEN 1 ELSE 0 END), " +
           "COALESCE(SUM(s.costoEstimado), 0), " +
           "COALESCE(SUM(s.costoFinal), 0) " +
           "FROM Solicitud s")
    Object[] findEstadisticasDashboard();
    
    // Consulta para buscar solicitudes con filtros múltiples
    @Query("SELECT s FROM Solicitud s WHERE " +
           "(:clienteId IS NULL OR s.clienteId = :clienteId) AND " +
           "(:estado IS NULL OR s.estado = :estado) AND " +
           "(:prioridadMin IS NULL OR s.prioridad >= :prioridadMin) AND " +
           "(:prioridadMax IS NULL OR s.prioridad <= :prioridadMax) AND " +
           "(:fechaDesde IS NULL OR s.createdAt >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR s.createdAt <= :fechaHasta) AND " +
           "(:rutaRef IS NULL OR s.rutaRef LIKE %:rutaRef%)")
    List<Solicitud> findSolicitudesConFiltros(
        @Param("clienteId") UUID clienteId,
        @Param("estado") EstadoSolicitud estado,
        @Param("prioridadMin") Integer prioridadMin,
        @Param("prioridadMax") Integer prioridadMax,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta,
        @Param("rutaRef") String rutaRef
    );
}