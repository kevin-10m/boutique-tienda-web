package com.boutique.controller;

import com.boutique.service.ClientaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cuenta")
public class CuentaController {
    private final ClientaService clientas;

    public record Registro(@NotBlank @Email @Size(max = 254) String correo,
                           @NotBlank @Size(min = 8, max = 72) String clave) {}

    @GetMapping
    public String cuenta() {
        return "cuenta";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute Registro registro, BindingResult resultado, Model model) {
        if (resultado.hasErrors() || registro.clave().getBytes(StandardCharsets.UTF_8).length > 72) {
            model.addAttribute("errorRegistro", "Revisá el correo y usá una contraseña de al menos 8 caracteres y hasta 72 bytes.");
            return "cuenta";
        }
        try {
            clientas.registrar(registro.correo(), registro.clave());
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("errorRegistro", "El correo ya está registrado.");
            return "cuenta";
        }
        return "redirect:/cuenta?registrada";
    }
}
