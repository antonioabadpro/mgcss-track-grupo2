package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TecnicoTests {
    @Test
    void testSoloTecnicoActivoASolicitud() {
        Tecnico tecnico = new Tecnico(true);
        Solicitud solicitud = new Solicitud();
        solicitud.setTecnico(tecnico);

        assertTrue(solicitud.getTecnico().isTecnicoActivo() == true, "El tecnico es activo");
    }
    @Test
    void testSoloTecnicoInactivoASolicitud() {
        Tecnico tecnico = new Tecnico(false);
        Solicitud solicitud = new Solicitud();
        solicitud.setTecnico(tecnico);

        assertTrue(solicitud.getTecnico().isTecnicoActivo() == false, "El tecnico es inactivo");
    }
}

    
