package ar.edu.utnfc.backend.ms_logistica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Logística - API")
                        .version("v1")
                        .description("Microservicio de logística: solicitudes y planificación."));
    }
}
