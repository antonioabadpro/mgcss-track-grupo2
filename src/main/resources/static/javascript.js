const API_SOL = 'http://localhost:8080/api/solicitudes';
const API_TEC = 'http://localhost:8080/api/tecnicos';

let cacheSolicitudes = [];
let cacheTecnicos = [];
let modoPanelSol = 'CREAR'; // 'CREAR' o 'PROCESAR'
let idSolActiva = null;

// ==========================================
// UTILIDADES Y NAVEGACIÓN
// ==========================================
function avisar(mensaje, tipo) {
    const el = document.getElementById('notificacion');
    el.innerText = mensaje;
    el.className = `notificacion ${tipo}`;
    setTimeout(() => el.className = 'notificacion hidden', 5000);
}

function switchView(vista) {
    document.getElementById('navSolicitudes').classList.remove('active');
    document.getElementById('navTecnicos').classList.remove('active');
    
    document.getElementById('viewSolicitudes').classList.add('hidden');
    document.getElementById('viewTecnicos').classList.add('hidden');

    cerrarPanelSol();
    document.getElementById('panelCreacionTec').classList.add('hidden');
    document.getElementById('inputNombreTecnico').value = '';
    document.getElementById('inputEdadTecnico').value = '';

    if (vista === 'solicitudes') {
        document.getElementById('navSolicitudes').classList.add('active');
        document.getElementById('viewSolicitudes').classList.remove('hidden');
        document.getElementById('headerTitulo').innerText = "MGCSS - Gestión de Servicio Técnico";
        document.getElementById('headerSubtitulo').innerText = "Control de solicitudes de un Servicio Técnico en tiempo real";
    } else {
        document.getElementById('navTecnicos').classList.add('active');
        document.getElementById('viewTecnicos').classList.remove('hidden');
        document.getElementById('headerTitulo').innerText = "MGCSS - Plantilla de Técnicos";
        document.getElementById('headerSubtitulo').innerText = "Gestión del personal y altas en el sistema";
    }
    
    cargarDatos(); 
}

async function cargarDatos() {
    try {
        const [resSol, resTec] = await Promise.all([fetch(API_SOL), fetch(API_TEC)]);
        if (resSol.ok) cacheSolicitudes = await resSol.json();
        if (resTec.ok) cacheTecnicos = await resTec.json();
        
        renderizarSolicitudes();
        renderizarTecnicos();
    } catch (err) { 
        avisar("Error de conexión con el servidor.", "error"); 
    }
}

// ==========================================
// VISTA: SOLICITUDES (PANEL MULTIUSOS)
// ==========================================
function cerrarPanelSol() {
    document.getElementById('panelCreacionSol').classList.add('hidden');
}

function cargarSelectTecnicos(permitirVacio) {
    const select = document.getElementById('selectTecnicoCreacion');
    const msgAviso = document.getElementById('msgAvisoTecnicos');
    const btnGuardar = document.getElementById('btnGuardarSolicitud');
    
    select.innerHTML = '';
    
    // Si estamos en modo crear, permitimos "Sin asignar"
    if (permitirVacio) {
        select.innerHTML += `<option value="">-- Sin asignar (Quedará ABIERTA) --</option>`;
    }

    const activos = cacheTecnicos.filter(t => t.esActivo);
    
    if (activos.length === 0 && !permitirVacio) {
        select.classList.add('hidden');
        msgAviso.classList.remove('hidden');
        btnGuardar.disabled = true;
    } else {
        select.classList.remove('hidden');
        msgAviso.classList.add('hidden');
        btnGuardar.disabled = false;
        activos.forEach(t => {
            select.innerHTML += `<option value="${t.id}">${t.nombre} (ID: ${t.id})</option>`;
        });
    }
}

function abrirPanelCrear() {
    modoPanelSol = 'CREAR';
    idSolActiva = null;
    
    document.getElementById('tituloPanelSol').innerText = '📝 Registrar Nueva Avería';
    document.getElementById('btnGuardarSolicitud').innerText = 'Guardar Solicitud';
    
    const inputDesc = document.getElementById('inputDescripcion');
    inputDesc.value = '';
    inputDesc.readOnly = false;
    
    cargarSelectTecnicos(true); // Permitimos que la avería nazca sin técnico asignado
    
    document.getElementById('panelCreacionSol').classList.remove('hidden');
    inputDesc.focus();
}

function abrirPanelProcesar(id, descString) {
    modoPanelSol = 'PROCESAR';
    idSolActiva = id;
    
    document.getElementById('tituloPanelSol').innerText = `⚙️ Procesar Solicitud #${id}`;
    document.getElementById('btnGuardarSolicitud').innerText = 'Asignar y Procesar';
    
    const inputDesc = document.getElementById('inputDescripcion');
    inputDesc.value = descString || '';
    inputDesc.readOnly = true; // Bloqueamos la modificación de la descripción
    
    cargarSelectTecnicos(false); // NO permitimos vacío, hay que asignar a alguien
    
    document.getElementById('panelCreacionSol').classList.remove('hidden');
    window.scrollTo({ top: 0, behavior: 'smooth' }); // Subimos la pantalla suavemente
}

function ejecutarAccionPanelSol() {
    if (modoPanelSol === 'CREAR') {
        crearSolicitud();
    } else {
        asignarYProcesarSol();
    }
}

function toggleDescripcion(id) {
    document.getElementById(`desc-${id}`).classList.toggle('open');
}

function renderizarSolicitudes() {
    const tbody = document.getElementById('tablaSolicitudes');
    tbody.innerHTML = '';

    cacheSolicitudes.forEach(s => {
        const nombreTec = cacheTecnicos.find(t => t.id === s.tecnicoId)?.nombre || 'Sin asignar';
        
        // Evitamos que saltos de línea o comillas rompan el HTML del botón procesar
        const descSafe = s.descripcion ? s.descripcion.replace(/'/g, "\\'").replace(/"/g, "&quot;") : '';

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>#${s.id}</td>
            <td><span class="badge badge-${s.estado.toLowerCase().replace('_','')}">${s.estado}</span></td>
            <td>${s.fechaCreacion || '-'}</td>
            <td>${s.tecnicoId ? '👤 ' + nombreTec : '❌ Sin asignar'}</td>
            <td class="td-acciones">
                <div class="grupo-acciones">
                    <button class="btn-action" onclick="abrirPanelProcesar(${s.id}, '${descSafe}')">Procesar</button>
                    <button class="btn-action" onclick="actualizarEstado(${s.id}, 'CERRADA')">Cerrar</button>
                    <button class="btn-action" onclick="reabrir(${s.id})">Reabrir</button>
                </div>
                <button class="btn-action btn-detalle" onclick="toggleDescripcion(${s.id})">👁️ Ver Detalle</button>
            </td>
        `;
        tbody.appendChild(tr);

        const trDesc = document.createElement('tr');
        trDesc.id = `desc-${s.id}`;
        trDesc.className = 'desc-row';
        const textoDesc = s.descripcion ? s.descripcion : '<i>No se ha proporcionado descripción para esta avería.</i>';
        trDesc.innerHTML = `<td colspan="5" class="desc-cell"><strong>📄 Descripción del problema:</strong><br><br>${textoDesc}</td>`;
        tbody.appendChild(trDesc);
    });
}

// LOGICA DE LAS PETICIONES AL BACKEND
async function crearSolicitud() {
    const desc = document.getElementById('inputDescripcion').value.trim();
    const tecnicoId = document.getElementById('selectTecnicoCreacion').value;

    const bodyReq = {};
    if (desc) bodyReq.descripcion = desc;
    if (tecnicoId) bodyReq.tecnicoId = parseInt(tecnicoId);

    try {
        const res = await fetch(API_SOL, { 
            method: 'POST', 
            headers: {'Content-Type': 'application/json'}, 
            body: JSON.stringify(bodyReq)
        });
        if (res.ok) { 
            avisar("Solicitud creada con éxito", "exito"); 
            cerrarPanelSol(); 
            cargarDatos(); 
        }
    } catch (err) { avisar("Error al conectar con el servidor", "error"); }
}

async function asignarYProcesarSol() {
    const tecnicoId = document.getElementById('selectTecnicoCreacion').value;
    if (!tecnicoId) {
        avisar("Debes seleccionar un técnico para poder procesar la avería.", "error");
        return;
    }

    try {
        const res = await fetch(`${API_SOL}/${idSolActiva}/asignar-tecnico`, { 
            method: 'POST', 
            headers: {'Content-Type': 'application/json'}, 
            body: JSON.stringify({ tecnicoId: parseInt(tecnicoId) })
        });
        
        if (res.ok) { 
            avisar("Solicitud procesada y asignada al técnico.", "exito"); 
            cerrarPanelSol();
            cargarDatos(); 
        } else {
            avisar("Error de Regla de Negocio: Transición no válida.", "error");
        }
    } catch (err) { avisar("Error al conectar con el servidor", "error"); }
}

async function actualizarEstado(id, est) {
    const res = await fetch(`${API_SOL}/${id}/estado`, { method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({ estado: est }) });
    if (res.ok) cargarDatos(); else avisar("Regla de Negocio: Transición de estado no permitida.", "error");
}

async function reabrir(id) {
    const res = await fetch(`${API_SOL}/${id}/reabrir`, { method: 'PATCH' });
    if (res.ok) cargarDatos(); else avisar("Solo se pueden reabrir solicitudes CERRADAS.", "error");
}

// ==========================================
// VISTA: TÉCNICOS
// ==========================================
function togglePanelCreacionTec() {
    const panel = document.getElementById('panelCreacionTec');
    panel.classList.toggle('hidden');
    if (!panel.classList.contains('hidden')) {
        document.getElementById('inputNombreTecnico').focus();
    }
}

function renderizarTecnicos() {
    const tbody = document.getElementById('tablaTecnicos');
    tbody.innerHTML = '';

    cacheTecnicos.forEach(t => {
        const badgeClass = t.esActivo ? 'badge-abierta' : 'badge-cerrada';
        const textoEstado = t.esActivo ? 'ACTIVO' : 'INACTIVO';
        
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>#${t.id}</td>
            <td><strong>${t.nombre}</strong></td>
            <td>${t.edad} años</td>
            <td><span class="badge ${badgeClass}">${textoEstado}</span></td>
            <td style="text-align: right;">
                <button class="btn-action" onclick="toggleEstadoTecnico(${t.id})">🔄 Cambiar Estado</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

async function crearTecnico() {
    const nom = document.getElementById('inputNombreTecnico').value.trim();
    const ed = document.getElementById('inputEdadTecnico').value;
    
    if(!nom || !ed) { 
        avisar("Por favor, rellena todos los campos.", "error"); 
        return; 
    }

    try {
        const res = await fetch(API_TEC, {
            method: 'POST', 
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ nombre: nom, edad: parseInt(ed) })
        });
        
        if (res.ok) { 
            avisar("Técnico registrado correctamente", "exito"); 
            document.getElementById('inputNombreTecnico').value = ''; 
            document.getElementById('inputEdadTecnico').value = '';
            togglePanelCreacionTec(); 
            cargarDatos(); 
        } else { 
            avisar("Error: El técnico no puede ser menor de edad.", "error"); 
        }
    } catch (err) { avisar("Error de red", "error"); }
}

async function toggleEstadoTecnico(id) {
    try {
        const res = await fetch(`${API_TEC}/${id}/toggle-estado`, { method: 'PATCH' });
        if (res.ok) {
            cargarDatos(); 
        } else {
            avisar("Error al cambiar estado del técnico.", "error");
        }
    } catch (err) { avisar("Error de red", "error"); }
}

// === ARRANQUE INICIAL ===
document.addEventListener('DOMContentLoaded', cargarDatos);