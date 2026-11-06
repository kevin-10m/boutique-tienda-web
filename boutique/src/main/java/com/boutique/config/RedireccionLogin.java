package com.boutique.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/** Envía al administrador a su panel y a la clienta a la página que estaba visitando. */
public class RedireccionLogin extends SavedRequestAwareAuthenticationSuccessHandler {

    public RedireccionLogin() {
        setDefaultTargetUrl("/catalogo");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest solicitud, HttpServletResponse respuesta,
                                        Authentication autenticacion) throws ServletException, IOException {
        boolean esAdmin = autenticacion.getAuthorities().stream()
                .anyMatch(rol -> "ROLE_ADMIN".equals(rol.getAuthority()));
        if (esAdmin) {
            getRedirectStrategy().sendRedirect(solicitud, respuesta, "/admin/pedidos");
            return;
        }
        super.onAuthenticationSuccess(solicitud, respuesta, autenticacion);
    }
}
