package ar.edu.utnfc.backend.ms_recursos.config;

import ar.edu.utnfc.backend.ms_recursos.model.Camion;
import ar.edu.utnfc.backend.ms_recursos.model.CostoEstimacion;
import ar.edu.utnfc.backend.ms_recursos.model.CostoFinal;
import ar.edu.utnfc.backend.ms_recursos.model.Deposito;
import ar.edu.utnfc.backend.ms_recursos.model.Moneda;
import ar.edu.utnfc.backend.ms_recursos.model.Tarifa;
import ar.edu.utnfc.backend.ms_recursos.model.TarifaCamion;
import ar.edu.utnfc.backend.ms_recursos.model.TarifaDeposito;
import ar.edu.utnfc.backend.ms_recursos.model.UnidadTarifa;
import ar.edu.utnfc.backend.ms_recursos.repository.CamionRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.CostoEstimacionRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.CostoFinalRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.DepositoRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.TarifaCamionRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.TarifaDepositoRepository;
import ar.edu.utnfc.backend.ms_recursos.repository.TarifaRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            CamionRepository camionRepository,
            DepositoRepository depositoRepository,
            TarifaRepository tarifaRepository,
            TarifaCamionRepository tarifaCamionRepository,
            TarifaDepositoRepository tarifaDepositoRepository,
            CostoEstimacionRepository costoEstimacionRepository, // Nuevo
            CostoFinalRepository costoFinalRepository) { // Nuevo

        return args -> {
            initCamiones(camionRepository);
            initDepositos(depositoRepository);
            initTarifas(tarifaRepository);
            initRelacionesTarifas(camionRepository, depositoRepository, tarifaRepository,
                    tarifaCamionRepository, tarifaDepositoRepository);
            initCostosEjemplo(costoEstimacionRepository, costoFinalRepository); // Nuevo
        };
    }

    private void initCamiones(CamionRepository repository) {
        // Verificar si ya existen datos para no duplicar
        if (repository.count() == 0) {
            List<Camion> camionesIniciales = List.of(
                    Camion.builder()
                            .dominio("CAM-001")
                            .nombre("Volvo FH16")
                            .telefono("351-1234567")
                            .capPesoKg(new BigDecimal("25000.00"))
                            .capVolumeM3(new BigDecimal("82.000"))
                            .costoOperacionHora(new BigDecimal("150.00"))
                            .lsActive(true)
                            .build(),

                    Camion.builder()
                            .dominio("CAM-002")
                            .nombre("Mercedes Actros")
                            .telefono("351-2345678")
                            .capPesoKg(new BigDecimal("22000.00"))
                            .capVolumeM3(new BigDecimal("76.000"))
                            .costoOperacionHora(new BigDecimal("140.00"))
                            .lsActive(true)
                            .build(),

                    Camion.builder()
                            .dominio("CAM-003")
                            .nombre("Scania R500")
                            .telefono("351-3456789")
                            .capPesoKg(new BigDecimal("28000.00"))
                            .capVolumeM3(new BigDecimal("85.000"))
                            .costoOperacionHora(new BigDecimal("160.00"))
                            .lsActive(true)
                            .build(),

                    Camion.builder()
                            .dominio("CAM-004")
                            .nombre("Iveco S-Way")
                            .telefono("351-4567890")
                            .capPesoKg(new BigDecimal("20000.00"))
                            .capVolumeM3(new BigDecimal("70.000"))
                            .costoOperacionHora(new BigDecimal("130.00"))
                            .lsActive(true)
                            .build(),

                    Camion.builder()
                            .dominio("CAM-005")
                            .nombre("DAF XF")
                            .telefono("351-5678901")
                            .capPesoKg(new BigDecimal("23000.00"))
                            .capVolumeM3(new BigDecimal("78.000"))
                            .costoOperacionHora(new BigDecimal("145.00"))
                            .lsActive(true)
                            .build(),

                    Camion.builder()
                            .dominio("CAM-006")
                            .nombre("MAN TGX")
                            .telefono("351-6789012")
                            .capPesoKg(new BigDecimal("26000.00"))
                            .capVolumeM3(new BigDecimal("80.000"))
                            .costoOperacionHora(new BigDecimal("155.00"))
                            .lsActive(true)
                            .build(),

                    Camion.builder()
                            .dominio("CAM-007")
                            .nombre("Renault T")
                            .telefono("351-7890123")
                            .capPesoKg(new BigDecimal("21000.00"))
                            .capVolumeM3(new BigDecimal("72.000"))
                            .costoOperacionHora(new BigDecimal("135.00"))
                            .lsActive(true)
                            .build(),

                    Camion.builder()
                            .dominio("CAM-008")
                            .nombre("Freightliner")
                            .telefono("351-8901234")
                            .capPesoKg(new BigDecimal("24000.00"))
                            .capVolumeM3(new BigDecimal("75.000"))
                            .costoOperacionHora(new BigDecimal("142.00"))
                            .lsActive(true)
                            .build()

            );

            repository.saveAll(camionesIniciales);
            System.out.println("Datos iniciales de camiones cargados en la base de datos");
        }
    }

    private void initDepositos(DepositoRepository repository) {
        if (repository.count() == 0) {
            List<Deposito> depositos = List.of(
                    Deposito.builder()
                            .nombre("Depósito Centro")
                            .direccion("Av. Colón 1200, Córdoba")
                            .lat(new BigDecimal("-31.420083"))
                            .lng(new BigDecimal("-64.188776"))
                            .costoDiario(new BigDecimal("850.00"))
                            .lsActive(true)
                            .build(),

                    Deposito.builder()
                            .nombre("Depósito Norte")
                            .direccion("Ruta 9 Km 12, Córdoba")
                            .lat(new BigDecimal("-31.352641"))
                            .lng(new BigDecimal("-64.245689"))
                            .costoDiario(new BigDecimal("720.00"))
                            .lsActive(true)
                            .build(),

                    Deposito.builder()
                            .nombre("Depósito Sur")
                            .direccion("Av. Fuerza Aérea 3500, Córdoba")
                            .lat(new BigDecimal("-31.456123"))
                            .lng(new BigDecimal("-64.212345"))
                            .costoDiario(new BigDecimal("680.00"))
                            .lsActive(true)
                            .build(),

                    Deposito.builder()
                            .nombre("Depósito Este")
                            .direccion("Ruta 19 Km 8, Córdoba")
                            .lat(new BigDecimal("-31.389456"))
                            .lng(new BigDecimal("-64.167890"))
                            .costoDiario(new BigDecimal("790.00"))
                            .lsActive(true)
                            .build(),

                    Deposito.builder()
                            .nombre("Depósito Oeste")
                            .direccion("Av. Armada Argentina 2800, Córdoba")
                            .lat(new BigDecimal("-31.432167"))
                            .lng(new BigDecimal("-64.278901"))
                            .costoDiario(new BigDecimal("710.00"))
                            .lsActive(true)
                            .build());

            repository.saveAll(depositos);
            System.out.println("Datos iniciales de depósitos cargados en la base de datos");
        }
    }

    private void initTarifas(TarifaRepository tarifaRepository) {
        if (tarifaRepository.count() == 0) {
            List<Tarifa> tarifas = List.of(
                    Tarifa.builder()
                            .description("Tarifa Estándar Peso")
                            .unidad(UnidadTarifa.POR_KILO)
                            .valor(new BigDecimal("0.85"))
                            .moneda(Moneda.ARS)
                            .vigenciaHasta(LocalDate.of(2024, 12, 31))
                            .version(1)
                            .build(),

                    Tarifa.builder()
                            .description("Tarifa Estándar Volumen")
                            .unidad(UnidadTarifa.POR_METRO_CUBICO)
                            .valor(new BigDecimal("1250.50"))
                            .moneda(Moneda.ARS)
                            .vigenciaHasta(LocalDate.of(2024, 12, 31))
                            .version(1)
                            .build(),

                    Tarifa.builder()
                            .description("Tarifa Express Peso")
                            .unidad(UnidadTarifa.POR_KILO)
                            .valor(new BigDecimal("1.25"))
                            .moneda(Moneda.ARS)
                            .vigenciaHasta(LocalDate.of(2024, 12, 31))
                            .version(1)
                            .build(),

                    Tarifa.builder()
                            .description("Tarifa Express Volumen")
                            .unidad(UnidadTarifa.POR_METRO_CUBICO)
                            .valor(new BigDecimal("1850.75"))
                            .moneda(Moneda.ARS)
                            .vigenciaHasta(LocalDate.of(2024, 12, 31))
                            .version(1)
                            .build(),

                    Tarifa.builder()
                            .description("Tarifa Nocturna")
                            .unidad(UnidadTarifa.POR_HORA)
                            .valor(new BigDecimal("180.00"))
                            .moneda(Moneda.ARS)
                            .vigenciaHasta(LocalDate.of(2024, 12, 31))
                            .version(1)
                            .build(),

                    Tarifa.builder()
                            .description("Tarifa Fin de Semana")
                            .unidad(UnidadTarifa.POR_DIA)
                            .valor(new BigDecimal("2500.00"))
                            .moneda(Moneda.ARS)
                            .vigenciaHasta(LocalDate.of(2024, 12, 31))
                            .version(1)
                            .build());

            tarifaRepository.saveAll(tarifas);
            System.out.println("Datos iniciales de tarifas cargados en la base de datos");
        }
    }

    private void initRelacionesTarifas(
            CamionRepository camionRepository,
            DepositoRepository depositoRepository,
            TarifaRepository tarifaRepository,
            TarifaCamionRepository tarifaCamionRepository,
            TarifaDepositoRepository tarifaDepositoRepository) {

        if (tarifaCamionRepository.count() == 0 && tarifaDepositoRepository.count() == 0) {
            List<Camion> camiones = camionRepository.findAll();
            List<Deposito> depositos = depositoRepository.findAll();
            List<Tarifa> tarifas = tarifaRepository.findAll();

            // Asignar tarifas a camiones (primeras 5 tarifas a primeros 5 camiones)
            for (int i = 0; i < Math.min(tarifas.size(), camiones.size()); i++) {
                TarifaCamion tarifaCamion = TarifaCamion.builder()
                        .tarifaId(tarifas.get(i).getId())
                        .camionId(camiones.get(i).getId())
                        .build();
                tarifaCamionRepository.save(tarifaCamion);
            }

            // Asignar tarifas a depósitos (primeras 5 tarifas a primeros 5 depósitos)
            for (int i = 0; i < Math.min(tarifas.size(), depositos.size()); i++) {
                TarifaDeposito tarifaDeposito = TarifaDeposito.builder()
                        .tarifaId(tarifas.get(i).getId())
                        .depositoId(depositos.get(i).getId())
                        .build();
                tarifaDepositoRepository.save(tarifaDeposito);
            }

            System.out.println("Relaciones de tarifas cargadas en la base de datos");
        }
    }
    // En DataInitializer.java, agregar estos métodos:

    private void initCostosEjemplo(
            CostoEstimacionRepository costoEstimacionRepository,
            CostoFinalRepository costoFinalRepository) {

        // Solo crear datos de ejemplo si no existen
        if (costoEstimacionRepository.count() == 0 && costoFinalRepository.count() == 0) {

            // Crear algunas estimaciones de ejemplo
            List<CostoEstimacion> estimaciones = List.of(
                    CostoEstimacion.builder()
                            .solicitudRef(UUID.randomUUID().toString())
                            .rutaPayipro(
                                    "{\"ruta\": \"RUTA-001\", \"origen\": \"Córdoba\", \"destino\": \"Buenos Aires\"}")
                            .totalEstimado(new BigDecimal("12500.50"))
                            .moneda(Moneda.ARS)
                            .despose("{\"combustible\": 4500.00, \"peajes\": 800.00, \"otros\": 7200.50}")
                            .build(),

                    CostoEstimacion.builder()
                            .solicitudRef(UUID.randomUUID().toString())
                            .rutaPayipro("{\"ruta\": \"RUTA-002\", \"origen\": \"Córdoba\", \"destino\": \"Rosario\"}")
                            .totalEstimado(new BigDecimal("8500.75"))
                            .moneda(Moneda.ARS)
                            .despose("{\"combustible\": 3200.00, \"peajes\": 500.00, \"otros\": 4800.75}")
                            .build());

            // Crear algunos costos finales de ejemplo
            List<CostoFinal> costosFinales = List.of(
                    CostoFinal.builder()
                            .rutaRef("RUTA-001-FINAL")
                            .litros(new BigDecimal("350.50"))
                            .estadiasDias(1)
                            .totalFinal(new BigDecimal("13200.25"))
                            .moneda(Moneda.ARS)
                            .desposeJson(
                                    "{\"combustible_real\": 5200.00, \"peajes_extra\": 200.00, \"dias_extra\": 800.25}")
                            .build(),

                    CostoFinal.builder()
                            .rutaRef("RUTA-002-FINAL")
                            .litros(new BigDecimal("280.75"))
                            .estadiasDias(0)
                            .totalFinal(new BigDecimal("8200.00"))
                            .moneda(Moneda.ARS)
                            .desposeJson("{\"combustible_real\": 3000.00, \"ahorro_combustible\": 200.00}")
                            .build());

            costoEstimacionRepository.saveAll(estimaciones);
            costoFinalRepository.saveAll(costosFinales);

            System.out.println("Datos de ejemplo de costos cargados en la base de datos");
        }
    }

}