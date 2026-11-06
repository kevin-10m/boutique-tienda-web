# boutique-tienda-web
Aplicación web para una tienda de ropa ficticia (proyecto del curso).


Aplicación web para Boutique, una tienda de ropa femenina ficticia creada como caso de estudio del curso. El proyecto modela una tienda en línea donde las clientas pueden ver el catálogo, comprar prendas y dar seguimiento a sus pedidos, y donde el administrador gestiona productos, pedidos, cupones y reportes.

Funcionalidades principales:
Cuentas: registro, inicio y cierre de sesión, recuperación de contraseña.
Catálogo: filtros por categoría, talla y precio; ordenamiento; búsqueda; ofertas y guía de tallas.
Carrito y compra: selección de talla y color, cupones de descuento, datos de envío y método de entrega (envío o retiro en tienda).
Pagos: tarjeta (pasarela en modo de prueba) y SINPE Móvil con número de comprobante.
Pedidos: confirmación por correo, historial, seguimiento por estados y cancelación de pedidos pendientes.
Administración: productos e inventario, pedidos, cupones y reportes de ventas.

Forma de trabajo con ramas
Rama	Uso
main	Versión estable y entregable. No se trabaja directamente aquí.
develop	Rama donde se integra el trabajo del equipo.
feature/...	Una rama por historia o tarea, creada desde develop (ej. feature/hu12-carrito).
fix/...	Corrección de errores.

Pasos para trabajar:

Actualizar develop y crear una rama nueva: git checkout -b feature/hu12-carrito
Hacer commits con mensajes claros: feat: agrega filtro por talla, fix: corrige total con cupón, docs: actualiza README
Subir la rama: git push -u origin feature/hu12-carrito
Abrir un Pull Request hacia develop y pedir la revisión de otro integrante.
Al cerrar cada iteración, se une develop con main.



### Avance 2 · Cuenta y carrito

Esta rama añade el registro e inicio de sesión (HU-02), la selección de talla,
color y cantidad desde la ficha de producto (HU-11), el carrito persistente por
clienta (HU-12) y la modificación de cantidades y eliminación de artículos (HU-13).

Para ejecutar el proyecto se requiere Java 21, Maven y MySQL. Creá la base
`boutique_db` y un usuario con permisos sobre esa base. Definí
`SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` con tus credenciales.
Si cambiás el servidor o el nombre de la base, definí también `SPRING_DATASOURCE_URL`.
Desde la carpeta `boutique` que contiene el `pom.xml`, ejecutá
`mvn spring-boot:run` y abrí http://localhost:8080. Hibernate prepara las tablas
y el script de inicio carga los productos y las variantes de ejemplo.

Para probar el flujo, registrá una cuenta desde **Cuenta**, iniciá sesión y
abrí **Vestido Lino Alba** en el catálogo. Elegí M / Natural y agregá dos
unidades. En el carrito podés cambiar la cantidad, cerrar sesión y volver a
entrar para verificar la persistencia, y finalmente eliminar la prenda.
La combinación M / Negro está agotada. El envío estimado es configurable con
`BOUTIQUE_ENVIO_ESTANDAR` (₡2 500 por defecto y ₡0 con el carrito vacío).
