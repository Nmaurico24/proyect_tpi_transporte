package ar.edu.utnfc.backend.ms_recursos.repository;

import ar.edu.utnfc.backend.ms_recursos.model.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, UUID> {
    
    // Buscar por nombre exacto
    Optional<Deposito> findByNombre(String nombre);
    
    // Buscar por estado activo/inactivo
    List<Deposito> findByLsActive(Boolean lsActive);
    
    // Verificar si existe un depósito con cierto nombre
    boolean existsByNombre(String nombre);
    
    // Buscar depósitos por rango de costo diario
    List<Deposito> findByCostoDiarioBetween(BigDecimal costoMin, BigDecimal costoMax);
    
    // Buscar depósitos activos por rango de costo
    List<Deposito> findByLsActiveTrueAndCostoDiarioBetween(BigDecimal costoMin, BigDecimal costoMax);
    
    // Buscar depósitos por nombre (búsqueda parcial case-insensitive)
    List<Deposito> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar depósitos activos por nombre
    List<Deposito> findByLsActiveTrueAndNombreContainingIgnoreCase(String nombre);
    
    // Consulta personalizada para buscar depósitos dentro de un radio geográfico
    @Query("SELECT d FROM Deposito d WHERE " +
           "6371 * acos(cos(radians(:lat)) * cos(radians(d.lat)) * " +
           "cos(radians(d.lng) - radians(:lng)) + sin(radians(:lat)) * " +
           "sin(radians(d.lat))) <= :radiusKm AND d.lsActive = true")
    List<Deposito> findDepositosCercanos(
        @Param("lat") BigDecimal lat,
        @Param("lng") BigDecimal lng,
        @Param("radiusKm") Double radiusKm
    );
    
    // Consulta para obtener depósitos ordenados por costo (más económico primero)
    List<Deposito> findByLsActiveTrueOrderByCostoDiarioAsc();
    
    // Consulta para contar depósitos activos
    long countByLsActiveTrue();
    
    // Consulta para obtener el costo promedio de depósitos activos
    @Query("SELECT AVG(d.costoDiario) FROM Deposito d WHERE d.lsActive = true")
    Optional<BigDecimal> findCostoDiarioPromedioActivos();
    
    // Buscar depósitos con costo menor o igual a un valor
    List<Deposito> findByCostoDiarioLessThanEqual(BigDecimal costoMax);
    
    // Buscar depósitos activos con costo menor o igual a un valor
    List<Deposito> findByLsActiveTrueAndCostoDiarioLessThanEqual(BigDecimal costoMax);
}