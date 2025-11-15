package ar.edu.utnfc.backend.ms_recursos.config;

import ar.edu.utnfc.backend.ms_recursos.models.Camion;
import ar.edu.utnfc.backend.ms_recursos.models.Deposito;
import ar.edu.utnfc.backend.ms_recursos.models.TarifaRegla;
import ar.edu.utnfc.backend.ms_recursos.repositories.CamionRepository;
import ar.edu.utnfc.backend.ms_recursos.repositories.DepositoRepository;
import ar.edu.utnfc.backend.ms_recursos.repositories.TarifaReglaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedRecursosData(
            CamionRepository camionRepository,
            DepositoRepository depositoRepository,
            TarifaReglaRepository tarifaReglaRepository
    ) {
        return args -> {
            seedCamiones(camionRepository);
            seedDepositos(depositoRepository);
            seedTarifas(tarifaReglaRepository);
        };
    }

    private void seedCamiones(CamionRepository repository) {
        if (repository.count() > 0) {
            return;
        }

        var camiones = List.of(
                Camion.builder().patente("CAM-001").capPesoKg(25000d).capVolM3(82d).disponible(true).build(),
                Camion.builder().patente("CAM-002").capPesoKg(22000d).capVolM3(76d).disponible(true).build(),
                Camion.builder().patente("CAM-003").capPesoKg(28000d).capVolM3(85d).disponible(true).build(),
                Camion.builder().patente("CAM-004").capPesoKg(20000d).capVolM3(70d).disponible(true).build(),
                Camion.builder().patente("CAM-005").capPesoKg(23000d).capVolM3(78d).disponible(true).build(),
                Camion.builder().patente("CAM-006").capPesoKg(26000d).capVolM3(80d).disponible(true).build(),
                Camion.builder().patente("CAM-007").capPesoKg(21000d).capVolM3(72d).disponible(true).build(),
                Camion.builder().patente("CAM-008").capPesoKg(24000d).capVolM3(75d).disponible(true).build()
        );

        repository.saveAll(camiones);
    }

    private void seedDepositos(DepositoRepository repository) {
        if (repository.count() > 0) {
            return;
        }

        var depositos = List.of(
                Deposito.builder().nombre("Depósito Centro").lat(-31.420083).lng(-64.188776).capacidadContenedores(120).build(),
                Deposito.builder().nombre("Depósito Norte").lat(-31.352641).lng(-64.245689).capacidadContenedores(80).build(),
                Deposito.builder().nombre("Depósito Sur").lat(-31.456123).lng(-64.212345).capacidadContenedores(95).build(),
                Deposito.builder().nombre("Depósito Este").lat(-31.389456).lng(-64.167890).capacidadContenedores(70).build(),
                Deposito.builder().nombre("Depósito Oeste").lat(-31.432167).lng(-64.278901).capacidadContenedores(110).build()
        );

        repository.saveAll(depositos);
    }

    private void seedTarifas(TarifaReglaRepository repository) {
        if (repository.count() > 0) {
            return;
        }

        var reglas = List.of(
                TarifaRegla.builder()
                        .precioKm(950d)
                        .factorPeso(40d)
                        .factorVolumen(25d)
                        .recargoEstadia(450d)
                        .build(),
                TarifaRegla.builder()
                        .precioKm(1150d)
                        .factorPeso(55d)
                        .factorVolumen(35d)
                        .recargoEstadia(600d)
                        .build()
        );

        repository.saveAll(reglas);
    }
}
