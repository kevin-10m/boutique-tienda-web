package com.boutique.controller;

import com.boutique.domain.CheckoutForm;
import com.boutique.domain.Entrega;
import com.boutique.domain.MetodoPago;
import com.boutique.repository.ClientaRepository;
import com.boutique.service.CarritoService;
import com.boutique.service.CompraService;
import com.boutique.service.CorreoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private static final List<String> PROVINCIAS = List.of("San José", "Alajuela", "Cartago",
            "Heredia", "Guanacaste", "Puntarenas", "Limón");

    private final CarritoService carrito;
    private final CompraService compras;
    private final CorreoService correos;
    private final ClientaRepository clientas;

    @Value("${boutique.envio.estandar}")
    private BigDecimal envioEstandar;

    @Value("${boutique.retiro.horario}")
    private String horarioRetiro;

    @Value("${boutique.sinpe.numero}")
    private String numeroSinpe;

    @GetMapping
    public String formulario(Principal clienta, HttpSession sesion, Model model) {
        var form = new CheckoutForm();
        clientas.findByCorreo(clienta.getName()).ifPresent(perfil -> {
            form.setNombre(perfil.getNombre());
            form.setTelefono(perfil.getTelefono());
            form.setProvincia(perfil.getProvincia());
            form.setCanton(perfil.getCanton());
            form.setDistrito(perfil.getDistrito());
            form.setDireccion(perfil.getDireccion());
        });
        return mostrar(form, clienta, sesion, model);
    }

    @PostMapping
    public String confirmar(@Valid @ModelAttribute("compra") CheckoutForm form, BindingResult resultado,
                            Principal clienta, HttpSession sesion, Model model) {
        validarEntrega(form, resultado);
        if (resultado.hasErrors()) {
            return mostrar(form, clienta, sesion, model);
        }
        try {
            var cupon = (String) sesion.getAttribute(CarritoController.CUPON_SESION);
            var pedido = compras.confirmar(clienta.getName(), form, cupon);
            sesion.removeAttribute(CarritoController.CUPON_SESION);
            correos.enviarConfirmacion(pedido);
            return "redirect:/pedidos/" + pedido.getNumero() + "/confirmado";
        } catch (IllegalArgumentException error) {
            model.addAttribute("errorCompra", error.getMessage());
            return mostrar(form, clienta, sesion, model);
        }
    }

    private void validarEntrega(CheckoutForm form, BindingResult resultado) {
        if (form.getEntrega() != Entrega.DOMICILIO) {
            return;
        }
        rechazarSiVacio(form.getProvincia(), "provincia", "Elegí la provincia.", resultado);
        rechazarSiVacio(form.getCanton(), "canton", "Ingresá el cantón.", resultado);
        rechazarSiVacio(form.getDistrito(), "distrito", "Ingresá el distrito.", resultado);
        rechazarSiVacio(form.getDireccion(), "direccion", "Ingresá la dirección exacta.", resultado);
    }

    private void rechazarSiVacio(String valor, String campo, String mensaje, BindingResult resultado) {
        if (valor == null || valor.isBlank()) {
            resultado.rejectValue(campo, "requerido", mensaje);
        }
    }

    private String mostrar(CheckoutForm form, Principal clienta, HttpSession sesion, Model model) {
        var cupon = (String) sesion.getAttribute(CarritoController.CUPON_SESION);
        var resumen = carrito.consultar(clienta.getName(), cupon, form.getEntrega());
        if (resumen.lineas().isEmpty()) {
            return "redirect:/carrito";
        }
        form.setNumeroTarjeta(null);
        form.setVencimientoTarjeta(null);
        form.setCvv(null);
        model.addAttribute("compra", form);
        model.addAttribute("resumen", resumen);
        model.addAttribute("provincias", PROVINCIAS);
        model.addAttribute("metodos", MetodoPago.values());
        model.addAttribute("envioEstandar", envioEstandar);
        model.addAttribute("horarioRetiro", horarioRetiro);
        model.addAttribute("numeroSinpe", numeroSinpe);
        return "checkout";
    }
}
