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
public interface CostoEstimacionRepository extends JpaRepository<CostoEstimacion, UUID> {
    
    // Buscar por referencia de solicitud
    Optional<CostoEstimacion> findBySolicitudRef(String solicitudRef);
    
    // Buscar estimaciones por rango de fechas
    List<CostoEstimacion> findByCreateEnBetween(LocalDateTime desde, LocalDateTime hasta);
    
    // Buscar estimaciones que contengan cierta ruta
    List<CostoEstimacion> findByRutaPayiproContaining(String rutaRef);
    
    // Consulta para obtener el total estimado promedio
    @Query("SELECT AVG(ce.totalEstimado) FROM CostoEstimacion ce")
    Optional<BigDecimal> findPromedioTotalEstimado();
    
    // Consulta para estimaciones recientes
    @Query("SELECT ce FROM CostoEstimacion ce WHERE ce.createEn >= :fecha ORDER BY ce.createEn DESC")
    List<CostoEstimacion> findEstimacionesRecientes(@Param("fecha") LocalDateTime fecha);
    
    // Verificar si existe estimación para una solicitud
    boolean existsBySolicitudRef(String solicitudRef);
}

