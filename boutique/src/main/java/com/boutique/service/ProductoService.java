package com.boutique.service;

import com.boutique.domain.Producto;
import com.boutique.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarProductos(String texto) {
        return productoRepository.buscarProductos(texto);
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductosPorCategoria(Integer idCategoria) {
        return productoRepository
                .findByCategoriaIdCategoriaAndActivoTrueOrderByNombreAsc(idCategoria);
    }
}