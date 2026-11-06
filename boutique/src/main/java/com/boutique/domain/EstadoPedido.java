package com.boutique.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** El orden de las constantes es el orden de avance del pedido. */
@Getter
@RequiredArgsConstructor
public enum EstadoPedido {
    PENDIENTE_VERIFICACION("Pendiente de verificación", "text-bg-secondary"),
    PAGADO("Pagado", "text-bg-warning"),
    ENVIADO("Enviado", "text-bg-danger"),
    ENTREGADO("Entregado", "text-bg-success"),
    CANCELADO("Cancelado", "text-bg-dark");

    private final String etiqueta;
    private final String estilo;

    public List<EstadoPedido> getSiguientes() {
        return switch (this) {
            case PENDIENTE_VERIFICACION -> List.of(PAGADO, CANCELADO);
            case PAGADO -> List.of(ENVIADO, CANCELADO);
            case ENVIADO -> List.of(ENTREGADO);
            case ENTREGADO, CANCELADO -> List.of();
        };
    }
}
