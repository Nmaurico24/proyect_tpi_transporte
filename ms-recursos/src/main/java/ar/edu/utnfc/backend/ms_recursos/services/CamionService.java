package ar.edu.utnfc.backend.ms_recursos.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.utnfc.backend.ms_recursos.data.RecursosStore;
import ar.edu.utnfc.backend.ms_recursos.dto.CamionCreateDTO;
import ar.edu.utnfc.backend.ms_recursos.dto.CamionDTO;
import ar.edu.utnfc.backend.ms_recursos.dto.CamionListDTO;
import ar.edu.utnfc.backend.ms_recursos.dto.CamionUpdateDTO;
import ar.edu.utnfc.backend.ms_recursos.dto.DisponibilidadRequestDTO;
import ar.edu.utnfc.backend.ms_recursos.dto.DisponibilidadResponseDTO;
import ar.edu.utnfc.backend.ms_recursos.model.Camion;
import ar.edu.utnfc.backend.ms_recursos.repository.CamionRepository;
import ar.edu.utnfc.backend.ms_recursos.services.interfaz.CamionInterface;

@Service
public class CamionService implements CamionInterface {

    @Autowired
    //private RecursosStore recursosStore; // Temporal - trabaja con Entities
    private CamionRepository camionRepository;

    @Override
    public CamionDTO crearCamion(CamionCreateDTO camionCreateDTO) {
        // Validar que no exista un camión con el mismo dominio
        if (camionRepository.existsByDominio(camionCreateDTO.dominio())) {
            throw new RuntimeException("Ya existe un camión con el dominio: " + camionCreateDTO.dominio());
        }
        
        // Convertir DTO → Entity (los timestamps se generan automáticamente con @PrePersist)
        Camion camion = Camion.builder()
            .dominio(camionCreateDTO.dominio())
            .nombre(camionCreateDTO.nombre())
            .telefono(camionCreateDTO.telefono())
            .capPesoKg(camionCreateDTO.capPesoKg())
            .capVolumeM3(camionCreateDTO.capVolumeM3())
            .costoOperacionHora(camionCreateDTO.costoOperacionHora())
            .lsActive(camionCreateDTO.lsActive() != null ? camionCreateDTO.lsActive() : true)
            .build();
        
        // Guardar en base de datos
        Camion camionGuardado = camionRepository.save(camion);
        
        // Convertir Entity → DTO
        return convertToCamionDTO(camionGuardado);
    }

    @Override
    public CamionDTO actualizarCamion(UUID id, CamionUpdateDTO camionUpdateDTO) {
        // Buscar camión existente
        Camion camionExistente = camionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Camión no encontrado con ID: " + id));
        
        // Validar dominio único si se está cambiando
        if (camionUpdateDTO.dominio() != null && !camionUpdateDTO.dominio().equals(camionExistente.getDominio())) {
            if (camionRepository.existsByDominio(camionUpdateDTO.dominio())) {
                throw new RuntimeException("Ya existe un camión con el dominio: " + camionUpdateDTO.dominio());
            }
            camionExistente.setDominio(camionUpdateDTO.dominio());
        }
        
        // Actualizar campos si se proporcionan
        if (camionUpdateDTO.nombre() != null) {
            camionExistente.setNombre(camionUpdateDTO.nombre());
        }
        if (camionUpdateDTO.telefono() != null) {
            camionExistente.setTelefono(camionUpdateDTO.telefono());
        }
        if (camionUpdateDTO.capPesoKg() != null) {
            camionExistente.setCapPesoKg(camionUpdateDTO.capPesoKg());
        }
        if (camionUpdateDTO.capVolumeM3() != null) {
            camionExistente.setCapVolumeM3(camionUpdateDTO.capVolumeM3());
        }
        if (camionUpdateDTO.costoOperacionHora() != null) {
            camionExistente.setCostoOperacionHora(camionUpdateDTO.costoOperacionHora());
        }
        if (camionUpdateDTO.lsActive() != null) {
            camionExistente.setLsActive(camionUpdateDTO.lsActive());
        }
        
        // Guardar cambios (updatedAt se actualiza automáticamente con @PreUpdate)
        Camion camionActualizado = camionRepository.save(camionExistente);
        
        return convertToCamionDTO(camionActualizado);
    }

    @Override
    public DisponibilidadResponseDTO actualizarDisponibilidad(UUID id, DisponibilidadRequestDTO disponibilidadRequest) {
        Camion camion = camionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Camión no encontrado con ID: " + id));
        
        // Actualizar disponibilidad
        camion.setLsActive(disponibilidadRequest.disponible());
        
        // Guardar cambios
        Camion camionActualizado = camionRepository.save(camion);
        
        return new DisponibilidadResponseDTO(
            camionActualizado.getId(),
            camionActualizado.getLsActive(),
            disponibilidadRequest.motivo(),
            LocalDateTime.now()
        );
    }

    @Override
    public CamionDTO obtenerCamionPorId(UUID id) {
        Camion camion = camionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Camión no encontrado con ID: " + id));
        
        return convertToCamionDTO(camion);
    }

    @Override
    public void eliminarCamion(UUID id) {
        Camion camion = camionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Camión no encontrado con ID: " + id));
        
        // Eliminación lógica (no física)
        camion.setLsActive(false);
        camionRepository.save(camion);
    }

    @Override
    public Page<CamionListDTO> consultarDisponibles(LocalDateTime desde, LocalDateTime hasta, BigDecimal capPeso,
            BigDecimal capVol, Pageable pageable) {
        
        // Usar el repository para buscar camiones disponibles por capacidad
        List<Camion> camionesDisponibles;
        
        if (capPeso != null && capVol != null) {
            camionesDisponibles = camionRepository.findCamionesDisponiblesPorCapacidad(capPeso, capVol);
        } else {
            camionesDisponibles = camionRepository.findByLsActive(true);
        }
        
        // TODO: Aquí deberías integrar con el servicio de disponibilidad para verificar
        // que no estén ocupados en el rango de fechas [desde, hasta]
        
        // Convertir a DTOs y paginar
        List<CamionListDTO> camionesDTOs = camionesDisponibles.stream()
            .map(this::convertToCamionListDTO)
            .collect(Collectors.toList());
        
        return convertToPage(camionesDTOs, pageable);
    }

    @Override
    public Page<CamionListDTO> obtenerPagina(Pageable pageable) {
        // Usar paginación nativa de Spring Data JPA
        Page<Camion> camionesPage = camionRepository.findAll(pageable);
        
        // Convertir Page<Entity> a Page<DTO>
        return camionesPage.map(this::convertToCamionListDTO);
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    private CamionDTO convertToCamionDTO(Camion camion) {
        return new CamionDTO(
            camion.getId(),
            camion.getDominio(),
            camion.getNombre(),
            camion.getTelefono(),
            camion.getCapPesoKg(),
            camion.getCapVolumeM3(),
            camion.getCostoOperacionHora(),
            camion.getLsActive(),
            camion.getCreatedAt(),
            camion.getUpdatedAt()
        );
    }

    private CamionListDTO convertToCamionListDTO(Camion camion) {
        return new CamionListDTO(
            camion.getId(),
            camion.getDominio(),
            camion.getNombre(),
            camion.getTelefono(),
            camion.getCapPesoKg(),
            camion.getCapVolumeM3(),
            camion.getLsActive()
        );
    }

    private <T> Page<T> convertToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        
        if (start > list.size()) {
            return new PageImpl<>(List.of(), pageable, list.size());
        }

        List<T> pageContent = list.subList(start, end);
        return new PageImpl<>(pageContent, pageable, list.size());
    }
}