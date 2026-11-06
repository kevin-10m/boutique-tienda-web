package com.boutique.repository;

import com.boutique.domain.Cupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuponRepository extends JpaRepository<Cupon, Long> {
    Optional<Cupon> findByCodigoIgnoreCase(String codigo);

    List<Cupon> findAllByOrderByVencimientoDesc();
}
