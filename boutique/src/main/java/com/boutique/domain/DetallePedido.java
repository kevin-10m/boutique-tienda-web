package com.boutique.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/** Guarda nombre y precio de la compra para que no cambien si luego se edita el producto. */
@Getter
@Setter
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_variante")
    private Variante variante;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private String talla;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;

    public BigDecimal getImporte() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
