package ar.edu.utnfc.backend.ms_logistica.config;

import ar.edu.utnfc.backend.ms_logistica.model.*;
import ar.edu.utnfc.backend.ms_logistica.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            ClienteRepository clienteRepository,
            SolicitudRepository solicitudRepository,
            ContenedorRepository contenedorRepository,
            SolicitudContenedorRepository solicitudContenedorRepository,
            EstimadaRepository estimadaRepository,
            SolicitudEventoRepository solicitudEventoRepository) {
        
        return args -> {
            initClientes(clienteRepository);
            initContenedores(contenedorRepository, clienteRepository);
            initSolicitudes(solicitudRepository, clienteRepository);
            initSolicitudContenedores(solicitudContenedorRepository, solicitudRepository, contenedorRepository);
            initEstimadas(estimadaRepository, solicitudRepository);
            initSolicitudEventos(solicitudEventoRepository, solicitudRepository);
        };
    }

    private void initClientes(ClienteRepository repository) {
        if (repository.count() == 0) {
            List<Cliente> clientes = List.of(
                Cliente.builder()
                    .numero("CLI-001")
                    .nombre("Empresa Logística S.A.")
                    .telefono("351-1234567")
                    .email("contacto@empresalogistica.com")
                    .isActive(true)
                    .build(),

                Cliente.builder()
                    .numero("CLI-002")
                    .nombre("Distribuidora Norte")
                    .telefono("351-2345678")
                    .email("ventas@distribuidoranorte.com")
                    .isActive(true)
                    .build(),

                Cliente.builder()
                    .numero("CLI-003")
                    .nombre("Comercial del Sur")
                    .telefono("351-3456789")
                    .email("info@comercialsur.com")
                    .isActive(true)
                    .build(),

                Cliente.builder()
                    .numero("CLI-004")
                    .nombre("Importadora Este")
                    .telefono("351-4567890")
                    .email("administracion@importadoraeste.com")
                    .isActive(true)
                    .build(),

                Cliente.builder()
                    .numero("CLI-005")
                    .nombre("Exportadora Oeste")
                    .telefono("351-5678901")
                    .email("logistica@exportadoraoeste.com")
                    .isActive(true)
                    .build()
            );

            repository.saveAll(clientes);
            System.out.println("Datos iniciales de clientes cargados en la base de datos");
        }
    }

    private void initContenedores(ContenedorRepository repository, ClienteRepository clienteRepository) {
        if (repository.count() == 0) {
            List<Cliente> clientes = clienteRepository.findAll();
            
            List<Contenedor> contenedores = List.of(
                // Contenedores para Cliente 1
                Contenedor.builder()
                    .clienteId(clientes.get(0).getId())
                    .etiqueta("CONT-001-001")
                    .pesoKg(new BigDecimal("5000.00"))
                    .volumenM3(new BigDecimal("25.000"))
                    .estado(EstadoContenedor.DISPONIBLE)
                    .isActive(true)
                    .build(),

                Contenedor.builder()
                    .clienteId(clientes.get(0).getId())
                    .etiqueta("CONT-001-002")
                    .pesoKg(new BigDecimal("7500.00"))
                    .volumenM3(new BigDecimal("32.500"))
                    .estado(EstadoContenedor.DISPONIBLE)
                    .isActive(true)
                    .build(),

                // Contenedores para Cliente 2
                Contenedor.builder()
                    .clienteId(clientes.get(1).getId())
                    .etiqueta("CONT-002-001")
                    .pesoKg(new BigDecimal("3000.00"))
                    .volumenM3(new BigDecimal("18.000"))
                    .estado(EstadoContenedor.DISPONIBLE)
                    .isActive(true)
                    .build(),

                Contenedor.builder()
                    .clienteId(clientes.get(1).getId())
                    .etiqueta("CONT-002-002")
                    .pesoKg(new BigDecimal("6000.00"))
                    .volumenM3(new BigDecimal("28.000"))
                    .estado(EstadoContenedor.ASIGNADO)
                    .isActive(true)
                    .build(),

                // Contenedores para Cliente 3
                Contenedor.builder()
                    .clienteId(clientes.get(2).getId())
                    .etiqueta("CONT-003-001")
                    .pesoKg(new BigDecimal("4000.00"))
                    .volumenM3(new BigDecimal("22.000"))
                    .estado(EstadoContenedor.EN_TRANSITO)
                    .isActive(true)
                    .build(),

                Contenedor.builder()
                    .clienteId(clientes.get(2).getId())
                    .etiqueta("CONT-003-002")
                    .pesoKg(new BigDecimal("5500.00"))
                    .volumenM3(new BigDecimal("30.000"))
                    .estado(EstadoContenedor.DISPONIBLE)
                    .isActive(true)
                    .build(),

                // Contenedores para Cliente 4
                Contenedor.builder()
                    .clienteId(clientes.get(3).getId())
                    .etiqueta("CONT-004-001")
                    .pesoKg(new BigDecimal("8000.00"))
                    .volumenM3(new BigDecimal("35.000"))
                    .estado(EstadoContenedor.MANTENIMIENTO)
                    .isActive(true)
                    .build(),

                // Contenedores para Cliente 5
                Contenedor.builder()
                    .clienteId(clientes.get(4).getId())
                    .etiqueta("CONT-005-001")
                    .pesoKg(new BigDecimal("2500.00"))
                    .volumenM3(new BigDecimal("15.000"))
                    .estado(EstadoContenedor.DISPONIBLE)
                    .isActive(true)
                    .build()
            );

            repository.saveAll(contenedores);
            System.out.println("Datos iniciales de contenedores cargados en la base de datos");
        }
    }

    private void initSolicitudes(SolicitudRepository repository, ClienteRepository clienteRepository) {
        if (repository.count() == 0) {
            List<Cliente> clientes = clienteRepository.findAll();
            
            List<Solicitud> solicitudes = List.of(
                // Solicitudes para Cliente 1
                Solicitud.builder()
                    .clienteId(clientes.get(0).getId())
                    .estado(EstadoSolicitud.PROGRAMADA)
                    .prioridad(1)
                    .origenDireccion("Av. Colón 1200, Córdoba")
                    .origenLat(new BigDecimal("-31.420083"))
                    .origenLng(new BigDecimal("-64.188776"))
                    .destinoDireccion("Av. Corrientes 123, Buenos Aires")
                    .destinoLat(new BigDecimal("-34.603722"))
                    .destinoLng(new BigDecimal("-58.381592"))
                    .costoEstimado(new BigDecimal("12500.50"))
                    .distanciaEstimadaKm(new BigDecimal("700.50"))
                    .duracionEstimadaMin(480)
                    .rutaRef("RUTA-CBA-BSAS-001")
                    .build(),

                Solicitud.builder()
                    .clienteId(clientes.get(0).getId())
                    .estado(EstadoSolicitud.EN_TRANSITO)
                    .prioridad(2)
                    .origenDireccion("Av. Colón 1200, Córdoba")
                    .origenLat(new BigDecimal("-31.420083"))
                    .origenLng(new BigDecimal("-64.188776"))
                    .destinoDireccion("Av. San Martín 456, Rosario")
                    .destinoLat(new BigDecimal("-32.946820"))
                    .destinoLng(new BigDecimal("-60.639320"))
                    .costoEstimado(new BigDecimal("8500.75"))
                    .costoFinal(new BigDecimal("8200.00"))
                    .distanciaEstimadaKm(new BigDecimal("400.25"))
                    .duracionEstimadaMin(300)
                    .duracionRealMin(285)
                    .rutaRef("RUTA-CBA-ROS-001")
                    .build(),

                // Solicitudes para Cliente 2
                Solicitud.builder()
                    .clienteId(clientes.get(1).getId())
                    .estado(EstadoSolicitud.BORRADOR)
                    .prioridad(3)
                    .origenDireccion("Ruta 9 Km 12, Córdoba")
                    .origenLat(new BigDecimal("-31.352641"))
                    .origenLng(new BigDecimal("-64.245689"))
                    .destinoDireccion("Av. Belgrano 789, Mendoza")
                    .destinoLat(new BigDecimal("-32.890840"))
                    .destinoLng(new BigDecimal("-68.827170"))
                    .costoEstimado(new BigDecimal("9500.25"))
                    .distanciaEstimadaKm(new BigDecimal("600.75"))
                    .duracionEstimadaMin(420)
                    .rutaRef("RUTA-CBA-MDZ-001")
                    .build(),

                // Solicitudes para Cliente 3
                Solicitud.builder()
                    .clienteId(clientes.get(2).getId())
                    .estado(EstadoSolicitud.ENTREGADA)
                    .prioridad(1)
                    .origenDireccion("Av. Fuerza Aérea 3500, Córdoba")
                    .origenLat(new BigDecimal("-31.456123"))
                    .origenLng(new BigDecimal("-64.212345"))
                    .destinoDireccion("Puerto Madero, Buenos Aires")
                    .destinoLat(new BigDecimal("-34.611780"))
                    .destinoLng(new BigDecimal("-58.362030"))
                    .costoEstimado(new BigDecimal("11000.00"))
                    .costoFinal(new BigDecimal("10850.50"))
                    .distanciaEstimadaKm(new BigDecimal("650.00"))
                    .duracionEstimadaMin(450)
                    .duracionRealMin(465)
                    .rutaRef("RUTA-CBA-PM-001")
                    .build(),

                // Solicitudes para Cliente 4
                Solicitud.builder()
                    .clienteId(clientes.get(3).getId())
                    .estado(EstadoSolicitud.PROGRAMADA)
                    .prioridad(2)
                    .origenDireccion("Ruta 19 Km 8, Córdoba")
                    .origenLat(new BigDecimal("-31.389456"))
                    .origenLng(new BigDecimal("-64.167890"))
                    .destinoDireccion("Av. Circunvalación 123, Santa Fe")
                    .destinoLat(new BigDecimal("-31.610000"))
                    .destinoLng(new BigDecimal("-60.700000"))
                    .costoEstimado(new BigDecimal("7200.80"))
                    .distanciaEstimadaKm(new BigDecimal("350.40"))
                    .duracionEstimadaMin(240)
                    .rutaRef("RUTA-CBA-SFE-001")
                    .build()
            );

            repository.saveAll(solicitudes);
            System.out.println("Datos iniciales de solicitudes cargados en la base de datos");
        }
    }

    private void initSolicitudContenedores(
            SolicitudContenedorRepository repository, 
            SolicitudRepository solicitudRepository,
            ContenedorRepository contenedorRepository) {
        
        if (repository.count() == 0) {
            List<Solicitud> solicitudes = solicitudRepository.findAll();
            List<Contenedor> contenedores = contenedorRepository.findAll();
            
            // Asignar contenedores a solicitudes
            List<SolicitudContenedor> relaciones = List.of(
                // Solicitud 1 (PROGRAMADA) - 2 contenedores
                SolicitudContenedor.builder()
                    .solicitudId(solicitudes.get(0).getId())
                    .contenedorId(contenedores.get(0).getId())
                    .ordenCarga(1)
                    .observaciones("Contenedor principal")
                    .build(),

                SolicitudContenedor.builder()
                    .solicitudId(solicitudes.get(0).getId())
                    .contenedorId(contenedores.get(1).getId())
                    .ordenCarga(2)
                    .observaciones("Contenedor secundario")
                    .build(),

                // Solicitud 2 (EN_TRANSITO) - 1 contenedor
                SolicitudContenedor.builder()
                    .solicitudId(solicitudes.get(1).getId())
                    .contenedorId(contenedores.get(3).getId())
                    .ordenCarga(1)
                    .observaciones("Contenedor en tránsito")
                    .build(),

                // Solicitud 3 (BORRADOR) - 1 contenedor
                SolicitudContenedor.builder()
                    .solicitudId(solicitudes.get(2).getId())
                    .contenedorId(contenedores.get(2).getId())
                    .ordenCarga(1)
                    .observaciones("Contenedor asignado")
                    .build(),

                // Solicitud 4 (ENTREGADA) - 2 contenedores
                SolicitudContenedor.builder()
                    .solicitudId(solicitudes.get(3).getId())
                    .contenedorId(contenedores.get(4).getId())
                    .ordenCarga(1)
                    .observaciones("Contenedor entregado")
                    .build(),

                SolicitudContenedor.builder()
                    .solicitudId(solicitudes.get(3).getId())
                    .contenedorId(contenedores.get(5).getId())
                    .ordenCarga(2)
                    .observaciones("Contenedor adicional")
                    .build()
            );

            repository.saveAll(relaciones);
            System.out.println("Relaciones de solicitud-contenedor cargadas en la base de datos");
        }
    }

    private void initEstimadas(EstimadaRepository repository, SolicitudRepository solicitudRepository) {
        if (repository.count() == 0) {
            List<Solicitud> solicitudes = solicitudRepository.findAll();
            
            List<Estimada> estimadas = List.of(
                // Estimaciones para Solicitud 1
                Estimada.builder()
                    .solicitudId(solicitudes.get(0).getId())
                    .fuente("SISTEMA")
                    .payloadJson("{\"algoritmo\": \"basico\", \"parametros\": {\"combustible\": 4500, \"peajes\": 800}}")
                    .distanciaKm(new BigDecimal("700.50"))
                    .duracionMin(480)
                    .costoTotal(new BigDecimal("12500.50"))
                    .moneda(Moneda.ARS)
                    .build(),

                Estimada.builder()
                    .solicitudId(solicitudes.get(0).getId())
                    .fuente("OPERADOR")
                    .payloadJson("{\"observaciones\": \"Estimación manual por experiencia\", \"factor\": 1.1}")
                    .distanciaKm(new BigDecimal("700.50"))
                    .duracionMin(500)
                    .costoTotal(new BigDecimal("13000.00"))
                    .moneda(Moneda.ARS)
                    .build(),

                // Estimaciones para Solicitud 2
                Estimada.builder()
                    .solicitudId(solicitudes.get(1).getId())
                    .fuente("SISTEMA")
                    .payloadJson("{\"algoritmo\": \"avanzado\", \"combustible\": 3200, \"peajes\": 500}")
                    .distanciaKm(new BigDecimal("400.25"))
                    .duracionMin(300)
                    .costoTotal(new BigDecimal("8500.75"))
                    .moneda(Moneda.ARS)
                    .build(),

                // Estimaciones para Solicitud 3
                Estimada.builder()
                    .solicitudId(solicitudes.get(2).getId())
                    .fuente("MS-RECURSOS")
                    .payloadJson("{\"api\": \"costos-v1\", \"segmentos\": 3, \"tarifas_aplicadas\": [\"ESTANDAR_PESO\", \"ESTANDAR_VOLUMEN\"]}")
                    .distanciaKm(new BigDecimal("600.75"))
                    .duracionMin(420)
                    .costoTotal(new BigDecimal("9500.25"))
                    .moneda(Moneda.ARS)
                    .build(),

                // Estimaciones para Solicitud 4
                Estimada.builder()
                    .solicitudId(solicitudes.get(3).getId())
                    .fuente("SISTEMA")
                    .payloadJson("{\"algoritmo\": \"premium\", \"combustible\": 3800, \"peajes\": 600, \"otros\": 6600}")
                    .distanciaKm(new BigDecimal("650.00"))
                    .duracionMin(450)
                    .costoTotal(new BigDecimal("11000.00"))
                    .moneda(Moneda.ARS)
                    .build()
            );

            repository.saveAll(estimadas);
            System.out.println("Datos iniciales de estimaciones cargados en la base de datos");
        }
    }

    private void initSolicitudEventos(SolicitudEventoRepository repository, SolicitudRepository solicitudRepository) {
        if (repository.count() == 0) {
            List<Solicitud> solicitudes = solicitudRepository.findAll();
            
            List<SolicitudEvento> eventos = List.of(
                // Eventos para Solicitud 1 (PROGRAMADA)
                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(0).getId())
                    .estado("BORRADOR")
                    .detalle("Solicitud creada por el sistema")
                    .actor("SISTEMA")
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build(),

                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(0).getId())
                    .estado("PROGRAMADA")
                    .detalle("Solicitud programada para envío inmediato")
                    .actor("OPERADOR_001")
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build(),

                // Eventos para Solicitud 2 (EN_TRANSITO)
                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(1).getId())
                    .estado("BORRADOR")
                    .detalle("Solicitud creada automáticamente")
                    .actor("SISTEMA")
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build(),

                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(1).getId())
                    .estado("PROGRAMADA")
                    .detalle("Asignada a camión CAM-003")
                    .actor("DISPATCHER_002")
                    .createdAt(LocalDateTime.now().minusDays(4))
                    .build(),

                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(1).getId())
                    .estado("EN_TRANSITO")
                    .detalle("Camión en ruta hacia destino")
                    .actor("GPS_TRACKING")
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build(),

                // Eventos para Solicitud 3 (BORRADOR)
                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(2).getId())
                    .estado("BORRADOR")
                    .detalle("Solicitud en proceso de creación")
                    .actor("CLIENTE_WEB")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build(),

                // Eventos para Solicitud 4 (ENTREGADA)
                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(3).getId())
                    .estado("BORRADOR")
                    .detalle("Solicitud recibida")
                    .actor("SISTEMA")
                    .createdAt(LocalDateTime.now().minusDays(10))
                    .build(),

                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(3).getId())
                    .estado("PROGRAMADA")
                    .detalle("Programada para envío prioritario")
                    .actor("OPERADOR_003")
                    .createdAt(LocalDateTime.now().minusDays(9))
                    .build(),

                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(3).getId())
                    .estado("EN_TRANSITO")
                    .detalle("En camino al destino final")
                    .actor("DRIVER_005")
                    .createdAt(LocalDateTime.now().minusDays(8))
                    .build(),

                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(3).getId())
                    .estado("ENTREGADA")
                    .detalle("Mercadería entregada y firmada por destinatario")
                    .actor("DRIVER_005")
                    .createdAt(LocalDateTime.now().minusDays(7))
                    .build(),

                // Eventos para Solicitud 5 (PROGRAMADA)
                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(4).getId())
                    .estado("BORRADOR")
                    .detalle("Nueva solicitud de transporte")
                    .actor("CLIENTE_PORTAL")
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build(),

                SolicitudEvento.builder()
                    .solicitudId(solicitudes.get(4).getId())
                    .estado("PROGRAMADA")
                    .detalle("Asignada para envío estándar")
                    .actor("OPERADOR_002")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build()
            );

            repository.saveAll(eventos);
            System.out.println("Datos iniciales de eventos de solicitud cargados en la base de datos");
        }
    }
}