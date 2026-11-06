const contenedor = document.querySelector("#variantes");

document.querySelector("#agregarVariante").addEventListener("click", () => {
    const indice = contenedor.querySelectorAll(".fila-variante").length;
    const campo = (nombre, texto, tipo) => `
        <div class="col-4">
            <input name="variantes[${indice}].${nombre}" type="${tipo}" class="form-control"
                   placeholder="${texto}" aria-label="${texto}" ${tipo === "number" ? 'min="0"' : 'maxlength="30"'}>
        </div>`;
    contenedor.insertAdjacentHTML("beforeend", `
        <div class="row g-2 mb-2 fila-variante">
            ${campo("talla", "Talla", "text")}${campo("color", "Color", "text")}${campo("existencias", "Existencias", "number")}
        </div>`);
});
