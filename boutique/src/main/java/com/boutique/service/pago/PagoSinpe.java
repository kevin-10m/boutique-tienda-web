package com.boutique.service.pago;

import com.boutique.domain.CheckoutForm;
import com.boutique.domain.EstadoPedido;
import com.boutique.domain.MetodoPago;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/** SINPE Móvil solo registra el comprobante; el administrador lo verifica después. */
@Component
public class PagoSinpe implements ProcesadorPago {

    @Override
    public MetodoPago metodo() {
        return MetodoPago.SINPE;
    }

    @Override
    public ResultadoPago procesar(CheckoutForm datos, BigDecimal total) {
        String comprobante = datos.getComprobante() == null ? "" : datos.getComprobante().strip();
        if (!comprobante.matches("\\d{6,30}")) {
            throw new IllegalArgumentException("Ingresá un número de comprobante válido.");
        }
        return new ResultadoPago(EstadoPedido.PENDIENTE_VERIFICACION, null, comprobante);
    }
}
