const formulario = document.querySelector("#formCompra");
const subtotal = Number(formulario.dataset.subtotal);
const envio = Number(formulario.dataset.envio);
const colones = (monto) => `₡ ${monto.toLocaleString("en-US").replaceAll(",", " ")}`;

function actualizarEntrega() {
    const esRetiro = document.querySelector("#entregaRetiro").checked;
    document.querySelector("#datosDireccion").hidden = esRetiro;
    document.querySelector("#horarioRetiro").hidden = !esRetiro;
    const costo = esRetiro ? 0 : envio;
    document.querySelector("#montoEnvio").textContent = colones(costo);
    document.querySelector("#montoTotal").textContent = colones(subtotal + costo);
}

function actualizarPago() {
    const esSinpe = document.querySelector("#pagoSinpe").checked;
    document.querySelector("#datosTarjeta").hidden = esSinpe;
    document.querySelector("#datosSinpe").hidden = !esSinpe;
}

document.querySelectorAll("input[name=entrega]")
    .forEach((opcion) => opcion.addEventListener("change", actualizarEntrega));
document.querySelectorAll("input[name=metodoPago]")
    .forEach((opcion) => opcion.addEventListener("change", actualizarPago));
actualizarEntrega();
actualizarPago();
