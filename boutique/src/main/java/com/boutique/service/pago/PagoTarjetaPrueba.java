package com.boutique.service.pago;

import com.boutique.domain.CheckoutForm;
import com.boutique.domain.EstadoPedido;
import com.boutique.domain.MetodoPago;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** Pasarela simulada: valida la tarjeta sin cobrar ni guardar ninguno de sus datos. */
@Component
public class PagoTarjetaPrueba implements ProcesadorPago {
    static final String TARJETA_RECHAZADA = "4000000000000002";

    @Override
    public MetodoPago metodo() {
        return MetodoPago.TARJETA;
    }

    @Override
    public ResultadoPago procesar(CheckoutForm datos, BigDecimal total) {
        String numero = datos.getNumeroTarjeta() == null ? "" : datos.getNumeroTarjeta().replaceAll("[ -]", "");
        if (!numero.matches("\\d{13,19}") || !cumpleLuhn(numero)) {
            throw new IllegalArgumentException("Revisá el número de tarjeta.");
        }
        if (!vigente(datos.getVencimientoTarjeta())) {
            throw new IllegalArgumentException("Revisá la fecha de vencimiento de la tarjeta.");
        }
        if (datos.getCvv() == null || !datos.getCvv().matches("\\d{3,4}")) {
            throw new IllegalArgumentException("Revisá el código de seguridad de la tarjeta.");
        }
        if (TARJETA_RECHAZADA.equals(numero)) {
            throw new IllegalArgumentException("La pasarela de prueba rechazó la tarjeta.");
        }
        String referencia = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new ResultadoPago(EstadoPedido.PAGADO, referencia, null);
    }

    private boolean vigente(String vencimiento) {
        if (vencimiento == null || !vencimiento.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return false;
        }
        int mes = Integer.parseInt(vencimiento.substring(0, 2));
        int anio = 2000 + Integer.parseInt(vencimiento.substring(3));
        return !YearMonth.of(anio, mes).isBefore(YearMonth.now());
    }

    private boolean cumpleLuhn(String numero) {
        int suma = 0;
        boolean duplicar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = numero.charAt(i) - '0';
            if (duplicar) {
                digito = digito > 4 ? digito * 2 - 9 : digito * 2;
            }
            suma += digito;
            duplicar = !duplicar;
        }
        return suma % 10 == 0;
    }
}
