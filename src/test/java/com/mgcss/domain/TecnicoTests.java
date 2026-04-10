package com.mgcss.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TecnicoTests {
    @Test
    void testSoloTecnicoActivoASolicitud() {
        Tecnico tecnico = new tecnicoActivo();
        Solicitud solicitud = new Solicitud();
        solicitud.setTecnico(tecnico);
        assert(solicitud.getTecnico().getEstado() && "El tecnico es activo");
    }
    @Test
    void testSoloTecnicoInactivoASolicitud() {
        Tecnico tecnico = new tecnicoInactivo();
        Solicitud solicitud = new Solicitud();
        solicitud.setTecnico(tecnico);
        assertFalse(solicitud.getTecnico().getEstado() && "El tecnico es inactivo");
    }
}

    
