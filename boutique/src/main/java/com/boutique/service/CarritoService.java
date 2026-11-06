package com.boutique.service;

import com.boutique.domain.LineaCarrito;
import com.boutique.domain.Variante;
import com.boutique.repository.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarritoService {
    private final ClientaRepository clientas;
    private final VarianteRepository variantes;
    private final LineaCarritoRepository lineas;

    @Value("${boutique.envio.estandar}")
    private BigDecimal envioEstandar;

    public List<Variante> opciones(Integer productoId) {
        return variantes.findByProductoIdProductoOrderByTallaAscColorAsc(productoId);
    }

    public long contarUnidades(String correo) {
        return lineas.contarUnidades(correo);
    }

    public record Resumen(List<LineaCarrito> lineas, BigDecimal subtotal,
                          BigDecimal envio, BigDecimal total) {}

    public Resumen consultar(String correo) {
        var contenido = lineas.findByClientaCorreoOrderByIdAsc(correo);
        var subtotal = contenido.stream().map(LineaCarrito::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var envio = contenido.isEmpty() ? BigDecimal.ZERO : envioEstandar;
        return new Resumen(contenido, subtotal, envio, subtotal.add(envio));
    }

    @Transactional
    public void agregar(String correo, Long varianteId, int cantidad) {
        var clienta = clientas.bloquearPorCorreo(correo);
        var variante = variantes.findById(varianteId)
                .orElseThrow(() -> new IllegalArgumentException("La opción seleccionada ya no existe."));
        var linea = lineas.findByClientaIdAndVarianteId(clienta.getId(), varianteId)
                .orElseGet(LineaCarrito::new);
        if (cantidad < 1) {
            throw new IllegalArgumentException("Elegí al menos una unidad.");
        }
        long total = (long) linea.getCantidad() + cantidad;
        validarCantidad(variante, total);
        linea.setClienta(clienta);
        linea.setVariante(variante);
        linea.setCantidad((int) total);
        lineas.save(linea);
    }

    @Transactional
    public void actualizar(String correo, Long id, int cantidad) {
        var linea = buscarLineaPropia(correo, id);
        validarCantidad(linea.getVariante(), cantidad);
        linea.setCantidad(cantidad);
    }

    @Transactional
    public void eliminar(String correo, Long id) {
        lineas.delete(buscarLineaPropia(correo, id));
    }

    private LineaCarrito buscarLineaPropia(String correo, Long id) {
        var clienta = clientas.bloquearPorCorreo(correo);
        return lineas.findByIdAndClientaId(id, clienta.getId())
                .orElseThrow(() -> new IllegalArgumentException("El artículo ya no está en tu carrito."));
    }

    private void validarCantidad(Variante variante, long cantidad) {
        var producto = variante.getProducto();
        if (!Boolean.TRUE.equals(producto.getActivo()) || cantidad < 1
                || cantidad > Math.min(variante.getExistencias(), producto.getExistencias())) {
            throw new IllegalArgumentException("La cantidad solicitada supera las existencias o es inválida.");
        }
    }
}
