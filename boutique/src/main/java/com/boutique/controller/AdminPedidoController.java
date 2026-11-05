package com.boutique.controller;

import com.boutique.domain.EstadoPedido;
import com.boutique.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/pedidos")
public class AdminPedidoController {
    private final PedidoService pedidos;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("pedidos", pedidos.listarTodos());
        model.addAttribute("conteo", pedidos.contarPorEstado());
        return "admin/pedidos";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado,
                                RedirectAttributes mensajes) {
        try {
            pedidos.cambiarEstado(id, estado);
            mensajes.addFlashAttribute("mensaje", "Estado actualizado.");
        } catch (IllegalArgumentException error) {
            mensajes.addFlashAttribute("errorPedido", error.getMessage());
        }
        return "redirect:/admin/pedidos";
    }
}
