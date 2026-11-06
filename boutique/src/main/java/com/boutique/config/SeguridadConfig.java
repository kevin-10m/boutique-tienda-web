package com.boutique.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain seguridad(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/carrito", "/carrito/**", "/checkout", "/pedidos/**").authenticated()
                .anyRequest().permitAll())
            .formLogin(login -> login.loginPage("/cuenta").loginProcessingUrl("/cuenta/ingresar")
                .usernameParameter("correo").passwordParameter("clave")
                .successHandler(new RedireccionLogin()).failureUrl("/cuenta?error"))
            .logout(logout -> logout.logoutUrl("/cuenta/salir").logoutSuccessUrl("/"));
        return http.build();
    }
}
