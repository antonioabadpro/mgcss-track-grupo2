package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("domain")
@SpringBootTest
class SolicitudTests {

	/**
	 * Este test verifica que el estado de una solicitud 'Abierta' NO cambie al estado "Cerrada" al intentar cerrarla.
	 */
	@Test
	void testCerrarSolicitudAbierta() {
		Solicitud solicitud = new Solicitud();
		solicitud.setEstado(EstadoSolicitud.ABIERTA);
		
		boolean esCerrada = solicitud.cerrarSolicitud();
		
		assertEquals(EstadoSolicitud.ABIERTA, solicitud.getEstado(), "La solicitud no debería cerrarse si no está en estado 'En Proceso'.");
		assertEquals(false, esCerrada, "La Solicitud no debería ser cerrada.");
	}
	
	/**
	 * Este test verifica que el estado de una solicitud 'En Proceso' cambie al estado "Cerrada".
	 */
	@Test
	void testCerrarSolicitudEnProceso() {
		Solicitud solicitud = new Solicitud();
		solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
		
		boolean esCerrada = solicitud.cerrarSolicitud();
		
		assertEquals(EstadoSolicitud.CERRADA, solicitud.getEstado(), "La solicitud debería actualizarse al estado 'Cerrada' después de cerrarla.");
		assertEquals(true, esCerrada, "La Solicitud debería ser cerrada.");
	}

	/**
	 * Este test verifica que la fecha de creación de una solicitud se establezca correctamente al momento de instanciarla y que tenga el formato (DD/MM/YYYY).
	 */
	@Test
	void testFormatoFechaCreacion() {
		Solicitud solicitud = new Solicitud();
		String fechaCreacion = solicitud.getFechaCreacion();
		
		// Verificar que la fecha de creación tenga el formato (DD/MM/YYYY)
		assertEquals(10, fechaCreacion.length(), "La fecha de creación debería tener 10 caracteres.");
		assertEquals('/', fechaCreacion.charAt(2), "El tercer carácter debería ser '/'.");
		assertEquals('/', fechaCreacion.charAt(5), "El sexto carácter debería ser '/'.");
	}

	/**
	 * Este test verifica que se pueda establecer una nueva fecha de creación con el formato correcto (DD/MM/YYYY)
	 */
	@Test
	void testSetFechaCreacion() {
		Solicitud solicitud = new Solicitud();
		String nuevaFecha = "15/08/2024";
		boolean isSetted = solicitud.setFechaCreacion(nuevaFecha);
		
		assertEquals(nuevaFecha, solicitud.getFechaCreacion(), "La fecha de creación debería actualizarse al nuevo valor.");
		assertEquals(true, isSetted, "El método setFechaCreacion debería retornar true para una fecha con formato correcto.");
	}

	/**
	 * Este test verifica que NO se pueda establecer una nueva fecha de creación con un formato incorrecto (por ejemplo, YYYY-MM-DD)
	 */
	@Test
	void testSetFechaCreacionFormatoIncorrecto() {
		Solicitud solicitud = new Solicitud();
		String fechaInvalida = "2024-08-15";
		boolean isSetted = solicitud.setFechaCreacion(fechaInvalida);
		
		assertEquals(false, isSetted, "El método setFechaCreacion debería retornar false para una fecha con formato incorrecto.");
	}

	/**
	 * Este test verifica que el estado de una solicitud 'Abierta' cambie al estado "En Proceso" al procesarla.
	 */
	@Test
	void testProcesarSolicitud() {
		Solicitud solicitud = new Solicitud();
		solicitud.setEstado(EstadoSolicitud.ABIERTA);
		
		boolean esProcesada = solicitud.procesarSolicitud();
		
		assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstado(), "La solicitud debería actualizarse al estado 'En Proceso' después de procesarla.");
		assertEquals(true, esProcesada, "El método procesarSolicitud debería retornar true para una solicitud en estado 'Abierta'.");
	}

	/**
	 * Este test verifica que el estado de una solicitud 'En Proceso' NO puede procesarse porque ya esta en el estado 'En Proceso'
	 */
	@Test
	void testProcesarSolicitudEnProceso() {
		Solicitud solicitud = new Solicitud();
		solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
		
		boolean esProcesada = solicitud.procesarSolicitud();
		
		assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstado(), "La solicitud no debería cambiar de estado si no está en estado 'Abierta'.");
		assertEquals(false, esProcesada, "El método procesarSolicitud debería retornar false para una solicitud que no está en estado 'Abierta'.");
	}

	/**
	 * Este test verifica que el estado de una solicitud 'Cerrada' NO puede procesarse porque ya esta cerrada
	 */
	@Test
	void testProcesarSolicitudCerrada() {
		Solicitud solicitud = new Solicitud();
		solicitud.setEstado(EstadoSolicitud.CERRADA);
		
		boolean esProcesada = solicitud.procesarSolicitud();
		
		assertEquals(EstadoSolicitud.CERRADA, solicitud.getEstado(), "La solicitud no debería cambiar de estado si no está en estado 'Abierta'.");
		assertEquals(false, esProcesada, "El método procesarSolicitud debería retornar false para una solicitud que no está en estado 'Abierta'.");
	}

	/**
	 * Este test verifica que el estado de una solicitud 'Cerrada' cambie al estado "En Proceso" al reabrirla.
	 */
	@Test
	void testReabrirSolicitudCerrada() {
		Solicitud solicitud = new Solicitud();
		solicitud.setEstado(EstadoSolicitud.CERRADA);
		
		boolean esReabierta = solicitud.reabrir();
		
		assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstado(), "La solicitud debería actualizarse al estado 'En Proceso' después de reabrirla.");
		assertEquals(true, esReabierta, "La Solicitud debería ser reabierta.");
	}
}
