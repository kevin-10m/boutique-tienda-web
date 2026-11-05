package com.boutique.controller;

import com.boutique.domain.ProductoForm;
import com.boutique.service.CategoriaService;
import com.boutique.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/productos")
public class AdminProductoController {
    private final ProductoService productos;
    private final CategoriaService categorias;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("productos", productos.getProductos());
        return "admin/productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        var form = new ProductoForm();
        form.getVariantes().add(new ProductoForm.VarianteForm());
        return mostrar(form, model);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model) {
        return mostrar(productos.getFormulario(id), model);
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") ProductoForm form, BindingResult resultado,
                          Model model, RedirectAttributes mensajes) {
        if (resultado.hasErrors()) {
            return mostrar(form, model);
        }
        try {
            productos.guardar(form);
        } catch (IllegalArgumentException error) {
            model.addAttribute("errorProducto", error.getMessage());
            return mostrar(form, model);
        }
        mensajes.addFlashAttribute("mensaje", "Producto guardado.");
        return "redirect:/admin/productos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes mensajes) {
        productos.eliminar(id);
        mensajes.addFlashAttribute("mensaje", "Producto eliminado del catálogo.");
        return "redirect:/admin/productos";
    }

    private String mostrar(ProductoForm form, Model model) {
        model.addAttribute("producto", form);
        model.addAttribute("categorias", categorias.getCategorias());
        return "admin/producto-form";
    }
}
