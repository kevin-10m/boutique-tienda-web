package com.boutique.service;

import com.boutique.domain.Clienta;
import com.boutique.repository.ClientaRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientaService implements UserDetailsService {
    private final ClientaRepository clientas;
    private final PasswordEncoder encoder;

    @Transactional
    public void registrar(String correo, String clave) {
        Clienta clienta = new Clienta();
        clienta.setCorreo(correo.strip().toLowerCase(Locale.ROOT));
        clienta.setClaveHash(encoder.encode(clave));
        clientas.saveAndFlush(clienta);
    }

    @Transactional(readOnly = true)
    public Clienta buscar(String correo) {
        return clientas.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Cuenta desconocida"));
    }

    @Override
    public UserDetails loadUserByUsername(String correo) {
        var clienta = clientas.findByCorreo(correo.strip().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UsernameNotFoundException("Cuenta desconocida"));
        return User.withUsername(clienta.getCorreo()).password(clienta.getClaveHash())
                .roles(clienta.getRol()).build();
    }
}
