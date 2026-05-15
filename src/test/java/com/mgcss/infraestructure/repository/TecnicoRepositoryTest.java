package com.mgcss.infraestructure.repository;

import com.mgcss.domain.Tecnico;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
@DataJpaTest
class TecnicoRepositoryTest {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    /**
     * Verifica que un técnico pueda ser guardado y recuperado correctamente de la base de datos.
     */
    @Test
    void testGuardarYRecuperarTecnico() {
        Tecnico tecnico = new Tecnico("María López", 28);
        tecnico.setTecnicoActivo();
        
        Tecnico tecnicoGuardado = tecnicoRepository.save(tecnico);
        
        assertNotNull(tecnicoGuardado.getId(), "El ID no debería ser nulo tras guardar");

        Optional<Tecnico> tecnicoRecuperado = tecnicoRepository.findById(tecnicoGuardado.getId());
        
        assertTrue(tecnicoRecuperado.isPresent(), "El técnico debería encontrarse en la base de datos");
        assertEquals(tecnicoGuardado.getId(), tecnicoRecuperado.get().getId(), "Los IDs deben coincidir");
        assertEquals(tecnicoGuardado.getNombre(), tecnicoRecuperado.get().getNombre(), "Los nombres deben coincidir");
        assertEquals(tecnicoGuardado.getEdad(), tecnicoRecuperado.get().getEdad(), "Las edades deben coincidir");
        assertEquals(tecnicoGuardado.isTecnicoActivo(), tecnicoRecuperado.get().isTecnicoActivo(), "El estado de actividad debe coincidir");
    }
}