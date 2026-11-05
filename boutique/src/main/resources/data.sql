INSERT IGNORE INTO categoria (id_categoria, nombre, activo) VALUES
(1, 'Vestidos', true),
(2, 'Blusas', true),
(3, 'Accesorios', true);

INSERT IGNORE INTO producto
(id_producto, nombre, descripcion, precio, existencias, imagen, activo, id_categoria)
VALUES
(1, 'Vestido Lino Alba',
 'Vestido midi en lino natural, corte recto y estilo elegante.',
 42500, 12,
 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=600',
 true, 1),

(2, 'Vestido Midi Rosé',
 'Vestido midi de estilo femenino en tonos suaves.',
 48000, 0,
 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=600',
 true, 1),

(3, 'Vestido Noche Terra',
 'Vestido largo elegante para ocasiones especiales.',
 68000, 6,
 'https://images.unsplash.com/photo-1596783074918-c84cb06531ca?w=600',
 true, 1),

(4, 'Blusa Seda Marfil',
 'Blusa ligera de estilo clásico en tono marfil.',
 28000, 8,
 'https://images.unsplash.com/photo-1571044420976-94b71786bae8?auto=format&fit=crop&q=80&w=600',
 true, 2),

(5, 'Blusa Algodón Sol',
 'Blusa cómoda de algodón para uso diario.',
 22500, 15,
 'https://images.unsplash.com/photo-1605763240000-7e93b172d754?w=600',
 true, 2),

(6, 'Aretes Oro Luna',
 'Aretes delicados para complementar cualquier conjunto.',
 15900, 24,
 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=600',
 true, 3),

(7, 'Bolso Cuero Nude',
 'Bolso en tono nude con diseño sencillo y elegante.',
 56000, 3,
 'https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=600',
 true, 3);

UPDATE producto
SET imagen = 'https://images.unsplash.com/photo-1571044420976-94b71786bae8?auto=format&fit=crop&q=80&w=600'
WHERE id_producto = 4;

-- Distribución de ejemplo del inventario existente por talla y color.
INSERT IGNORE INTO variante (id_producto, talla, color, existencias) VALUES
(1, 'S', 'Natural', 4), (1, 'M', 'Natural', 6), (1, 'L', 'Natural', 2),
(1, 'M', 'Negro', 0), (2, 'M', 'Rosado', 0),
(3, 'S', 'Terracota', 2), (3, 'M', 'Terracota', 4),
(4, 'S', 'Marfil', 2), (4, 'M', 'Marfil', 4), (4, 'L', 'Marfil', 2),
(5, 'S', 'Amarillo', 5), (5, 'M', 'Amarillo', 5), (5, 'L', 'Blanco', 5),
(6, 'Única', 'Dorado', 24), (7, 'Única', 'Nude', 3);

-- Productos con descuento activo. El valor 0 del panel se respeta en cada reinicio.
UPDATE producto SET descuento = 15 WHERE id_producto = 3 AND descuento IS NULL;
UPDATE producto SET descuento = 20 WHERE id_producto = 5 AND descuento IS NULL;
UPDATE producto SET descuento = 10 WHERE id_producto = 6 AND descuento IS NULL;
UPDATE producto SET descuento = 0 WHERE descuento IS NULL;

-- Cuentas de prueba, con datos ficticios.
INSERT IGNORE INTO clienta (correo, clave_hash, rol, nombre) VALUES
('sofia.admin@boutique.test', '{bcrypt}$2a$10$UIXi8RG5RFPLbG06OjVWpOAzEd4PtW.frfh52RRRpNFmyaEPjGOcK', 'ADMIN', 'Sofía Admin'),
('ana.rodriguez@boutique.test', '{bcrypt}$2a$10$oE7jYeixPiMEnWy4yg7.g.1AXvQHBaXq4CKzFH13Li/4oVN0t.hMy', 'CLIENTA', 'Ana Rodríguez Vargas');
