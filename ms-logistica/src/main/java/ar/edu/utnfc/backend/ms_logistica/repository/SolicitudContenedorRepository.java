package ar.edu.utnfc.backend.ms_logistica.repository;

import ar.edu.utnfc.backend.ms_logistica.model.SolicitudContenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolicitudContenedorRepository extends JpaRepository<SolicitudContenedor, UUID> {
    
    // Buscar relaciones por solicitud
    List<SolicitudContenedor> findBySolicitudId(UUID solicitudId);
    
    // Buscar relaciones por contenedor
    List<SolicitudContenedor> findByContenedorId(UUID contenedorId);
    
    // Buscar relación específica
    Optional<SolicitudContenedor> findBySolicitudIdAndContenedorId(UUID solicitudId, UUID contenedorId);
    
    // Verificar si existe una relación
    boolean existsBySolicitudIdAndContenedorId(UUID solicitudId, UUID contenedorId);
    
    // Contar contenedores por solicitud
    long countBySolicitudId(UUID solicitudId);
    
    // Eliminar relaciones por solicitud
    @Transactional
    @Modifying
    void deleteBySolicitudId(UUID solicitudId);
    
    // Eliminar relaciones por contenedor
    @Transactional
    @Modifying
    void deleteByContenedorId(UUID contenedorId);
    
    // Eliminar relación específica
    @Transactional
    @Modifying
    void deleteBySolicitudIdAndContenedorId(UUID solicitudId, UUID contenedorId);
    
    // Obtener contenedores de una solicitud con información del contenedor
    @Query("SELECT sc FROM SolicitudContenedor sc JOIN Contenedor c ON sc.contenedorId = c.id WHERE sc.solicitudId = :solicitudId ORDER BY sc.ordenCarga ASC")
    List<SolicitudContenedor> findContenedoresConDetallesBySolicitudId(@Param("solicitudId") UUID solicitudId);
    
    // Obtener solicitudes de un contenedor
    @Query("SELECT sc FROM SolicitudContenedor sc JOIN Solicitud s ON sc.solicitudId = s.id WHERE sc.contenedorId = :contenedorId ORDER BY s.createdAt DESC")
    List<SolicitudContenedor> findSolicitudesByContenedorId(@Param("contenedorId") UUID contenedorId);
    
    // Consulta para verificar si un contenedor está en uso en solicitudes activas
    @Query("SELECT COUNT(sc) > 0 FROM SolicitudContenedor sc JOIN Solicitud s ON sc.solicitudId = s.id WHERE sc.contenedorId = :contenedorId AND s.estado IN ('BORRADOR', 'PROGRAMADA', 'EN_TRANSITO')")
    boolean existsEnSolicitudesActivas(@Param("contenedorId") UUID contenedorId);
    
    // Obtener el próximo orden de carga para una solicitud
    @Query("SELECT COALESCE(MAX(sc.ordenCarga), 0) + 1 FROM SolicitudContenedor sc WHERE sc.solicitudId = :solicitudId")
    Integer findProximoOrdenCarga(@Param("solicitudId") UUID solicitudId);
    
    // Actualizar orden de carga
    @Transactional
    @Modifying
    @Query("UPDATE SolicitudContenedor sc SET sc.ordenCarga = :nuevoOrden WHERE sc.solicitudId = :solicitudId AND sc.contenedorId = :contenedorId")
    void actualizarOrdenCarga(@Param("solicitudId") UUID solicitudId, @Param("contenedorId") UUID contenedorId, @Param("nuevoOrden") Integer nuevoOrden);
}