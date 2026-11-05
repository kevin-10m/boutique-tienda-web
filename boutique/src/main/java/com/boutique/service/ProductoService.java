package com.boutique.service;

import com.boutique.domain.Producto;
import com.boutique.domain.ProductoForm;
import com.boutique.domain.Variante;
import com.boutique.repository.CategoriaRepository;
import com.boutique.repository.ProductoRepository;
import com.boutique.repository.VarianteRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final VarianteRepository varianteRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public List<Producto> getOfertas() {
        return productoRepository
                .findByActivoTrueAndDescuentoGreaterThanOrderByNombreAsc(0);
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

    @Transactional(readOnly = true)
    public ProductoForm getFormulario(Integer idProducto) {
        var producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe."));
        var form = new ProductoForm();
        form.setIdProducto(producto.getIdProducto());
        form.setNombre(producto.getNombre());
        form.setDescripcion(producto.getDescripcion());
        form.setPrecio(producto.getPrecio());
        form.setDescuento(producto.isEnOferta() ? producto.getDescuento() : 0);
        form.setImagen(producto.getImagen());
        form.setIdCategoria(producto.getCategoria().getIdCategoria());
        for (var variante : varianteRepository
                .findByProductoIdProductoOrderByTallaAscColorAsc(idProducto)) {
            var fila = new ProductoForm.VarianteForm();
            fila.setId(variante.getId());
            fila.setTalla(variante.getTalla());
            fila.setColor(variante.getColor());
            fila.setExistencias(variante.getExistencias());
            form.getVariantes().add(fila);
        }
        return form;
    }

    /** Crea o actualiza el producto; sus existencias son la suma de las variantes. */
    @Transactional
    public void guardar(ProductoForm form) {
        var producto = form.getIdProducto() == null
                ? new Producto()
                : productoRepository.findById(form.getIdProducto())
                        .orElseThrow(() -> new IllegalArgumentException("El producto no existe."));
        var categoria = categoriaRepository.findById(form.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Elegí una categoría válida."));
        var filas = form.getVariantes().stream().filter(fila -> !fila.isVacia()).toList();
        if (filas.isEmpty()) {
            throw new IllegalArgumentException("Agregá al menos una talla y color.");
        }

        producto.setNombre(form.getNombre().strip());
        producto.setDescripcion(form.getDescripcion().strip());
        producto.setPrecio(form.getPrecio());
        producto.setDescuento(form.getDescuento());
        producto.setImagen(form.getImagen().strip());
        producto.setCategoria(categoria);
        producto.setActivo(true);
        producto.setExistencias(0);
        productoRepository.save(producto);

        int total = 0;
        var combinaciones = new HashSet<String>();
        for (var fila : filas) {
            if (fila.getTalla() == null || fila.getTalla().isBlank()
                    || fila.getColor() == null || fila.getColor().isBlank()
                    || fila.getExistencias() == null) {
                throw new IllegalArgumentException("Completá talla, color y existencias en cada fila.");
            }
            String talla = fila.getTalla().strip();
            String color = fila.getColor().strip();
            if (!combinaciones.add((talla + "|" + color).toLowerCase())) {
                throw new IllegalArgumentException("La combinación " + talla + " · " + color + " está repetida.");
            }
            var variante = fila.getId() == null
                    ? new Variante()
                    : varianteRepository.findById(fila.getId())
                            .filter(v -> v.getProducto().getIdProducto().equals(producto.getIdProducto()))
                            .orElseThrow(() -> new IllegalArgumentException("La variante no pertenece al producto."));
            variante.setProducto(producto);
            variante.setTalla(talla);
            variante.setColor(color);
            variante.setExistencias(fila.getExistencias());
            varianteRepository.save(variante);
            total += fila.getExistencias();
        }
        producto.setExistencias(total);
    }

    /** Oculta el producto sin borrarlo para conservar el historial de pedidos. */
    @Transactional
    public void eliminar(Integer idProducto) {
        productoRepository.findById(idProducto).ifPresent(p -> p.setActivo(false));
    }
}
