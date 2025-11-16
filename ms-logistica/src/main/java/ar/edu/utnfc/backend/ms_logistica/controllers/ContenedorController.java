package ar.edu.utnfc.backend.ms_logistica.controllers;

import ar.edu.utnfc.backend.ms_logistica.models.Contenedor;
import ar.edu.utnfc.backend.ms_logistica.services.ContenedorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contenedores")
public class ContenedorController {

    private final ContenedorService contenedorService;

    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    @GetMapping
    public List<Contenedor> listar(@RequestParam(required = false) UUID clienteId) {
        return contenedorService.listarContenedores(clienteId);
    }
}
