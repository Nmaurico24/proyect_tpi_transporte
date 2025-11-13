package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.dto.*;
import ar.edu.utnfc.backend.ms_logistica.model.SolicitudContenedor;
import ar.edu.utnfc.backend.ms_logistica.model.Contenedor;
import ar.edu.utnfc.backend.ms_logistica.model.Solicitud;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoContenedor;
import ar.edu.utnfc.backend.ms_logistica.repository.SolicitudContenedorRepository;
import ar.edu.utnfc.backend.ms_logistica.repository.SolicitudRepository;
import ar.edu.utnfc.backend.ms_logistica.repository.ContenedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudContenedorService {

    @Autowired
    private SolicitudContenedorRepository solicitudContenedorRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private ContenedorRepository contenedorRepository;

    public SolicitudContenedorDTO agregarContenedorASolicitud(UUID solicitudId, AgregarContenedorSolicitudDTO agregarDTO) {
        // Validar que la solicitud existe
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));
        
        // Validar que el contenedor existe
        Contenedor contenedor = contenedorRepository.findById(agregarDTO.contenedorId())
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con ID: " + agregarDTO.contenedorId()));
        
        // Validar que el contenedor esté disponible
        if (contenedor.getEstado() != EstadoContenedor.DISPONIBLE) {
            throw new RuntimeException("El contenedor no está disponible. Estado actual: " + contenedor.getEstado());
        }
        
        // Validar que no esté ya asignado a esta solicitud
        if (solicitudContenedorRepository.existsBySolicitudIdAndContenedorId(solicitudId, agregarDTO.contenedorId())) {
            throw new RuntimeException("El contenedor ya está asignado a esta solicitud");
        }
        
        // Obtener el próximo orden de carga
        Integer proximoOrden = solicitudContenedorRepository.findProximoOrdenCarga(solicitudId);
        
        // Crear la relación
        SolicitudContenedor relacion = SolicitudContenedor.builder()
            .solicitudId(solicitudId)
            .contenedorId(agregarDTO.contenedorId())
            .ordenCarga(proximoOrden)
            .observaciones("Contenedor agregado a la solicitud")
            .build();
        
        SolicitudContenedor relacionGuardada = solicitudContenedorRepository.save(relacion);
        
        // Actualizar estado del contenedor
        contenedor.setEstado(EstadoContenedor.ASIGNADO);
        contenedorRepository.save(contenedor);
        
        return convertToSolicitudContenedorDTO(relacionGuardada);
    }

    public void removerContenedorDeSolicitud(UUID solicitudId, UUID contenedorId) {
        // Validar que la relación existe
        SolicitudContenedor relacion = solicitudContenedorRepository.findBySolicitudIdAndContenedorId(solicitudId, contenedorId)
            .orElseThrow(() -> new RuntimeException("El contenedor no está asignado a esta solicitud"));
        
        // Eliminar la relación
        solicitudContenedorRepository.deleteBySolicitudIdAndContenedorId(solicitudId, contenedorId);
        
        // Verificar si el contenedor está en otras solicitudes activas
        boolean enOtrasSolicitudes = solicitudContenedorRepository.existsEnSolicitudesActivas(contenedorId);
        
        // Actualizar estado del contenedor
        Contenedor contenedor = contenedorRepository.findById(contenedorId)
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));
        
        if (!enOtrasSolicitudes) {
            contenedor.setEstado(EstadoContenedor.DISPONIBLE);
            contenedorRepository.save(contenedor);
        }
    }

    public List<ContenedorListDTO> obtenerContenedoresDeSolicitud(UUID solicitudId) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + solicitudId);
        }
        
        List<SolicitudContenedor> relaciones = solicitudContenedorRepository.findContenedoresConDetallesBySolicitudId(solicitudId);
        
        return relaciones.stream()
            .map(rel -> contenedorRepository.findById(rel.getContenedorId()))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .map(this::convertToContenedorListDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudListDTO> obtenerSolicitudesDeContenedor(UUID contenedorId) {
        // Validar que el contenedor existe
        if (!contenedorRepository.existsById(contenedorId)) {
            throw new RuntimeException("Contenedor no encontrado con ID: " + contenedorId);
        }
        
        List<SolicitudContenedor> relaciones = solicitudContenedorRepository.findSolicitudesByContenedorId(contenedorId);
        
        return relaciones.stream()
            .map(rel -> solicitudRepository.findById(rel.getSolicitudId()))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .map(this::convertToSolicitudListDTO)
            .collect(Collectors.toList());
    }

    public void limpiarContenedoresDeSolicitud(UUID solicitudId) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + solicitudId);
        }
        
        // Obtener todos los contenedores de la solicitud
        List<SolicitudContenedor> relaciones = solicitudContenedorRepository.findBySolicitudId(solicitudId);
        
        // Liberar los contenedores
        for (SolicitudContenedor relacion : relaciones) {
            Contenedor contenedor = contenedorRepository.findById(relacion.getContenedorId())
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));
            
            // Verificar si el contenedor está en otras solicitudes activas
            boolean enOtrasSolicitudes = solicitudContenedorRepository.existsEnSolicitudesActivas(contenedor.getId());
            
            if (!enOtrasSolicitudes) {
                contenedor.setEstado(EstadoContenedor.DISPONIBLE);
                contenedorRepository.save(contenedor);
            }
        }
        
        // Eliminar todas las relaciones
        solicitudContenedorRepository.deleteBySolicitudId(solicitudId);
    }

    public void actualizarOrdenContenedores(UUID solicitudId, List<UUID> contenedorIds) {
        // Validar que la solicitud existe
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + solicitudId);
        }
        
        // Actualizar el orden de cada contenedor
        for (int i = 0; i < contenedorIds.size(); i++) {
            UUID contenedorId = contenedorIds.get(i);
            Integer nuevoOrden = i + 1;
            
            // Verificar que la relación existe
            if (!solicitudContenedorRepository.existsBySolicitudIdAndContenedorId(solicitudId, contenedorId)) {
                throw new RuntimeException("El contenedor " + contenedorId + " no está asignado a esta solicitud");
            }
            
            // Actualizar el orden
            solicitudContenedorRepository.actualizarOrdenCarga(solicitudId, contenedorId, nuevoOrden);
        }
    }

    public long contarContenedoresEnSolicitud(UUID solicitudId) {
        return solicitudContenedorRepository.countBySolicitudId(solicitudId);
    }

    public boolean verificarContenedorEnSolicitud(UUID solicitudId, UUID contenedorId) {
        return solicitudContenedorRepository.existsBySolicitudIdAndContenedorId(solicitudId, contenedorId);
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    private SolicitudContenedorDTO convertToSolicitudContenedorDTO(SolicitudContenedor relacion) {
        return new SolicitudContenedorDTO(
            relacion.getId(),
            relacion.getSolicitudId(),
            relacion.getContenedorId()
        );
    }

    private ContenedorListDTO convertToContenedorListDTO(Contenedor contenedor) {
        String clienteNombre = "Cliente"; // Podrías obtenerlo de una consulta
        
        return new ContenedorListDTO(
            contenedor.getId(),
            contenedor.getClienteId(),
            clienteNombre,
            contenedor.getEtiqueta(),
            contenedor.getPesoKg(),
            contenedor.getVolumenM3(),
            contenedor.getEstado().toString(),
            contenedor.getIsActive()
        );
    }

    private SolicitudListDTO convertToSolicitudListDTO(Solicitud solicitud) {
        String clienteNombre = "Cliente"; // Podrías obtenerlo de una consulta
        
        return new SolicitudListDTO(
            solicitud.getId(),
            solicitud.getClienteId(),
            clienteNombre,
            solicitud.getEstado(),
            solicitud.getPrioridad(),
            solicitud.getOrigenDireccion(),
            solicitud.getDestinoDireccion(),
            solicitud.getCostoEstimado(),
            solicitud.getRutaRef(),
            solicitud.getCreatedAt()
        );
    }
}