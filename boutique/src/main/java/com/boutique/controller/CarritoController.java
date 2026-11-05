package com.boutique.controller;

import com.boutique.service.CarritoService;
import com.boutique.service.CuponService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {
    public static final String CUPON_SESION = "cuponCarrito";

    private final CarritoService carrito;
    private final CuponService cupones;

    @GetMapping
    public String ver(Principal clienta, HttpSession sesion, Model model) {
        var codigo = (String) sesion.getAttribute(CUPON_SESION);
        model.addAttribute("resumen", carrito.consultar(clienta.getName(), codigo));
        return "carrito";
    }

    @PostMapping("/agregar")
    public String agregar(Principal clienta, @RequestParam Long varianteId, @RequestParam int cantidad,
                          RedirectAttributes mensajes) {
        carrito.agregar(clienta.getName(), varianteId, cantidad);
        mensajes.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        return "redirect:/carrito";
    }

    @PostMapping("/{id}/cantidad")
    public String actualizar(Principal clienta, @PathVariable Long id, @RequestParam int cantidad) {
        carrito.actualizar(clienta.getName(), id, cantidad);
        return "redirect:/carrito";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(Principal clienta, @PathVariable Long id) {
        carrito.eliminar(clienta.getName(), id);
        return "redirect:/carrito";
    }

    @PostMapping("/cupon")
    public String aplicarCupon(@RequestParam String codigo, HttpSession sesion, RedirectAttributes mensajes) {
        var cupon = cupones.buscarVigente(codigo);
        if (cupon.isEmpty()) {
            mensajes.addFlashAttribute("errorCarrito", "El cupón no es válido o ya venció.");
            return "redirect:/carrito";
        }
        sesion.setAttribute(CUPON_SESION, cupon.get().getCodigo());
        mensajes.addFlashAttribute("mensaje", "Cupón aplicado.");
        return "redirect:/carrito";
    }

    @PostMapping("/cupon/quitar")
    public String quitarCupon(HttpSession sesion) {
        sesion.removeAttribute(CUPON_SESION);
        return "redirect:/carrito";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String avisar(IllegalArgumentException error, RedirectAttributes mensajes) {
        mensajes.addFlashAttribute("errorCarrito", error.getMessage());
        return "redirect:/carrito";
    }
}
