package com.boutique;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.boutique.domain.*;
import com.boutique.repository.*;
import com.boutique.service.CompraService;
import com.boutique.service.PedidoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompraServiceTests {
    private static final String CORREO = "prueba@boutique.test";

    @Autowired private CompraService compras;
    @Autowired private PedidoService pedidoService;
    @Autowired private ClientaRepository clientas;
    @Autowired private CategoriaRepository categorias;
    @Autowired private ProductoRepository productos;
    @Autowired private VarianteRepository variantes;
    @Autowired private LineaCarritoRepository lineas;
    @Autowired private CuponRepository cupones;
    @Autowired private PedidoRepository pedidos;

    private Variante variante;

    @BeforeEach
    void prepararDatos() {
        pedidos.deleteAll();
        lineas.deleteAll();
        cupones.deleteAll();
        variantes.deleteAll();
        productos.deleteAll();
        categorias.deleteAll();
        clientas.deleteAll();

        var clienta = new Clienta();
        clienta.setCorreo(CORREO);
        clienta.setClaveHash("hash");
        clientas.save(clienta);

        var categoria = new Categoria();
        categoria.setNombre("Vestidos");
        categorias.save(categoria);

        var producto = new Producto();
        producto.setNombre("Vestido de prueba");
        producto.setDescripcion("Descripción");
        producto.setPrecio(50000d);
        producto.setExistencias(5);
        producto.setCategoria(categoria);
        productos.save(producto);

        variante = new Variante();
        variante.setProducto(producto);
        variante.setTalla("M");
        variante.setColor("Negro");
        variante.setExistencias(5);
        variantes.save(variante);

        agregarAlCarrito(clienta, 2);

        var cupon = new Cupon();
        cupon.setCodigo("PRUEBA10");
        cupon.setPorcentaje(10);
        cupon.setVencimiento(LocalDate.now().plusDays(1));
        cupones.save(cupon);
    }

    private void agregarAlCarrito(Clienta clienta, int cantidad) {
        var linea = new LineaCarrito();
        linea.setClienta(clienta);
        linea.setVariante(variante);
        linea.setCantidad(cantidad);
        lineas.save(linea);
    }

    private CheckoutForm tarjeta(String numero) {
        var datos = new CheckoutForm();
        datos.setNombre("Ana");
        datos.setTelefono("88881234");
        datos.setProvincia("San José");
        datos.setCanton("Escazú");
        datos.setDistrito("San Rafael");
        datos.setDireccion("Frente al parque");
        datos.setNumeroTarjeta(numero);
        datos.setVencimientoTarjeta("12/40");
        datos.setCvv("123");
        return datos;
    }

    @Test
    void creaPedidoPagadoDescuentaInventarioYVaciaCarrito() {
        var pedido = compras.confirmar(CORREO, tarjeta("4242 4242 4242 4242"), "PRUEBA10");

        assertThat(pedido.getEstado()).isEqualTo(EstadoPedido.PAGADO);
        assertThat(pedido.getNumero()).startsWith("BTQ-");
        assertThat(pedido.getDescuento()).isEqualByComparingTo("10000");
        assertThat(pedido.getTotal()).isEqualByComparingTo(new BigDecimal("93500"));
        assertThat(variantes.findById(variante.getId()).orElseThrow().getExistencias()).isEqualTo(3);
        assertThat(productos.findById(variante.getProducto().getIdProducto()).orElseThrow().getExistencias()).isEqualTo(3);
        assertThat(lineas.count()).isZero();
    }

    @Test
    void sinpeQuedaPendienteYRetiroNoCobraEnvio() {
        var datos = tarjeta(null);
        datos.setEntrega(Entrega.RETIRO);
        datos.setMetodoPago(MetodoPago.SINPE);
        datos.setComprobante("123456789");

        var pedido = compras.confirmar(CORREO, datos, null);

        assertThat(pedido.getEstado()).isEqualTo(EstadoPedido.PENDIENTE_VERIFICACION);
        assertThat(pedido.getEnvio()).isEqualByComparingTo("0");
        assertThat(pedido.getDireccion()).isNull();
    }

    @Test
    void pagoRechazadoNoDejaPedidoNiTocaInventario() {
        assertThatThrownBy(() -> compras.confirmar(CORREO, tarjeta("4000 0000 0000 0002"), null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(pedidos.count()).isZero();
        assertThat(variantes.findById(variante.getId()).orElseThrow().getExistencias()).isEqualTo(5);
        assertThat(lineas.count()).isEqualTo(1);
    }

    @Test
    void noVendeMasDeLoDisponible() {
        var clienta = clientas.findByCorreo(CORREO).orElseThrow();
        lineas.deleteAll();
        agregarAlCarrito(clienta, 6);

        assertThatThrownBy(() -> compras.confirmar(CORREO, tarjeta("4242424242424242"), null))
                .hasMessageContaining("existencias");

        assertThat(variantes.findById(variante.getId()).orElseThrow().getExistencias()).isEqualTo(5);
    }

    @Test
    void cuponVencidoNoAplicaDescuento() {
        var cupon = cupones.findByCodigoIgnoreCase("PRUEBA10").orElseThrow();
        cupon.setVencimiento(LocalDate.now().minusDays(1));
        cupones.save(cupon);

        var pedido = compras.confirmar(CORREO, tarjeta("4242424242424242"), "PRUEBA10");

        assertThat(pedido.getDescuento()).isEqualByComparingTo("0");
        assertThat(pedido.getCupon()).isNull();
    }

    @Test
    void cancelarDevuelveInventarioYBloqueaCambiosNoPermitidos() {
        var pedido = compras.confirmar(CORREO, tarjeta("4242424242424242"), null);

        assertThatThrownBy(() -> pedidoService.cambiarEstado(pedido.getId(), EstadoPedido.ENTREGADO))
                .hasMessageContaining("no está permitido");

        pedidoService.cambiarEstado(pedido.getId(), EstadoPedido.CANCELADO);

        assertThat(variantes.findById(variante.getId()).orElseThrow().getExistencias()).isEqualTo(5);
        assertThat(productos.findById(variante.getProducto().getIdProducto()).orElseThrow().getExistencias()).isEqualTo(5);
    }
}
