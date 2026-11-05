package com.boutique.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MetodoPago {
    TARJETA("Tarjeta"), SINPE("SINPE Móvil");

    private final String etiqueta;
}
