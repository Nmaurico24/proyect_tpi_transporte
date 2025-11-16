package ar.edu.utnfc.backend.ms_recursos.services;

import ar.edu.utnfc.backend.ms_recursos.exceptions.NotFoundException;
import ar.edu.utnfc.backend.ms_recursos.models.Deposito;
import ar.edu.utnfc.backend.ms_recursos.repositories.DepositoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositoService {
    private final DepositoRepository repo;

    public DepositoService(DepositoRepository repo) {
        this.repo = repo;
    }

    public Deposito crear(Deposito d) {
        return repo.save(d);
    }

    public Deposito actualizar(Long id, Deposito d) {
        Deposito db = repo.findById(id).orElseThrow(() -> new NotFoundException("Deposito " + id));
        db.setNombre(d.getNombre());
        db.setLat(d.getLat());
        db.setLng(d.getLng());
        db.setCapacidadContenedores(d.getCapacidadContenedores());
        return repo.save(db);
    }

    public List<Deposito> listar() {
        return repo.findAll();
    }

    public Deposito obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Deposito " + id));
    }
}
