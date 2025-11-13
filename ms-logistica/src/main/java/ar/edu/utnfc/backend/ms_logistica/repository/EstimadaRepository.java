package ar.edu.utnfc.backend.ms_logistica.repository;

import ar.edu.utnfc.backend.ms_logistica.model.Estimada;
import ar.edu.utnfc.backend.ms_logistica.model.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstimadaRepository extends JpaRepository<Estimada, UUID> {
    
    // Buscar estimaciones por solicitud
    List<Estimada> findBySolicitudId(UUID solicitudId);
    
    // Buscar estimaciones por fuente
    List<Estimada> findByFuente(String fuente);
    
    // Buscar estimaciones por moneda
    List<Estimada> findByMoneda(Moneda moneda);
    
    // Buscar estimaciones por rango de costo
    List<Estimada> findByCostoTotalBetween(BigDecimal costoMin, BigDecimal costoMax);
    
    // Buscar estimaciones por rango de distancia
    List<Estimada> findByDistanciaKmBetween(BigDecimal distanciaMin, BigDecimal distanciaMax);
    
    // Buscar estimaciones por rango de fechas
    List<Estimada> findByCreatedAtBetween(LocalDateTime desde, LocalDateTime hasta);
    
    // Obtener la última estimación para una solicitud
    @Query("SELECT e FROM Estimada e WHERE e.solicitudId = :solicitudId ORDER BY e.createdAt DESC LIMIT 1")
    Optional<Estimada> findUltimaEstimacionBySolicitudId(@Param("solicitudId") UUID solicitudId);
    
    // Obtener estimaciones recientes
    @Query("SELECT e FROM Estimada e WHERE e.createdAt >= :fecha ORDER BY e.createdAt DESC")
    List<Estimada> findEstimacionesRecientes(@Param("fecha") LocalDateTime fecha);
    
    // Consulta para estadísticas de estimaciones
    @Query("SELECT COUNT(e), AVG(e.costoTotal), AVG(e.distanciaKm), AVG(e.duracionMin) FROM Estimada e")
    Object[] findEstadisticasGenerales();
    
    // Consulta para comparar estimaciones por fuente
    @Query("SELECT e.fuente, COUNT(e), AVG(e.costoTotal), AVG(e.distanciaKm) FROM Estimada e GROUP BY e.fuente")
    List<Object[]> findEstadisticasPorFuente();
    
    // Consulta para encontrar la estimación más precisa (comparando con costo final)
    @Query("SELECT e FROM Estimada e JOIN Solicitud s ON e.solicitudId = s.id WHERE s.costoFinal IS NOT NULL ORDER BY ABS(e.costoTotal - s.costoFinal) ASC")
    List<Estimada> findEstimacionesMasPrecisas();
    
    // Consulta para estimaciones con mayor diferencia con el costo final
    @Query("SELECT e FROM Estimada e JOIN Solicitud s ON e.solicitudId = s.id WHERE s.costoFinal IS NOT NULL ORDER BY ABS(e.costoTotal - s.costoFinal) DESC")
    List<Estimada> findEstimacionesMenosPrecisas();
    
    // Verificar si existe estimación para una solicitud
    boolean existsBySolicitudId(UUID solicitudId);
    
    // Contar estimaciones por fuente
    long countByFuente(String fuente);
    
    // Obtener el costo promedio por fuente
    @Query("SELECT e.fuente, AVG(e.costoTotal) FROM Estimada e GROUP BY e.fuente")
    List<Object[]> findCostoPromedioPorFuente();
}