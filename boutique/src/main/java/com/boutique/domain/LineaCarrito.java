package com.boutique.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_clienta", "id_variante"}))
public class LineaCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_clienta")
    private Clienta clienta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_variante")
    private Variante variante;

    @Column(nullable = false)
    private int cantidad;

    public BigDecimal getImporte() {
        return variante.getProducto().getPrecioFinal()
                .multiply(BigDecimal.valueOf(cantidad));
    }
}
