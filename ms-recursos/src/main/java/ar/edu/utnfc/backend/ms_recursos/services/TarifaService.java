package ar.edu.utnfc.backend.ms_recursos.services;

import ar.edu.utnfc.backend.ms_recursos.dto.CostoEstimacionRequest;
import ar.edu.utnfc.backend.ms_recursos.dto.CostoEstimacionResponse;
import ar.edu.utnfc.backend.ms_recursos.dto.CostoFinalRequest;
import ar.edu.utnfc.backend.ms_recursos.dto.CostoFinalResponse;
import ar.edu.utnfc.backend.ms_recursos.models.TarifaRegla;
import ar.edu.utnfc.backend.ms_recursos.repositories.TarifaReglaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaService {

    private final TarifaReglaRepository repo;

    public TarifaService(TarifaReglaRepository repo) {
        this.repo = repo;
    }

    public TarifaRegla guardarRegla(TarifaRegla r) {
        return repo.save(r);
    }

    public List<TarifaRegla> listarReglas() {
        return repo.findAll();
    }

    public CostoEstimacionResponse estimar(CostoEstimacionRequest req) {
        // Estrategia simple: usar la primera regla disponible
        var regla = repo.findAll().stream().findFirst().orElseGet(
                () -> TarifaRegla.builder()
                        .precioKm(1000.0).factorPeso(50.0).factorVolumen(30.0).recargoEstadia(500.0)
                        .build());

        double baseKm = req.getKmEstimados() * regla.getPrecioKm();
        double extraPeso = (req.getPesoKg() / 1000.0) * regla.getFactorPeso(); // por tonelada
        double extraVol = req.getVolumenM3() * regla.getFactorVolumen();

        double total = baseKm + extraPeso + extraVol;

        return CostoEstimacionResponse.builder()
                .km(req.getKmEstimados())
                .precioKm(regla.getPrecioKm())
                .detallePeso(extraPeso)
                .detalleVolumen(extraVol)
                .totalEstimado(total)
                .build();
    }

    public CostoFinalResponse costoFinal(CostoFinalRequest req) {
        var regla = repo.findAll().stream().findFirst().orElseGet(
                () -> TarifaRegla.builder()
                        .precioKm(1000.0).factorPeso(50.0).factorVolumen(30.0).recargoEstadia(500.0)
                        .build());

        double baseKm = req.getKmReales() * regla.getPrecioKm();
        double combustible = req.getLitrosConsumidos() * 0.0; // si querés agregar costo/lt, suma otra regla
        double estadias = req.getHorasEstadia() * regla.getRecargoEstadia();

        double total = baseKm + combustible + estadias;

        return CostoFinalResponse.builder()
                .kmReales(req.getKmReales())
                .precioKm(regla.getPrecioKm())
                .litros(req.getLitrosConsumidos())
                .horasEstadia(req.getHorasEstadia())
                .recargoEstadia(regla.getRecargoEstadia())
                .totalFinal(total)
                .build();
    }
}
