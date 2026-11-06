package com.boutique.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer existencias;

    @Column(length = 1024)
    private String imagen;

    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    /** Porcentaje de descuento activo; 0 significa que no está en oferta. */
    private Integer descuento = 0;

    public boolean isEnOferta() {
        return descuento != null && descuento > 0;
    }

    public BigDecimal getPrecioFinal() {
        var original = BigDecimal.valueOf(precio);
        if (!isEnOferta()) {
            return original;
        }
        return original.multiply(BigDecimal.valueOf(100 - descuento))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
    }
}
