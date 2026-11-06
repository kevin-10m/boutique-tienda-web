package com.boutique.controller;

import com.boutique.domain.EstadoPedido;
import com.boutique.service.PedidoService;
import java.security.Principal;
import java.util.List;
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

    @GetMapping("/{numero}")
    public String detalle(@PathVariable String numero, Principal clienta, Model model) {
        model.addAttribute("pedido", pedidos.buscarDeClienta(clienta.getName(), numero));
        model.addAttribute("pasos", List.of(EstadoPedido.PENDIENTE_VERIFICACION, EstadoPedido.PAGADO,
                EstadoPedido.ENVIADO, EstadoPedido.ENTREGADO));
        return "pedido-detalle";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String noEncontrado() {
        return "redirect:/cuenta";
    }
}
