package ar.edu.utnfc.backend.ms_logistica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteCreateDTO(
    @NotBlank String numero,
    @NotBlank String nombre,
    String telefono,
    @Email String email,
    Boolean isActive
) {}