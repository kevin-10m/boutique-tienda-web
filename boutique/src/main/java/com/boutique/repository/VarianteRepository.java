package com.boutique.repository;

import com.boutique.domain.Variante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VarianteRepository extends JpaRepository<Variante, Long> {
    List<Variante> findByProductoIdProductoOrderByTallaAscColorAsc(Integer idProducto);
}
