package ar.edu.utnfc.backend.ms_logistica.dto;

import jakarta.validation.constraints.Email;

public record ClienteUpdateDTO(
    String nombre,
    String telefono,
    @Email String email,
    Boolean isActive
) {}