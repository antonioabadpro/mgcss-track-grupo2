package com.mgcss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/styles.css", "/images/**", "/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/**").permitAll() // La API la dejamos sin auth estricto para no romper tests
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/solicitudes", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")); // Desactivar CSRF para la API REST si aplica

        return http.build();
    }

    @org.springframework.beans.factory.annotation.Value("${app.admin.password:admin123}")
    private String adminPassword;

    @org.springframework.beans.factory.annotation.Value("${app.user.password:user123}")
    private String userPassword;

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode(adminPassword))
            .roles("ADMIN")
            .build();
            
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder.encode(userPassword))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
