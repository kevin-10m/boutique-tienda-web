# boutique-tienda-web
Aplicación web para Boutique, una tienda de ropa femenina ficticia creada como caso de estudio del curso.
El proyecto modela una tienda en línea donde las clientas pueden ver el catálogo, comprar prendas y dar seguimiento a sus pedidos, y donde el administrador gestiona productos, pedidos, cupones y reportes.

Funcionalidades previstas
Cuentas: registro, inicio y cierre de sesión, recuperación de contraseña.
Catálogo: filtros por categoría, talla y precio, ordenamiento, búsqueda, ofertas y guía de tallas.
Carrito y compra: selección de talla y color, cupones de descuento, datos de envío y método de entrega.
Pagos: tarjeta en modo de prueba y SINPE Móvil.
Pedidos: confirmación, historial, seguimiento por estados y cancelación de pedidos pendientes.
Administración: productos e inventario, pedidos, cupones y reportes de ventas.

Forma de trabajo con ramas
main: versión estable e integrada del proyecto. No se trabaja directamente aquí.
feature/...: rama utilizada para desarrollar una historia de usuario o funcionalidad.
fix/...: rama utilizada para corregir errores.
Pasos para trabajar
Actualizar la rama main.
Crear una nueva rama desde main para la funcionalidad que se va a desarrollar.
Realizar los cambios y hacer commits con mensajes claros.
Subir la rama a GitHub.
Crear un Pull Request hacia main.
Solicitar la revisión de otro integrante.

Una vez revisado y aprobado, realizar el merge hacia main.
Ejemplo de rama: feature/hu12-carrito
Ejemplos de commits: feat: agrega carrito de compras, fix: corrige total con cupón, docs: actualiza README

## Avance 2

### Requisitos y ejecución

Se necesita Java 21, Maven y MySQL.

1. Ejecutá `database/crear_base.sql` en MySQL una sola vez.
2. Desde la carpeta `boutique`, ejecutá `mvn spring-boot:run`.
3. Abrí http://localhost:8080. Hibernate crea las tablas y `data.sql` carga los datos de ejemplo.

Las pruebas automáticas se ejecutan con `mvn test` y usan una base temporal en memoria.

### Usuarios de prueba

| Rol | Correo | Contraseña |
| --- | --- | --- |
| Administrador | sofia.admin@boutique.test | Admin2026! |
| Clienta | ana.rodriguez@boutique.test | Clienta2026! |

### Datos para probar la compra

- Cupón vigente: `VERANO10`. Cupón vencido: `VENCIDO25`.
- Tarjeta aprobada: `4242 4242 4242 4242`. Tarjeta rechazada: `4000 0000 0000 0002`. Usá una fecha futura y cualquier CVV de 3 dígitos.
- SINPE Móvil: cualquier comprobante de al menos 6 dígitos. El pedido queda pendiente hasta que el administrador lo verifique.

La pasarela es simulada y los datos de tarjeta no se guardan. Para enviar correos reales, definí `BOUTIQUE_MAIL_HOST`, `BOUTIQUE_MAIL_USER` y `BOUTIQUE_MAIL_PASSWORD`. Sin esas variables, la confirmación aparece en la consola.

### Estado del backlog

| Historias | Estado |
| --- | --- |
| HU-01, 02, 04, 05, 07, 08, 11, 12, 13 | Implementadas |
| HU-10, 14, 15, 16, 17, 18, 19, 20, 26, 27, 28 | Implementadas |
| HU-03, 06, 09 | Pendientes de integrar al repositorio |
| HU-21, 22, 23, 24, 25, 29, 30 | Planificadas para la iteración 3 |

### Base de datos

Las ocho tablas son `categoria`, `producto`, `variante`, `clienta`, `linea_carrito`, `cupon`, `pedido` y `detalle_pedido`. La tabla transaccional es `pedido`, junto con su detalle.
