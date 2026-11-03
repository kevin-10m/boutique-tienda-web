package com.boutique.controller;

import com.boutique.service.CategoriaService;
import com.boutique.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping
    public String listado(Model model) {

        var productos = productoService.getProductos();
        var categorias = categoriaService.getCategorias();

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);

        return "catalogo/listado";
    }
}