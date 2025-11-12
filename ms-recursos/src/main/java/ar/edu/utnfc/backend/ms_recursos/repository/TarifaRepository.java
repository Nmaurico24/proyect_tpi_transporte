package ar.edu.utnfc.backend.ms_recursos.repository;

import ar.edu.utnfc.backend.ms_recursos.model.Moneda;
import ar.edu.utnfc.backend.ms_recursos.model.Tarifa;
import ar.edu.utnfc.backend.ms_recursos.model.UnidadTarifa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, UUID> {
    
    // Buscar tarifas por descripción
    List<Tarifa> findByDescriptionContainingIgnoreCase(String description);
    
    // Buscar tarifas por unidad
    List<Tarifa> findByUnidad(UnidadTarifa unidad);
    
    // Buscar tarifas por moneda
    List<Tarifa> findByMoneda(Moneda moneda);
    
    // Buscar tarifas vigentes (fecha actual <= vigenciaHasta )
    List<Tarifa> findByVigenciaHastaGreaterThanEqual(LocalDate fecha);
    
    // Buscar tarifas vencidas
    List<Tarifa> findByVigenciaHastaLessThan(LocalDate fecha);
    
    // Buscar tarifas por rango de fechas de vigencia
    List<Tarifa> findByVigenciaHastaBetween(LocalDate inicio, LocalDate fin);
    
    // Consulta personalizada para tarifas vigentes
    @Query("SELECT t FROM Tarifa t WHERE t.vigenciaHasta >= CURRENT_DATE ORDER BY t.vigenciaHasta ASC")
    List<Tarifa> findTarifasVigentes();
    
    // Consulta para tarifas por unidad y vigentes
    @Query("SELECT t FROM Tarifa t WHERE t.unidad = :unidad AND t.vigenciaHasta >= CURRENT_DATE")
    List<Tarifa> findTarifasVigentesByUnidad(@Param("unidad") UnidadTarifa unidad);
    
    // Buscar por descripción exacta
    Optional<Tarifa> findByDescription(String description);
    
    // Buscar por descripción y versión
    Optional<Tarifa> findByDescriptionAndVersion(String description, Integer version);
    
    // Verificar si existe una tarifa con misma descripción y versión
    boolean existsByDescriptionAndVersion(String description, Integer version);
    
    // Buscar tarifas por versión
    List<Tarifa> findByVersion(Integer version);
    
    // Buscar la última versión de una tarifa por descripción
    @Query("SELECT t FROM Tarifa t WHERE t.description = :description ORDER BY t.version DESC LIMIT 1")
    Optional<Tarifa> findUltimaVersionByDescription(@Param("description") String description);
    
    // Consulta para obtener todas las unidades de tarifa disponibles
    @Query("SELECT DISTINCT t.unidad FROM Tarifa t WHERE t.vigenciaHasta >= CURRENT_DATE")
    List<UnidadTarifa> findUnidadesTarifariasVigentes();
}