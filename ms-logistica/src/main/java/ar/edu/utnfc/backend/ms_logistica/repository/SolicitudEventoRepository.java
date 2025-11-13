package ar.edu.utnfc.backend.ms_logistica.repository;

import ar.edu.utnfc.backend.ms_logistica.model.SolicitudEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolicitudEventoRepository extends JpaRepository<SolicitudEvento, UUID> {
    
    // Buscar eventos por solicitud
    List<SolicitudEvento> findBySolicitudId(UUID solicitudId);
    
    // Buscar eventos por solicitud ordenados por fecha (más reciente primero)
    List<SolicitudEvento> findBySolicitudIdOrderByCreatedAtDesc(UUID solicitudId);
    
    // Buscar eventos por estado
    List<SolicitudEvento> findByEstado(String estado);
    
    // Buscar eventos por actor
    List<SolicitudEvento> findByActor(String actor);
    
    // Buscar eventos por rango de fechas
    List<SolicitudEvento> findByCreatedAtBetween(LocalDateTime desde, LocalDateTime hasta);
    
    // Buscar eventos recientes
    @Query("SELECT se FROM SolicitudEvento se WHERE se.createdAt >= :fecha ORDER BY se.createdAt DESC")
    List<SolicitudEvento> findEventosRecientes(@Param("fecha") LocalDateTime fecha);
    
    // Obtener el último evento de una solicitud
    @Query("SELECT se FROM SolicitudEvento se WHERE se.solicitudId = :solicitudId ORDER BY se.createdAt DESC LIMIT 1")
    Optional<SolicitudEvento> findUltimoEventoBySolicitudId(@Param("solicitudId") UUID solicitudId);
    
    // Consulta para historial completo de una solicitud
    @Query("SELECT se FROM SolicitudEvento se WHERE se.solicitudId = :solicitudId ORDER BY se.createdAt DESC")
    List<SolicitudEvento> findHistorialCompletoBySolicitudId(@Param("solicitudId") UUID solicitudId);
    
    // Contar eventos por solicitud
    long countBySolicitudId(UUID solicitudId);
    
    // Buscar eventos por múltiples estados
    List<SolicitudEvento> findByEstadoIn(List<String> estados);
    
    // Consulta para estadísticas de eventos por actor
    @Query("SELECT se.actor, COUNT(se) FROM SolicitudEvento se GROUP BY se.actor ORDER BY COUNT(se) DESC")
    List<Object[]> findEstadisticasPorActor();
    
    // Consulta para línea de tiempo de una solicitud
    @Query("SELECT se FROM SolicitudEvento se WHERE se.solicitudId = :solicitudId ORDER BY se.createdAt ASC")
    List<SolicitudEvento> findLineaTiempoBySolicitudId(@Param("solicitudId") UUID solicitudId);
}