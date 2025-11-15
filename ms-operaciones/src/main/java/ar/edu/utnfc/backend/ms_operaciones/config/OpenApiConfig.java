package ar.edu.utnfc.backend.ms_operaciones.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("MS Operaciones")
                .version("1.0")
                .description("Rutas, tramos, asignación y tracking."));
    }
}
