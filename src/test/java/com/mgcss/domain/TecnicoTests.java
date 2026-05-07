package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("domain")
@SpringBootTest
class TecnicoTests {
    /**
     * Este test verifica que un técnico activo puede estar asignado a una solicitud.
     */
    @Test
    void testSoloTecnicoActivoASolicitud() {
        Tecnico tecnico = new Tecnico(true);
        Solicitud solicitud = new Solicitud();
        boolean isSetted = solicitud.setTecnico(tecnico);

        assertTrue(isSetted, "El tecnico es activo");
    }
    
    /**
     * Este test verifica que un técnico inactivo NO pueda estar asignado a una solicitud.
     */
    @Test
    void testSoloTecnicoInactivoASolicitud() {
        Tecnico tecnico = new Tecnico(false);
        Solicitud solicitud = new Solicitud();
        boolean isSetted = solicitud.setTecnico(tecnico);

        assertFalse(isSetted, "El tecnico es inactivo");
    }

    /**
     * Este test verifica que el estado de un técnico pueda cambiarse correctamente entre activo e inactivo.
     */
    @Test
    void testCambiarEstadoTecnico() {
        Tecnico tecnico = new Tecnico();
        assertFalse(tecnico.isTecnicoActivo(), "El tecnico debería ser inactivo por defecto");

        tecnico.setTecnicoActivo();
        assertTrue(tecnico.isTecnicoActivo(), "El tecnico debería ser activo después de llamar a setTecnicoActivo()");

        tecnico.setTecnicoInactivo();
        assertFalse(tecnico.isTecnicoActivo(), "El tecnico debería ser inactivo después de llamar a setTecnicoInactivo()");
    }

    /**
     * Este test verifica que se puedan establecer correctamente el nombre y la edad de un técnico.
     */
    @Test
    void testSetNombreYEdadTecnico() {
        Tecnico tecnico = new Tecnico();
        String nombre = "Juan Perez";
        int edad = 30;

        tecnico.setNombre(nombre);
        tecnico.setEdad(edad);

        assertEquals(nombre, tecnico.getNombre(), "El nombre del técnico debería ser 'Juan Perez'");
        assertEquals(edad, tecnico.getEdad(), "La edad del técnico debería ser 30");
    }

    /**
     * Este test verifica que NO el Tecnico NO puede ser menor de edad.
     * Si el Tecnico es menor de edad, se lanza una excepción.
     */
    @Test
    void verificarMayoriaEdadTecnico() {
        Tecnico tecnico = new Tecnico();
        int edad = 16;

        try {
            tecnico.setEdad(edad);
            fail("Debe ejecutarse la excepción 'IllegalArgumentException', ya que el Tecnico es menor de edad.");
        } catch (IllegalArgumentException e) {
            assertEquals("El tecnico NO puede ser menor de edad.", e.getMessage());
        }
    }

    /**
     * Este test verifica que el constructor con nombre y edad inicializa correctamente el técnico.
     */
    @Test
    void testConstructorNombreYEdad() {
        Tecnico tecnico = new Tecnico("Ana García", 25);

        assertEquals("Ana García", tecnico.getNombre(), "El nombre debería ser 'Ana García'");
        assertEquals(25, tecnico.getEdad(), "La edad debería ser 25");
    }

    /**
     * Este test verifica que getId() devuelve null cuando el técnico no ha sido persistido.
     */
    @Test
    void testGetIdEsNullSinPersistir() {
        Tecnico tecnico = new Tecnico();
        org.junit.jupiter.api.Assertions.assertNull(tecnico.getId(), "El ID debe ser null antes de persistir");
    }

    /**
     * Este test verifica que setTecnico con null devuelve false y no asigna técnico.
     */
    @Test
    void testSetTecnicoNullDevuelveFalse() {
        Solicitud solicitud = new Solicitud();
        boolean resultado = solicitud.setTecnico(null);

        assertFalse(resultado, "setTecnico(null) debe retornar false");
        org.junit.jupiter.api.Assertions.assertNull(solicitud.getTecnico(), "El técnico debe seguir siendo null");
    }
}
