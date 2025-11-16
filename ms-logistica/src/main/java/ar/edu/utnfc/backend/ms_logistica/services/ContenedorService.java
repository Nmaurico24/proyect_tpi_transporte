package ar.edu.utnfc.backend.ms_logistica.services;

import ar.edu.utnfc.backend.ms_logistica.exceptions.NotFoundException;
import ar.edu.utnfc.backend.ms_logistica.models.Cliente;
import ar.edu.utnfc.backend.ms_logistica.models.Contenedor;
import ar.edu.utnfc.backend.ms_logistica.repositories.ClienteRepository;
import ar.edu.utnfc.backend.ms_logistica.repositories.ContenedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;
    private final ClienteRepository clienteRepository;

    public ContenedorService(ContenedorRepository contenedorRepository,
            ClienteRepository clienteRepository) {
        this.contenedorRepository = contenedorRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Contenedor> listarContenedores(UUID clienteId) {
        if (clienteId == null) {
            return contenedorRepository.findAll();
        }
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return contenedorRepository.findByCliente(cliente);
    }
}
