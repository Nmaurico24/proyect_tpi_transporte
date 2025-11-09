package ar.edu.utnfc.backend.ms_recursos.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import ar.edu.utnfc.backend.ms_recursos.model.*;
import jakarta.annotation.PostConstruct;

@Component
public class RecursosStore {
    private final Map<UUID, CamionDTO> camionesData = new ConcurrentHashMap<>();
    private final Map<UUID, DepositoDTO> depositosData = new ConcurrentHashMap<>();
    private final Map<UUID, TarifaDTO> tarifasData = new ConcurrentHashMap<>();
    private final Map<UUID, TarifaCamionDTO> tarifaCamionData = new ConcurrentHashMap<>();
    private final Map<UUID, TarifaDepositoDTO> tarifaDepositoData = new ConcurrentHashMap<>();

    // ----- Datos de prueba para Camiones -----
    private static final List<String[]> CAMIONES_DATA = List.of(
        // dominio, nombre, telefono, cap_peso_kg, cap_volume_m3, costo_operacion_hora
        new String[]{"CAM-001", "Volvo FH16", "351-1234567", "25000", "82", "150.00"},
        new String[]{"CAM-002", "Mercedes Actros", "351-2345678", "22000", "76", "140.00"},
        new String[]{"CAM-003", "Scania R500", "351-3456789", "28000", "85", "160.00"},
        new String[]{"CAM-004", "Iveco S-Way", "351-4567890", "20000", "70", "130.00"},
        new String[]{"CAM-005", "DAF XF", "351-5678901", "23000", "78", "145.00"},
        new String[]{"CAM-006", "MAN TGX", "351-6789012", "26000", "80", "155.00"},
        new String[]{"CAM-007", "Renault T", "351-7890123", "21000", "72", "135.00"},
        new String[]{"CAM-008", "Freightliner", "351-8901234", "24000", "75", "142.00"}
    );

    // ----- Datos de prueba para Depósitos -----
    private static final List<String[]> DEPOSITOS_DATA = List.of(
        // nombre, direccion, lat, lng, costo_diario
        new String[]{"Depósito Centro", "Av. Colón 1200, Córdoba", "-31.420083", "-64.188776", "850.00"},
        new String[]{"Depósito Norte", "Ruta 9 Km 12, Córdoba", "-31.352641", "-64.245689", "720.00"},
        new String[]{"Depósito Sur", "Av. Fuerza Aérea 3500, Córdoba", "-31.456123", "-64.212345", "680.00"},
        new String[]{"Depósito Este", "Ruta 19 Km 8, Córdoba", "-31.389456", "-64.167890", "790.00"},
        new String[]{"Depósito Oeste", "Av. Armada Argentina 2800, Córdoba", "-31.432167", "-64.278901", "710.00"}
    );

    // ----- Datos de prueba para Tarifas -----
    private static final List<String[]> TARIFAS_DATA = List.of(
        // description, unidad, valor, moneda, vigencia_basta, version
        new String[]{"Tarifa Estándar Peso", "POR_KILO", "0.85", "ARS", "2024-12-31", "1"},
        new String[]{"Tarifa Estándar Volumen", "POR_METRO_CUBICO", "1250.50", "ARS", "2024-12-31", "1"},
        new String[]{"Tarifa Express Peso", "POR_KILO", "1.25", "ARS", "2024-12-31", "1"},
        new String[]{"Tarifa Express Volumen", "POR_METRO_CUBICO", "1850.75", "ARS", "2024-12-31", "1"},
        new String[]{"Tarifa Nocturna", "POR_HORA", "180.00", "ARS", "2024-12-31", "1"},
        new String[]{"Tarifa Fin de Semana", "POR_DIA", "2500.00", "ARS", "2024-12-31", "1"}
    );

    @PostConstruct
    void init() {
        seedCamiones();
        seedDepositos();
        seedTarifas();
        seedTarifasCamiones();
        seedTarifasDepositos();
    }

    private void seedCamiones() {
        LocalDateTime now = LocalDateTime.now();
        for (String[] camionData : CAMIONES_DATA) {
            CamionDTO camion = new CamionDTO();
            camion.setId(UUID.randomUUID());
            camion.setDominio(camionData[0]);
            camion.setNombre(camionData[1]);
            camion.setTelefono(camionData[2]);
            camion.setCapPesoKg(new BigDecimal(camionData[3]));
            camion.setCapVolumeM3(new BigDecimal(camionData[4]));
            camion.setCostoOperacionHora(new BigDecimal(camionData[5]));
            camion.setLsActive(true);
            camion.setCreatedAt(now);
            camion.setUpdatedAt(now);
            
            camionesData.put(camion.getId(), camion);
        }
    }

    private void seedDepositos() {
        LocalDateTime now = LocalDateTime.now();
        for (String[] depositoData : DEPOSITOS_DATA) {
            DepositoDTO deposito = new DepositoDTO();
            deposito.setId(UUID.randomUUID());
            deposito.setNombre(depositoData[0]);
            deposito.setDireccion(depositoData[1]);
            deposito.setLat(new BigDecimal(depositoData[2]));
            deposito.setLng(new BigDecimal(depositoData[3]));
            deposito.setCostoDiario(new BigDecimal(depositoData[4]));
            deposito.setLsActive(true);
            deposito.setCreatedAt(now);
            deposito.setUpdatedAt(now);
            
            depositosData.put(deposito.getId(), deposito);
        }
    }

    private void seedTarifas() {
        LocalDateTime now = LocalDateTime.now();
        for (String[] tarifaData : TARIFAS_DATA) {
            TarifaDTO tarifa = new TarifaDTO();
            tarifa.setId(UUID.randomUUID());
            tarifa.setDescription(tarifaData[0]);
            tarifa.setUnidad(UnidadTarifa.valueOf(tarifaData[1]));
            tarifa.setValor(new BigDecimal(tarifaData[2]));
            tarifa.setMoneda(Moneda.valueOf(tarifaData[3]));
            tarifa.setVigenciaBasta(java.time.LocalDate.parse(tarifaData[4]));
            tarifa.setVersion(Integer.parseInt(tarifaData[5]));
            tarifa.setCreatedAt(now);
            tarifa.setUpdatedAt(now);
            
            tarifasData.put(tarifa.getId(), tarifa);
        }
    }

    private void seedTarifasCamiones() {
        // Asignar tarifas a camiones de forma aleatoria
        List<UUID> camionIds = new ArrayList<>(camionesData.keySet());
        List<UUID> tarifaIds = new ArrayList<>(tarifasData.keySet());
        
        Random random = new Random();
        for (int i = 0; i < Math.min(camionIds.size(), tarifaIds.size()); i++) {
            TarifaCamionDTO tarifaCamion = new TarifaCamionDTO();
            tarifaCamion.setId(UUID.randomUUID());
            tarifaCamion.setTarifaId(tarifaIds.get(i));
            tarifaCamion.setCamionId(camionIds.get(i));
            
            tarifaCamionData.put(tarifaCamion.getId(), tarifaCamion);
        }
    }

    private void seedTarifasDepositos() {
        // Asignar tarifas a depósitos de forma aleatoria
        List<UUID> depositoIds = new ArrayList<>(depositosData.keySet());
        List<UUID> tarifaIds = new ArrayList<>(tarifasData.keySet());
        
        Random random = new Random();
        for (int i = 0; i < Math.min(depositoIds.size(), tarifaIds.size()); i++) {
            TarifaDepositoDTO tarifaDeposito = new TarifaDepositoDTO();
            tarifaDeposito.setId(UUID.randomUUID());
            tarifaDeposito.setTarifaId(tarifaIds.get(i));
            tarifaDeposito.setDepositoId(depositoIds.get(i));
            
            tarifaDepositoData.put(tarifaDeposito.getId(), tarifaDeposito);
        }
    }

    // ------------------- Métodos de acceso a datos -------------------

    public List<CamionDTO> findAllCamiones(Boolean active) {
        return camionesData.values().stream()
                .filter(c -> active == null || c.getLsActive().equals(active))
                .sorted(Comparator.comparing(CamionDTO::getDominio))
                .toList();
    }

    public Optional<CamionDTO> findCamionById(UUID id) {
        return Optional.ofNullable(camionesData.get(id));
    }

    public List<DepositoDTO> findAllDepositos(Boolean active) {
        return depositosData.values().stream()
                .filter(d -> active == null || d.getLsActive().equals(active))
                .sorted(Comparator.comparing(DepositoDTO::getNombre))
                .toList();
    }

    public Optional<DepositoDTO> findDepositoById(UUID id) {
        return Optional.ofNullable(depositosData.get(id));
    }

    public List<TarifaDTO> findAllTarifas() {
        return new ArrayList<>(tarifasData.values());
    }

    public Optional<TarifaDTO> findTarifaById(UUID id) {
        return Optional.ofNullable(tarifasData.get(id));
    }

    public List<TarifaCamionDTO> findTarifasByCamion(UUID camionId) {
        return tarifaCamionData.values().stream()
                .filter(tc -> tc.getCamionId().equals(camionId))
                .toList();
    }

    public List<TarifaDepositoDTO> findTarifasByDeposito(UUID depositoId) {
        return tarifaDepositoData.values().stream()
                .filter(td -> td.getDepositoId().equals(depositoId))
                .toList();
    }

    // Métodos para guardar nuevos datos
    public void saveCamion(CamionDTO camion) {
        if (camion.getId() == null) {
            camion.setId(UUID.randomUUID());
        }
        camion.setUpdatedAt(LocalDateTime.now());
        camionesData.put(camion.getId(), camion);
    }

    public void saveDeposito(DepositoDTO deposito) {
        if (deposito.getId() == null) {
            deposito.setId(UUID.randomUUID());
        }
        deposito.setUpdatedAt(LocalDateTime.now());
        depositosData.put(deposito.getId(), deposito);
    }

    public void saveTarifa(TarifaDTO tarifa) {
        if (tarifa.getId() == null) {
            tarifa.setId(UUID.randomUUID());
        }
        tarifa.setUpdatedAt(LocalDateTime.now());
        tarifasData.put(tarifa.getId(), tarifa);
    }

}
