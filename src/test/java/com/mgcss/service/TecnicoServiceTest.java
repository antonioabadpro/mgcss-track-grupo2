package com.mgcss.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import com.mgcss.domain.Tecnico;
import com.mgcss.infraestructure.repository.TecnicoRepository;

@Tag("service")
@ExtendWith(MockitoExtension.class)
class TecnicoServiceTest {

    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private TecnicoService tecnicoService;

    /**
     * Este test verifica que al crear un técnico esté en estado activo y se guarde correctamente en el repositorio.
     */
    @Test
    void testCrearTecnico() {
        Tecnico tecnico = new Tecnico("Paco López", 30);
        when(tecnicoRepository.save(tecnico)).thenReturn(tecnico);

        Tecnico resultado = tecnicoService.crearTecnico(tecnico);

        assertTrue(resultado.isTecnicoActivo(), "El técnico debe activarse al crearse");
        assertEquals("Paco López", resultado.getNombre());
        assertEquals(30, resultado.getEdad());
        verify(tecnicoRepository).save(tecnico);
    }

    /**
     * Este test verifica que se pueda listar todos los técnicos correctamente, y que el repositorio sea consultado para obtener la lista de técnicos.
     */
    @Test
    void testListarTecnicos() {
        List<Tecnico> lista = Arrays.asList(new Tecnico("Paco López", 30), new Tecnico("Ana Gómez", 25));
        when(tecnicoRepository.findAll()).thenReturn(lista);

        List<Tecnico> resultado = tecnicoService.listarTecnicos();

        assertEquals(2, resultado.size());
        verify(tecnicoRepository).findAll();
    }

    /**
     * Este test verifica que el estado de un técnico se pueda cambiar correctamente, tanto de activo a inactivo como de inactivo a activo.
     */
    @Test
    void cambiarEstadoTecnico() {
        Tecnico tecnico = new Tecnico("Paco López", 30);
        tecnico.setId(1L);
        tecnico.setTecnicoActivo();

        tecnicoService.cambiarEstado(tecnico, false);

        assertFalse(tecnico.isTecnicoActivo(), "El técnico debe estar inactivo después de cambiar su estado");
        verify(tecnicoRepository).save(tecnico);
    }

    /**
     * Este test verifica que se pueda consultar un técnico por su ID y que se retorne el técnico correcto. También verifica que se lance una excepción si el técnico no existe.
     */
    @Test
    void testConsultarTecnico_porId() {
        Tecnico tecnico = new Tecnico("Paco López", 30);
        tecnico.setId(1L);
        when(tecnicoRepository.findById(1L)).thenReturn(java.util.Optional.of(tecnico));

        Tecnico resultado = tecnicoService.consultarTecnico(1L);

        assertNotNull(resultado);
        assertEquals("Paco López", resultado.getNombre());
        verify(tecnicoRepository).findById(1L);
    }

    @Test
    void testConsultarTecnico_porId_NoExiste() {
        when(tecnicoRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> tecnicoService.consultarTecnico(1L), "Debe lanzar una excepción si el técnico no existe");
        verify(tecnicoRepository).findById(1L);
    }
}