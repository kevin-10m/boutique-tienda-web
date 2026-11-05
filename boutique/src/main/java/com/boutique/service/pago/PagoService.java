package com.boutique.service.pago;

import com.boutique.domain.CheckoutForm;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagoService {
    private final List<ProcesadorPago> procesadores;

    public ResultadoPago cobrar(CheckoutForm datos, BigDecimal total) {
        return procesadores.stream()
                .filter(procesador -> procesador.metodo() == datos.getMetodoPago())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Elegí un método de pago."))
                .procesar(datos, total);
    }
}
