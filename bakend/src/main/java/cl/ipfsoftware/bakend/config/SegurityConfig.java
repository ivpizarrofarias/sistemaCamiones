package cl.ipfsoftware.bakend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SegurityConfig {

    // ===================== PASSWORD ENCODER =====================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===================== SECURITY FILTER =====================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF OFF (obligatorio para Postman / Angular)
                .csrf(csrf -> csrf.disable())

                //  Desactivar login por formulario
                .formLogin(form -> form.disable())

                // Desactivar basic auth
                .httpBasic(basic -> basic.disable())

                //Autorización
                .authorizeHttpRequests(auth -> auth
                        //  ENDPOINT PÚBLICO
                        .requestMatchers(
                                "/api/usuarios/guardar",
                                "/api/contenedores/**",
                                "/api/usuarios/**"

                        ).permitAll()

                        // 🔒 TODO LO DEMÁS PROTEGIDO
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
