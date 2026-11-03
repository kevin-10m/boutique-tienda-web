package com.boutique.repository;

import com.boutique.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrueOrderByNombreAsc();

    public List<Producto>
            findByNombreContainingIgnoreCaseAndActivoTrueOrderByNombreAsc(
                    String nombre);

    public List<Producto>
            findByCategoriaIdCategoriaAndActivoTrueOrderByNombreAsc(
                    Integer idCategoria);
}