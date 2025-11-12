package ar.edu.utnfc.backend.ms_recursos.repository;

import ar.edu.utnfc.backend.ms_recursos.model.CostoEstimacion;
import ar.edu.utnfc.backend.ms_recursos.model.CostoFinal;
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
public interface CostoFinalRepository extends JpaRepository<CostoFinal, UUID> {
    
    // Buscar por referencia de ruta
    Optional<CostoFinal> findByRutaRef(String rutaRef);
    
    // Buscar costos finales por rango de fechas
    List<CostoFinal> findByCreateEnBetween(LocalDateTime desde, LocalDateTime hasta);
    
    // Buscar costos finales por rango de total
    List<CostoFinal> findByTotalFinalBetween(BigDecimal min, BigDecimal max);
    
    // Consulta para obtener el total final promedio
    @Query("SELECT AVG(cf.totalFinal) FROM CostoFinal cf")
    Optional<BigDecimal> findPromedioTotalFinal();
    
    // Consulta para costos finales recientes
    @Query("SELECT cf FROM CostoFinal cf WHERE cf.createEn >= :fecha ORDER BY cf.createEn DESC")
    List<CostoFinal> findCostosFinalesRecientes(@Param("fecha") LocalDateTime fecha);
    
    // Consulta para estadísticas de costos
    @Query("SELECT COUNT(cf), SUM(cf.totalFinal), AVG(cf.totalFinal), MIN(cf.totalFinal), MAX(cf.totalFinal) FROM CostoFinal cf")
    Object[] findEstadisticasCostos();
    
    // Verificar si existe costo final para una ruta
    boolean existsByRutaRef(String rutaRef);
}