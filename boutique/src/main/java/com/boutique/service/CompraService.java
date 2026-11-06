package com.boutique.service;

import com.boutique.domain.*;
import com.boutique.repository.*;
import com.boutique.service.pago.PagoService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompraService {
    private static final DateTimeFormatter FORMATO_NUMERO = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ClientaRepository clientas;
    private final LineaCarritoRepository lineas;
    private final VarianteRepository variantes;
    private final ProductoRepository productos;
    private final PedidoRepository pedidos;
    private final CarritoService carrito;
    private final CuponService cupones;
    private final PagoService pagos;

    /**
     * Convierte el carrito en un pedido. Todo ocurre en una sola transacción: si falta
     * inventario o el pago se rechaza, no queda pedido ni se descuentan existencias.
     */
    @Transactional
    public Pedido confirmar(String correo, CheckoutForm datos, String codigoCupon) {
        var clienta = clientas.bloquearPorCorreo(correo);
        var contenido = lineas.findByClientaCorreoOrderByIdAsc(correo).stream()
                .sorted(Comparator.comparing(linea -> linea.getVariante().getId()))
                .toList();
        if (contenido.isEmpty()) {
            throw new IllegalArgumentException("Tu carrito está vacío.");
        }

        descontarInventario(contenido);

        var cupon = cupones.buscarVigente(codigoCupon).orElse(null);
        var resumen = carrito.calcular(contenido, cupon, datos.getEntrega());
        var pago = pagos.cobrar(datos, resumen.total());

        var pedido = new Pedido();
        pedido.setClienta(clienta);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(pago.estadoInicial());
        pedido.setEntrega(datos.getEntrega());
        pedido.setNombre(datos.getNombre().strip());
        pedido.setTelefono(datos.getTelefono().strip());
        if (datos.getEntrega() == Entrega.DOMICILIO) {
            pedido.setProvincia(datos.getProvincia().strip());
            pedido.setCanton(datos.getCanton().strip());
            pedido.setDistrito(datos.getDistrito().strip());
            pedido.setDireccion(datos.getDireccion().strip());
        }
        pedido.setMetodoPago(datos.getMetodoPago());
        pedido.setComprobante(pago.comprobante());
        pedido.setReferenciaPago(pago.referencia());
        pedido.setCupon(resumen.cupon());
        pedido.setSubtotal(resumen.subtotal());
        pedido.setDescuento(resumen.descuento());
        pedido.setEnvio(resumen.envio());
        pedido.setTotal(resumen.total());
        contenido.forEach(linea -> pedido.getDetalles().add(crearDetalle(pedido, linea)));

        pedidos.save(pedido);
        pedido.setNumero("BTQ-%s-%04d".formatted(pedido.getFecha().format(FORMATO_NUMERO), pedido.getId()));

        if (datos.isGuardarDireccion() && datos.getEntrega() == Entrega.DOMICILIO) {
            guardarDireccion(clienta, datos);
        }
        lineas.deleteAll(contenido);
        return pedido;
    }

    private void descontarInventario(List<LineaCarrito> contenido) {
        var unidadesPorProducto = new TreeMap<Integer, Integer>();
        for (var linea : contenido) {
            var variante = linea.getVariante();
            var producto = variante.getProducto();
            if (!Boolean.TRUE.equals(producto.getActivo())
                    || variantes.descontar(variante.getId(), linea.getCantidad()) == 0) {
                throw sinExistencias(producto);
            }
            unidadesPorProducto.merge(producto.getIdProducto(), linea.getCantidad(), Integer::sum);
        }
        unidadesPorProducto.forEach((id, unidades) -> {
            if (productos.descontar(id, unidades) == 0) {
                throw new IllegalArgumentException("Uno de los productos ya no tiene existencias suficientes.");
            }
        });
    }

    private IllegalArgumentException sinExistencias(Producto producto) {
        return new IllegalArgumentException(producto.getNombre() + " ya no tiene existencias suficientes.");
    }

    private DetallePedido crearDetalle(Pedido pedido, LineaCarrito linea) {
        var variante = linea.getVariante();
        var detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setVariante(variante);
        detalle.setNombreProducto(variante.getProducto().getNombre());
        detalle.setTalla(variante.getTalla());
        detalle.setColor(variante.getColor());
        detalle.setCantidad(linea.getCantidad());
        detalle.setPrecioUnitario(variante.getProducto().getPrecioFinal());
        return detalle;
    }

    private void guardarDireccion(Clienta clienta, CheckoutForm datos) {
        clienta.setNombre(datos.getNombre().strip());
        clienta.setTelefono(datos.getTelefono().strip());
        clienta.setProvincia(datos.getProvincia().strip());
        clienta.setCanton(datos.getCanton().strip());
        clienta.setDistrito(datos.getDistrito().strip());
        clienta.setDireccion(datos.getDireccion().strip());
    }
}
