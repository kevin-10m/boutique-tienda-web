package com.boutique.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Datos que el administrador captura para crear o editar un producto. */
@Getter
@Setter
public class ProductoForm {
    private Integer idProducto;

    @NotBlank @Size(max = 100)
    private String nombre;

    @NotBlank @Size(max = 500)
    private String descripcion;

    @NotNull @Positive
    private Double precio;

    @NotNull @Min(0) @Max(90)
    private Integer descuento = 0;

    @NotBlank @Size(max = 1024) @Pattern(regexp = "^https?://.+", message = "Usá un enlace http o https.")
    private String imagen;

    @NotNull
    private Integer idCategoria;

    @Valid
    private List<VarianteForm> variantes = new ArrayList<>();

    @Getter
    @Setter
    public static class VarianteForm {
        private Long id;

        @Size(max = 30)
        private String talla;

        @Size(max = 30)
        private String color;

        @Min(0)
        private Integer existencias;

        public boolean isVacia() {
            return id == null && (talla == null || talla.isBlank()) && (color == null || color.isBlank());
        }
    }
}
