package ar.edu.utnfc.backend.ms_recursos.repository;

import ar.edu.utnfc.backend.ms_recursos.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CamionRepository extends JpaRepository<Camion, UUID> {
    
    Optional<Camion> findByDominio(String dominio);
    
    List<Camion> findByLsActive(Boolean lsActive);
    
    @Query("SELECT c FROM Camion c WHERE c.lsActive = true AND c.capPesoKg >= :pesoMin AND c.capVolumeM3 >= :volumenMin")
    List<Camion> findCamionesDisponiblesPorCapacidad(
        @Param("pesoMin") BigDecimal pesoMin, 
        @Param("volumenMin") BigDecimal volumenMin
    );
    
    boolean existsByDominio(String dominio);
}