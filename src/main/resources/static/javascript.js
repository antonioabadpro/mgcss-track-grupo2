const API_BASE = 'http://localhost:8080/api/solicitudes';

function avisar(mensaje, tipo) {
    const el = document.getElementById('notificacion');
    el.innerText = mensaje;
    el.className = `notificacion ${tipo}`;
    setTimeout(() => el.className = 'notificacion hidden', 5000);
}

// Abrir/Cerrar el panel superior de creación
function togglePanelCreacion() {
    const panel = document.getElementById('panelCreacion');
    panel.classList.toggle('hidden');
    if (!panel.classList.contains('hidden')) {
        document.getElementById('inputDescripcion').focus();
    }
}

// Abrir/Cerrar la fila de descripción en la tabla
function toggleDescripcion(id) {
    const filaDesc = document.getElementById(`desc-${id}`);
    filaDesc.classList.toggle('open');
}

// Cargar lista (GET)
async function cargarSolicitudes() {
    try {
        const res = await fetch(API_BASE);
        if (!res.ok) throw new Error("No se pudo cargar la lista");
        const datos = await res.json();
        
        const tbody = document.getElementById('tablaSolicitudes');
        tbody.innerHTML = '';

        datos.forEach(s => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${s.id}</td>
                <td><span class="badge badge-${s.estado.toLowerCase().replace('_','')}">${s.estado}</span></td>
                <td>${s.fechaCreacion || '-'}</td>
                <td>${s.tecnicoId ? '👤 ' + s.tecnicoId : '❌ Sin asignar'}</td>
                
                <td class="td-acciones">
                    <div class="grupo-acciones">
                        <button class="btn-action" onclick="asignarTecnico(${s.id})">Asignar</button>
                        <button class="btn-action" onclick="actualizarEstado(${s.id}, 'EN_PROCESO')">Procesar</button>
                        <button class="btn-action" onclick="actualizarEstado(${s.id}, 'CERRADA')">Cerrar</button>
                        <button class="btn-action" onclick="reabrir(${s.id})">Reabrir</button>
                    </div>
                    <button class="btn-action btn-detalle" onclick="toggleDescripcion(${s.id})">👁️ Ver Detalle</button>
                </td>
            `;
            tbody.appendChild(tr);

            // Fila oculta de descripción (Acordeón)
            const trDesc = document.createElement('tr');
            trDesc.id = `desc-${s.id}`;
            trDesc.className = 'desc-row';
            const textoDesc = s.descripcion ? s.descripcion : '<i>No se ha proporcionado descripción para esta avería.</i>';
            trDesc.innerHTML = `
                <td colspan="5" class="desc-cell">
                    <strong>📄 Descripción del problema:</strong><br><br>
                    ${textoDesc}
                </td>
            `;
            tbody.appendChild(trDesc);
        });
    } catch (err) {
        avisar("Error: No hay conexión con el servidor.", "error");
    }
}

// Crear (POST)
async function crearSolicitud() {
    const inputDesc = document.getElementById('inputDescripcion');
    const textoDescripcion = inputDesc.value.trim();
    
    const bodyRequest = textoDescripcion ? { descripcion: textoDescripcion } : {};

    try {
        const res = await fetch(API_BASE, { 
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(bodyRequest)
        });
        
        if (res.ok) {
            avisar("Solicitud creada con éxito", "exito");
            inputDesc.value = ''; 
            togglePanelCreacion(); // Ocultamos el panel tras crear
            cargarSolicitudes();
        }
    } catch (err) {
        avisar("Error al crear", "error");
    }
}

// Asignar Técnico (POST)
async function asignarTecnico(id) {
    const tecnicoId = prompt("Introduce el ID del técnico:");
    if (!tecnicoId) return;

    try {
        const res = await fetch(`${API_BASE}/${id}/asignar-tecnico`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ tecnicoId: parseInt(tecnicoId) })
        });
        if (res.ok) {
            avisar(`Técnico ${tecnicoId} asignado a la solicitud #${id}`, "exito");
            cargarSolicitudes();
        } else {
            avisar("Error: El técnico no existe o la solicitud no es válida.", "error");
        }
    } catch (err) { avisar("Error de conexión", "error"); }
}

// Cambiar Estado (PUT)
async function actualizarEstado(id, nuevoEstado) {
    try {
        const res = await fetch(`${API_BASE}/${id}/estado`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ estado: nuevoEstado })
        });

        if (res.ok) {
            avisar(`Estado de #${id} cambiado a ${nuevoEstado}`, "exito");
            cargarSolicitudes();
        } else {
            avisar(`Regla de Negocio: No se puede pasar a ${nuevoEstado} desde el estado actual.`, "error");
        }
    } catch (err) { avisar("Error de red", "error"); }
}

// Reabrir (PATCH)
async function reabrir(id) {
    try {
        const res = await fetch(`${API_BASE}/${id}/reabrir`, { method: 'PATCH' });
        if (res.ok) {
            avisar(`Solicitud #${id} reabierta`, "exito");
            cargarSolicitudes();
        } else {
            avisar("Solo se pueden reabrir solicitudes CERRADAS.", "error");
        }
    } catch (err) { avisar("Error de red", "error"); }
}

document.addEventListener('DOMContentLoaded', cargarSolicitudes);