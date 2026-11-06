package com.boutique.service;

import com.boutique.domain.Cupon;
import com.boutique.repository.CuponRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CuponService {
    private final CuponRepository cupones;

    public List<Cupon> listar() {
        return cupones.findAllByOrderByVencimientoDesc();
    }

    public Cupon buscar(Long id) {
        return cupones.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El cupón no existe."));
    }

    /** Devuelve el cupón solo si existe, está activo y no ha vencido. */
    public Optional<Cupon> buscarVigente(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return Optional.empty();
        }
        return cupones.findByCodigoIgnoreCase(codigo.strip())
                .filter(cupon -> cupon.isVigente(LocalDate.now()));
    }

    @Transactional
    public void guardar(Cupon cupon) {
        cupon.setCodigo(cupon.getCodigo().strip().toUpperCase(Locale.ROOT));
        cupones.saveAndFlush(cupon);
    }

    @Transactional
    public void cambiarActivo(Long id) {
        var cupon = buscar(id);
        cupon.setActivo(!cupon.isActivo());
    }
}
