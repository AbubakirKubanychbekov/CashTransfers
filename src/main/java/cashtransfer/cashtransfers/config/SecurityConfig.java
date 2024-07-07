package cashtransfer.cashtransfers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author Abubakir Dev
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.addAllowedHeader("*");
                    corsConfig.addAllowedMethod("*");
                    corsConfig.addAllowedOrigin("*");
                    return corsConfig;
                }))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/cash_registers/**",
                                "/cash_registers/create",
                                "/cash_registers/save",
                                "/cash_registers/transfer",
                                "/cash_registers/search",
                                "/transfers/**",
                                "/auth/**",
                                "/css/**", "/js/**", "/images/**",
                                "/error/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
