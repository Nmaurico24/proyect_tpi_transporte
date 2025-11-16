package ar.edu.utnfc.backend.ms_logistica.config;

import ar.edu.utnfc.backend.ms_logistica.models.*;
import ar.edu.utnfc.backend.ms_logistica.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    CommandLineRunner seedLogisticaData(
            ClienteRepository clienteRepository,
            ContenedorRepository contenedorRepository,
            SolicitudRepository solicitudRepository,
            SolicitudContenedorRepository solicitudContenedorRepository,
            EstimadaRepository estimadaRepository,
            SolicitudEventoRepository solicitudEventoRepository
    ) {
        return args -> {
            var clientes = initClientes(clienteRepository);
            var contenedores = initContenedores(contenedorRepository, clientes);
            var solicitudes = initSolicitudes(solicitudRepository, clientes);
            initSolicitudContenedores(solicitudContenedorRepository, solicitudes, contenedores);
            initEstimadas(estimadaRepository, solicitudes);
            initSolicitudEventos(solicitudEventoRepository, solicitudes);
        };
    }

    private List<Cliente> initClientes(ClienteRepository repository) {
        if (repository.count() == 0) {
            var clientes = List.of(
                    Cliente.builder().numero("CLI-001").nombre("Empresa Logística S.A.")
                            .telefono("351-1234567").email("contacto@empresalogistica.com").isActive(true).build(),
                    Cliente.builder().numero("CLI-002").nombre("Distribuidora Norte")
                            .telefono("351-2345678").email("ventas@distribuidoranorte.com").isActive(true).build(),
                    Cliente.builder().numero("CLI-003").nombre("Comercial del Sur")
                            .telefono("351-3456789").email("info@comercialsur.com").isActive(true).build(),
                    Cliente.builder().numero("CLI-004").nombre("Importadora Este")
                            .telefono("351-4567890").email("administracion@importadoraeste.com").isActive(true).build(),
                    Cliente.builder().numero("CLI-005").nombre("Exportadora Oeste")
                            .telefono("351-5678901").email("logistica@exportadoraoeste.com").isActive(true).build()
            );

            repository.saveAll(clientes);
        }

        return repository.findAll();
    }

    private List<Contenedor> initContenedores(ContenedorRepository repository, List<Cliente> clientes) {
        if (repository.count() == 0) {
            var contenedores = List.of(
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-001"))
                            .etiqueta("CONT-001-001")
                            .pesoKg(5000d)
                            .volumenM3(25d)
                            .estado("DISPONIBLE")
                            .isActive(true)
                            .build(),
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-001"))
                            .etiqueta("CONT-001-002")
                            .pesoKg(7500d)
                            .volumenM3(32.5)
                            .estado("DISPONIBLE")
                            .isActive(true)
                            .build(),
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-002"))
                            .etiqueta("CONT-002-001")
                            .pesoKg(3000d)
                            .volumenM3(18d)
                            .estado("DISPONIBLE")
                            .isActive(true)
                            .build(),
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-002"))
                            .etiqueta("CONT-002-002")
                            .pesoKg(6000d)
                            .volumenM3(28d)
                            .estado("ASIGNADO")
                            .isActive(true)
                            .build(),
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-003"))
                            .etiqueta("CONT-003-001")
                            .pesoKg(4000d)
                            .volumenM3(22d)
                            .estado("EN_TRANSITO")
                            .isActive(true)
                            .build(),
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-003"))
                            .etiqueta("CONT-003-002")
                            .pesoKg(5500d)
                            .volumenM3(30d)
                            .estado("DISPONIBLE")
                            .isActive(true)
                            .build(),
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-004"))
                            .etiqueta("CONT-004-001")
                            .pesoKg(8000d)
                            .volumenM3(35d)
                            .estado("MANTENIMIENTO")
                            .isActive(true)
                            .build(),
                    Contenedor.builder()
                            .cliente(findCliente(clientes, "CLI-005"))
                            .etiqueta("CONT-005-001")
                            .pesoKg(2500d)
                            .volumenM3(15d)
                            .estado("DISPONIBLE")
                            .isActive(true)
                            .build()
            );

            repository.saveAll(contenedores);
        }

        return repository.findAll();
    }

    private List<Solicitud> initSolicitudes(SolicitudRepository repository, List<Cliente> clientes) {
        if (repository.count() == 0) {
            var solicitudes = List.of(
                    Solicitud.builder()
                            .cliente(findCliente(clientes, "CLI-001"))
                            .estado(EstadoSolicitud.PROGRAMADA)
                            .prioridad(1)
                            .origenDireccion("Av. Colón 1200, Córdoba")
                            .destinoDireccion("Av. Corrientes 123, Buenos Aires")
                            .moneda(Moneda.ARS)
                            .precioEstimado(12500.5)
                            .distanciaEstimadaKm(700.5)
                            .duracionEstimadaMin(480)
                            .rutaRef("RUTA-CBA-BSAS-001")
                            .build(),
                    Solicitud.builder()
                            .cliente(findCliente(clientes, "CLI-001"))
                            .estado(EstadoSolicitud.EN_TRANSITO)
                            .prioridad(2)
                            .origenDireccion("Av. La Voz del Interior 7000, Córdoba")
                            .destinoDireccion("Puerto Rosario, Santa Fe")
                            .moneda(Moneda.ARS)
                            .precioEstimado(8900d)
                            .distanciaEstimadaKm(400d)
                            .duracionEstimadaMin(360)
                            .rutaRef("RUTA-CBA-ROS-002")
                            .build(),
                    Solicitud.builder()
                            .cliente(findCliente(clientes, "CLI-002"))
                            .estado(EstadoSolicitud.PROGRAMADA)
                            .prioridad(1)
                            .origenDireccion("Ruta 9 Km 12, Córdoba")
                            .destinoDireccion("Av. Mitre 500, Tucumán")
                            .moneda(Moneda.ARS)
                            .precioEstimado(6700d)
                            .distanciaEstimadaKm(600d)
                            .duracionEstimadaMin(420)
                            .rutaRef("RUTA-CBA-TUC-003")
                            .build(),
                    Solicitud.builder()
                            .cliente(findCliente(clientes, "CLI-003"))
                            .estado(EstadoSolicitud.EN_TRANSITO)
                            .prioridad(3)
                            .origenDireccion("Av. Fuerza Aérea 3500, Córdoba")
                            .destinoDireccion("Zona Franca, Mendoza")
                            .moneda(Moneda.ARS)
                            .precioEstimado(15200d)
                            .distanciaEstimadaKm(900d)
                            .duracionEstimadaMin(600)
                            .rutaRef("RUTA-CBA-MDZ-004")
                            .build()
            );

            repository.saveAll(solicitudes);
        }

        return repository.findAll();
    }

    private void initSolicitudContenedores(SolicitudContenedorRepository repository,
                                           List<Solicitud> solicitudes,
                                           List<Contenedor> contenedores) {
        if (repository.count() > 0) {
            return;
        }

        var items = List.of(
                SolicitudContenedor.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-BSAS-001"))
                        .contenedor(findContenedor(contenedores, "CONT-001-001"))
                        .build(),
                SolicitudContenedor.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-BSAS-001"))
                        .contenedor(findContenedor(contenedores, "CONT-001-002"))
                        .build(),
                SolicitudContenedor.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-ROS-002"))
                        .contenedor(findContenedor(contenedores, "CONT-002-002"))
                        .build(),
                SolicitudContenedor.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-TUC-003"))
                        .contenedor(findContenedor(contenedores, "CONT-002-001"))
                        .build(),
                SolicitudContenedor.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-MDZ-004"))
                        .contenedor(findContenedor(contenedores, "CONT-003-001"))
                        .build()
        );

        repository.saveAll(items);
    }

    private void initEstimadas(EstimadaRepository repository, List<Solicitud> solicitudes) {
        if (repository.count() > 0) {
            return;
        }

        var estimadas = List.of(
                Estimada.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-BSAS-001"))
                        .fuente("ms-operaciones")
                        .payloadJson("{\"km\":700.5,\"duracion\":480}")
                        .distanciaKm(700.5)
                        .duracionMin(480)
                        .costoTotal(12500.5)
                        .moneda(Moneda.ARS)
                        .build(),
                Estimada.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-ROS-002"))
                        .fuente("ms-operaciones")
                        .payloadJson("{\"km\":400,\"duracion\":360}")
                        .distanciaKm(400d)
                        .duracionMin(360)
                        .costoTotal(8900d)
                        .moneda(Moneda.ARS)
                        .build(),
                Estimada.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-TUC-003"))
                        .fuente("ms-operaciones")
                        .payloadJson("{\"km\":600,\"duracion\":420}")
                        .distanciaKm(600d)
                        .duracionMin(420)
                        .costoTotal(6700d)
                        .moneda(Moneda.ARS)
                        .build(),
                Estimada.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-MDZ-004"))
                        .fuente("ms-operaciones")
                        .payloadJson("{\"km\":900,\"duracion\":600}")
                        .distanciaKm(900d)
                        .duracionMin(600)
                        .costoTotal(15200d)
                        .moneda(Moneda.ARS)
                        .build()
        );

        repository.saveAll(estimadas);
    }

    private void initSolicitudEventos(SolicitudEventoRepository repository, List<Solicitud> solicitudes) {
        if (repository.count() > 0) {
            return;
        }

        var eventos = List.of(
                SolicitudEvento.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-BSAS-001"))
                        .estado("CREADA")
                        .detalle("Solicitud cargada en el sistema")
                        .actor("OPERADOR")
                        .build(),
                SolicitudEvento.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-BSAS-001"))
                        .estado("PROGRAMADA")
                        .detalle("Asignación de recursos completada")
                        .actor("SISTEMA")
                        .build(),
                SolicitudEvento.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-ROS-002"))
                        .estado("EN_TRANSITO")
                        .detalle("Camión salió del depósito Córdoba Norte")
                        .actor("OPERADOR")
                        .build(),
                SolicitudEvento.builder()
                        .solicitud(findSolicitud(solicitudes, "RUTA-CBA-MDZ-004"))
                        .estado("EN_TRANSITO")
                        .detalle("Arribando a checkpoint San Luis")
                        .actor("SISTEMA")
                        .build()
        );

        repository.saveAll(eventos);
    }

    private Cliente findCliente(List<Cliente> clientes, String numero) {
        return clientes.stream()
                .filter(c -> numero.equals(c.getNumero()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cliente " + numero + " no encontrado"));
    }

    private Contenedor findContenedor(List<Contenedor> contenedores, String etiqueta) {
        return contenedores.stream()
                .filter(c -> etiqueta.equals(c.getEtiqueta()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Contenedor " + etiqueta + " no encontrado"));
    }

    private Solicitud findSolicitud(List<Solicitud> solicitudes, String rutaRef) {
        return solicitudes.stream()
                .filter(s -> rutaRef.equals(s.getRutaRef()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Solicitud " + rutaRef + " no encontrada"));
    }
}
