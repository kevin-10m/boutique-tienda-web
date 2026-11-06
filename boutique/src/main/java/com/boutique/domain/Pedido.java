package com.boutique.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Transacción principal del sistema: cada compra confirmada por una clienta. */
@Getter
@Setter
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 30)
    private String numero;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_clienta")
    private Clienta clienta;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Entrega entrega;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String telefono;

    private String provincia;
    private String canton;
    private String distrito;

    @Column(length = 300)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MetodoPago metodoPago;

    private String comprobante;
    private String referenciaPago;
    private String cupon;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal descuento;

    @Column(nullable = false)
    private BigDecimal envio;

    @Column(nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles = new ArrayList<>();
}
