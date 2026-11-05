package com.boutique.repository;

import com.boutique.domain.LineaCarrito;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;

public interface LineaCarritoRepository extends JpaRepository<LineaCarrito, Long> {
    List<LineaCarrito> findByClientaCorreoOrderByIdAsc(String correo);
    Optional<LineaCarrito> findByClientaIdAndVarianteId(Long clientaId, Long varianteId);
    Optional<LineaCarrito> findByIdAndClientaId(Long id, Long clientaId);

    @Query("select coalesce(sum(l.cantidad), 0) from LineaCarrito l where l.clienta.correo = :correo")
    long contarUnidades(String correo);
}
