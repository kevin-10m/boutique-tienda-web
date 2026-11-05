package com.boutique.controller;

import com.boutique.service.CarritoService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequiredArgsConstructor
public class NavegacionControllerAdvice {
    private final CarritoService carrito;

    @ModelAttribute("unidadesCarrito")
    public long unidadesCarrito(Principal clienta) {
        return clienta == null ? 0 : carrito.contarUnidades(clienta.getName());
    }

    @ModelAttribute("correoActual")
    public String correoActual(Principal clienta) {
        return clienta == null ? "" : clienta.getName();
    }
}
