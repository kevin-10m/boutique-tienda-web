package com.boutique.controller;

import com.boutique.domain.Cupon;
import com.boutique.service.CuponService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/cupones")
public class AdminCuponController {
    private final CuponService cupones;

    @GetMapping
    public String listado(Model model) {
        var nuevo = new Cupon();
        nuevo.setVencimiento(LocalDate.now().plusMonths(1));
        return mostrar(nuevo, model);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        return mostrar(cupones.buscar(id), model);
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cupon") Cupon cupon, BindingResult resultado,
                          Model model, RedirectAttributes mensajes) {
        if (resultado.hasErrors()) {
            return mostrar(cupon, model);
        }
        try {
            cupones.guardar(cupon);
        } catch (DataIntegrityViolationException error) {
            resultado.rejectValue("codigo", "duplicado", "Ese código ya existe.");
            return mostrar(cupon, model);
        }
        mensajes.addFlashAttribute("mensaje", "Cupón guardado.");
        return "redirect:/admin/cupones";
    }

    @PostMapping("/{id}/activo")
    public String cambiarActivo(@PathVariable Long id, RedirectAttributes mensajes) {
        cupones.cambiarActivo(id);
        mensajes.addFlashAttribute("mensaje", "Cupón actualizado.");
        return "redirect:/admin/cupones";
    }

    private String mostrar(Cupon cupon, Model model) {
        model.addAttribute("cupon", cupon);
        model.addAttribute("cupones", cupones.listar());
        model.addAttribute("hoy", LocalDate.now());
        return "admin/cupones";
    }
}
