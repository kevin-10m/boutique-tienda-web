package com.boutique.service;

import com.boutique.domain.EstadoPedido;
import com.boutique.domain.Pedido;
import com.boutique.repository.PedidoRepository;
import com.boutique.repository.ProductoRepository;
import com.boutique.repository.VarianteRepository;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoService {
    private final PedidoRepository pedidos;
    private final VarianteRepository variantes;
    private final ProductoRepository productos;

    public Pedido buscarDeClienta(String correo, String numero) {
        return pedidos.findByNumeroAndClientaCorreo(numero, correo)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado."));
    }

    public List<Pedido> listarDeClienta(String correo) {
        return pedidos.findByClientaCorreoOrderByFechaDesc(correo);
    }

    public List<Pedido> listarTodos() {
        return pedidos.findAllByOrderByFechaDesc();
    }

    public Map<EstadoPedido, Long> contarPorEstado() {
        var conteo = new EnumMap<EstadoPedido, Long>(EstadoPedido.class);
        for (var estado : EstadoPedido.values()) {
            conteo.put(estado, pedidos.countByEstado(estado));
        }
        return conteo;
    }

    /** Avanza el pedido solo a un estado permitido y devuelve el inventario si se cancela. */
    @Transactional
    public void cambiarEstado(Long id, EstadoPedido nuevo) {
        var pedido = pedidos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado."));
        if (!pedido.getEstado().getSiguientes().contains(nuevo)) {
            throw new IllegalArgumentException("Ese cambio de estado no está permitido.");
        }
        pedido.setEstado(nuevo);
        if (nuevo == EstadoPedido.CANCELADO) {
            for (var detalle : pedido.getDetalles()) {
                var variante = detalle.getVariante();
                variantes.reponer(variante.getId(), detalle.getCantidad());
                productos.reponer(variante.getProducto().getIdProducto(), detalle.getCantidad());
            }
        }
    }
}
