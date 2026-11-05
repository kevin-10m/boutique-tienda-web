package com.boutique.domain;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Datos del formulario de compra. No genera toString para evitar que los datos
 * de tarjeta terminen en un registro; esos campos nunca se guardan.
 */
@Getter
@Setter
public class CheckoutForm {
    @NotNull
    private Entrega entrega = Entrega.DOMICILIO;

    @NotBlank(message = "Ingresá tu nombre.") @Size(max = 120)
    private String nombre;

    @NotBlank(message = "Ingresá tu teléfono.")
    @Pattern(regexp = "^[+0-9 ()-]{8,20}$", message = "Ingresá un teléfono válido.")
    private String telefono;

    @Size(max = 60)
    private String provincia;

    @Size(max = 60)
    private String canton;

    @Size(max = 60)
    private String distrito;

    @Size(max = 300)
    private String direccion;

    private boolean guardarDireccion;

    @NotNull
    private MetodoPago metodoPago = MetodoPago.TARJETA;

    @Size(max = 30)
    private String comprobante;

    private String numeroTarjeta;
    private String vencimientoTarjeta;
    private String cvv;
}
