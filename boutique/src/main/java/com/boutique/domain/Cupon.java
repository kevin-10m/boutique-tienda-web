package com.boutique.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Entity
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]{3,20}", message = "Usá de 3 a 20 letras o números.")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @NotNull @Min(1) @Max(90)
    @Column(nullable = false)
    private Integer porcentaje;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate vencimiento;

    @Column(nullable = false)
    private boolean activo = true;

    public boolean isVigente(LocalDate hoy) {
        return activo && !vencimiento.isBefore(hoy);
    }
}
