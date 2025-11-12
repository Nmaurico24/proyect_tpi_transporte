package ar.edu.utnfc.backend.ms_recursos.repository;

import ar.edu.utnfc.backend.ms_recursos.model.TarifaDeposito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TarifaDepositoRepository extends JpaRepository<TarifaDeposito, UUID> {
    
    List<TarifaDeposito> findByTarifaId(UUID tarifaId);
    
    List<TarifaDeposito> findByDepositoId(UUID depositoId);
    
    @Query("SELECT td FROM TarifaDeposito td WHERE td.depositoId = :depositoId AND EXISTS " +
           "(SELECT t FROM Tarifa t WHERE t.id = td.tarifaId AND t.vigenciaHasta >= CURRENT_DATE)")
    List<TarifaDeposito> findTarifasVigentesByDeposito(@Param("depositoId") UUID depositoId);
    
    void deleteByTarifaId(UUID tarifaId);
    
    void deleteByDepositoId(UUID depositoId);
    
    boolean existsByTarifaIdAndDepositoId(UUID tarifaId, UUID depositoId);
}