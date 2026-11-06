package com.boutique.service.pago;

import com.boutique.domain.EstadoPedido;

public record ResultadoPago(EstadoPedido estadoInicial, String referencia, String comprobante) {
}
