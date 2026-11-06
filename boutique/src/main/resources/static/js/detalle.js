const variante = document.querySelector("#variante");
const cantidad = document.querySelector("#cantidad");
const agregar = document.querySelector("#agregar");

if (variante && agregar) {
    function actualizarSeleccion() {
        cantidad.max = variante.selectedOptions[0]?.dataset.stock || "0";
        agregar.disabled = !variante.value || !cantidad.checkValidity();
    }
    variante.addEventListener("change", actualizarSeleccion);
    cantidad.addEventListener("input", actualizarSeleccion);
    actualizarSeleccion();
}
