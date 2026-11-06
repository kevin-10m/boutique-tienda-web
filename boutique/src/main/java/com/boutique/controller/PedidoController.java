package com.boutique.controller;

import com.boutique.service.PedidoService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidos;

    @GetMapping("/{numero}/confirmado")
    public String confirmado(@PathVariable String numero, Principal clienta, Model model) {
        model.addAttribute("pedido", pedidos.buscarDeClienta(clienta.getName(), numero));
        return "pedido-confirmado";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String noEncontrado() {
        return "redirect:/cuenta";
    }
}
