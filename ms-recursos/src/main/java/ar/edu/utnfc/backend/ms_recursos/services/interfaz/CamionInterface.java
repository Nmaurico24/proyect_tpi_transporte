package ar.edu.utnfc.backend.ms_recursos.services.interfaz;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ar.edu.utnfc.backend.ms_recursos.dto.*;

public interface CamionInterface {
    
    CamionDTO crearCamion(CamionCreateDTO camionCreateDTO);
    
    CamionDTO actualizarCamion(UUID id, CamionUpdateDTO camionUpdateDTO);
    
    DisponibilidadResponseDTO actualizarDisponibilidad(UUID id, DisponibilidadRequestDTO disponibilidadRequest);
    
    Page<CamionListDTO> consultarDisponibles(LocalDateTime desde, LocalDateTime hasta, BigDecimal capPeso, BigDecimal capVol, Pageable pageable);
    
    CamionDTO obtenerCamionPorId(UUID id);

    Page<CamionListDTO> obtenerPagina(Pageable pageable);
    
    void eliminarCamion(UUID id);
}
