package com.boutique.service;

import com.boutique.domain.Pedido;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/** Sin servidor de correo configurado, deja el mensaje en la consola para poder probar. */
@Slf4j
@Service
@RequiredArgsConstructor
public class CorreoService {
    private final ObjectProvider<JavaMailSender> remitente;

    @Value("${spring.mail.host:}")
    private String servidorCorreo;

    @Value("${boutique.correo.remitente}")
    private String direccionRemitente;

    public void enviarConfirmacion(Pedido pedido) {
        String productos = pedido.getDetalles().stream()
                .map(d -> "- %d × %s (%s, %s)".formatted(d.getCantidad(), d.getNombreProducto(), d.getTalla(), d.getColor()))
                .collect(Collectors.joining("\n"));
        String texto = """
                ¡Gracias por tu compra, %s!

                Pedido: %s
                %s

                Total: ₡%,.0f
                Estado: %s
                """.formatted(pedido.getNombre(), pedido.getNumero(), productos,
                pedido.getTotal(), pedido.getEstado().getEtiqueta());

        var servidor = remitente.getIfAvailable();
        if (servidor == null || servidorCorreo.isBlank()) {
            log.info("Correo no configurado. Confirmación para {}:\n{}", pedido.getClienta().getCorreo(), texto);
            return;
        }
        var mensaje = new SimpleMailMessage();
        mensaje.setFrom(direccionRemitente);
        mensaje.setTo(pedido.getClienta().getCorreo());
        mensaje.setSubject("Boutique: pedido " + pedido.getNumero());
        mensaje.setText(texto);
        try {
            servidor.send(mensaje);
        } catch (MailException error) {
            log.warn("No se pudo enviar la confirmación del pedido {}", pedido.getNumero(), error);
        }
    }
}
