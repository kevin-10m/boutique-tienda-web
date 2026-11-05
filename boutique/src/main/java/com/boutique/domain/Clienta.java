package com.boutique.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Clienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String correo;

    @Column(nullable = false)
    private String claveHash;

    @Column(nullable = false, columnDefinition = "varchar(20) default 'CLIENTA' not null")
    private String rol = "CLIENTA";

    private String nombre;
    private String telefono;
    private String provincia;
    private String canton;
    private String distrito;

    @Column(length = 300)
    private String direccion;
}
