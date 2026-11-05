package com.boutique.controller;

import com.boutique.service.CategoriaService;
import com.boutique.service.CarritoService;
import com.boutique.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final CarritoService carritoService;

    @GetMapping
    public String listado(Model model) {

        var productos = productoService.getProductos();
        var categorias = categoriaService.getCategorias();

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);

        return "catalogo/listado";
    }

    @GetMapping("/ofertas")
    public String ofertas(Model model) {
        model.addAttribute("productos", productoService.getOfertas());
        return "catalogo/ofertas";
    }

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(name = "texto", required = false, defaultValue = "")
            String texto,
            Model model) {

        var productos = texto.isBlank()
                ? java.util.Collections.emptyList()
                : productoService.buscarProductos(texto);

        model.addAttribute("productos", productos);
        model.addAttribute("texto", texto);

        return "catalogo/buscar";
    }

    @GetMapping("/producto/{idProducto}")
    public String detalle(
            @PathVariable Integer idProducto,
            Model model) {

        var productoOptional =
                productoService.getProducto(idProducto);

        if (productoOptional.isEmpty()
                || !Boolean.TRUE.equals(productoOptional.get().getActivo())) {
            return "redirect:/catalogo";
        }

        var producto = productoOptional.get();

        var recomendados =
                productoService
                        .getProductosPorCategoria(
                                producto.getCategoria().getIdCategoria())
                        .stream()
                        .filter(p ->
                                !p.getIdProducto().equals(idProducto))
                        .limit(3)
                        .toList();

        model.addAttribute("producto", producto);
        model.addAttribute("variantes", carritoService.opciones(idProducto));
        model.addAttribute("recomendados", recomendados);

        return "catalogo/detalle";
    }
}