package com.boutique.repository;

import com.boutique.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrueOrderByNombreAsc();

    public List<Producto> findByActivoTrueAndDescuentoGreaterThanOrderByNombreAsc(
            Integer descuento);

    public List<Producto>
            findByCategoriaIdCategoriaAndActivoTrueOrderByNombreAsc(
                    Integer idCategoria);

    @Query("""
           SELECT p
           FROM Producto p
           WHERE p.activo = true
           AND (
                LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                OR LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
           )
           ORDER BY p.nombre ASC
           """)
    public List<Producto> buscarProductos(
            @Param("texto") String texto);
}