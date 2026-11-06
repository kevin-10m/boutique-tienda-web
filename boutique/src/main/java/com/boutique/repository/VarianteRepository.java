package com.boutique.repository;

import com.boutique.domain.Variante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VarianteRepository extends JpaRepository<Variante, Long> {
    List<Variante> findByProductoIdProductoOrderByTallaAscColorAsc(Integer idProducto);

    /** Solo descuenta si alcanza, para que dos compras simultáneas no vendan de más. */
    @Modifying
    @Query("""
           UPDATE Variante v SET v.existencias = v.existencias - :cantidad
           WHERE v.id = :id AND v.existencias >= :cantidad
           """)
    int descontar(@Param("id") Long id, @Param("cantidad") int cantidad);

    @Modifying
    @Query("UPDATE Variante v SET v.existencias = v.existencias + :cantidad WHERE v.id = :id")
    void reponer(@Param("id") Long id, @Param("cantidad") int cantidad);
}
