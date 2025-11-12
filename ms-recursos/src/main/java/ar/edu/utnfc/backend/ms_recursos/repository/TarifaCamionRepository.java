package ar.edu.utnfc.backend.ms_recursos.repository;

import ar.edu.utnfc.backend.ms_recursos.model.TarifaCamion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TarifaCamionRepository extends JpaRepository<TarifaCamion, UUID> {
    
    List<TarifaCamion> findByTarifaId(UUID tarifaId);
    
    List<TarifaCamion> findByCamionId(UUID camionId);
    
    @Query("SELECT tc FROM TarifaCamion tc WHERE tc.camionId = :camionId AND EXISTS " +
           "(SELECT t FROM Tarifa t WHERE t.id = tc.tarifaId AND t.vigenciaHasta >= CURRENT_DATE)")
    List<TarifaCamion> findTarifasVigentesByCamion(@Param("camionId") UUID camionId);
    
    void deleteByTarifaId(UUID tarifaId);
    
    void deleteByCamionId(UUID camionId);
    
    boolean existsByTarifaIdAndCamionId(UUID tarifaId, UUID camionId);
}
