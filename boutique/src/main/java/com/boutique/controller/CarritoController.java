package com.boutique.controller;

import com.boutique.service.CarritoService;
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
    private final CarritoService carrito;

    @GetMapping
    public String ver(Principal clienta, Model model) {
        model.addAttribute("resumen", carrito.consultar(clienta.getName()));
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

    @ExceptionHandler(IllegalArgumentException.class)
    public String avisar(IllegalArgumentException error, RedirectAttributes mensajes) {
        mensajes.addFlashAttribute("errorCarrito", error.getMessage());
        return "redirect:/carrito";
    }
}
