package com.boutique.service.pago;

import com.boutique.domain.CheckoutForm;
import com.boutique.domain.MetodoPago;
import java.math.BigDecimal;

/** Patrón Strategy: cada método de pago aplica sus reglas detrás de la misma interfaz. */
public interface ProcesadorPago {
    MetodoPago metodo();

    ResultadoPago procesar(CheckoutForm datos, BigDecimal total);
}
