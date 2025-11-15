package ar.edu.utnfc.backend.ms_recursos.services;

import ar.edu.utnfc.backend.ms_recursos.exceptions.NotFoundException;
import ar.edu.utnfc.backend.ms_recursos.models.Camion;
import ar.edu.utnfc.backend.ms_recursos.repositories.CamionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamionService {
    private final CamionRepository repo;

    public CamionService(CamionRepository repo) {
        this.repo = repo;
    }

    public Camion crear(Camion c) {
        return repo.save(c);
    }

    public Camion actualizar(Long id, Camion c) {
        Camion db = repo.findById(id).orElseThrow(() -> new NotFoundException("Camion " + id));
        db.setPatente(c.getPatente());
        db.setCapPesoKg(c.getCapPesoKg());
        db.setCapVolM3(c.getCapVolM3());
        db.setDisponible(c.getDisponible());
        return repo.save(db);
    }

    public List<Camion> listar() {
        return repo.findAll();
    }

    public List<Camion> disponibles() {
        return repo.findByDisponibleTrue();
    }

    public Camion disponibilidad(Long id, boolean disponible) {
        Camion db = repo.findById(id).orElseThrow(() -> new NotFoundException("Camion " + id));
        db.setDisponible(disponible);
        return repo.save(db);
    }
}
