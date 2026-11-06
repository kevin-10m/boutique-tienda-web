package com.boutique.service;

import com.boutique.domain.Pedido;
import com.boutique.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoService {
    private final PedidoRepository pedidos;

    public Pedido buscarDeClienta(String correo, String numero) {
        return pedidos.findByNumeroAndClientaCorreo(numero, correo)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado."));
    }
}
