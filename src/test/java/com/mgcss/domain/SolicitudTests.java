package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mgcss.Solicitud;

@SpringBootTest
class SolicitudTests {

	@Test
	void testCerrarSolicitudAbierta() {
		Solicitud solicitud = new Solicitud();
		solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
		
		solicitud.cerrar();
		
		assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstado(), "La solicitud debería permanecer en estado 'En Proceso' después de cerrarla.");
	}

}
