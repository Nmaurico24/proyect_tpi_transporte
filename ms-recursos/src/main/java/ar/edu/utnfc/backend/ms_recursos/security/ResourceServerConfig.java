package ar.edu.utnfc.backend.ms_recursos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        // DEV: todo abierto. Para Keycloak/JWT, reemplazar por oauth2ResourceServer +
        // antMatchers
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    // Para JWT real:
    // @Bean
    // SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // http.csrf(csrf -> csrf.disable())
    // .authorizeHttpRequests(auth -> auth
    // .requestMatchers("/actuator/**","/v3/api-docs/**","/swagger-ui/**").permitAll()
    // .anyRequest().authenticated()
    // )
    // .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
    // return http.build();
    // }
}
