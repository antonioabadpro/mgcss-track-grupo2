package com.mgcss.api.controller;

import com.mgcss.domain.Tecnico;
import com.mgcss.service.TecnicoService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("api")
@WebMvcTest(TecnicoController.class)
class TecnicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TecnicoService tecnicoService;

    @Test
    void testCrearTecnico_Valido() throws Exception {
        Tecnico tecnico = new Tecnico("Paco García", 30);
        tecnico.setId(1L);
        tecnico.setTecnicoActivo();

        when(tecnicoService.crearTecnico(any(Tecnico.class))).thenReturn(tecnico);

        mockMvc.perform(post("/api/tecnicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Paco García\", \"edad\": 30}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Paco García"))
                .andExpect(jsonPath("$.esActivo").value(true));
    }

    @Test
    void testCrearTecnico_MenorDeEdad() throws Exception {
        when(tecnicoService.crearTecnico(any(Tecnico.class)))
                .thenThrow(new IllegalArgumentException("El tecnico NO puede ser menor de edad."));

        mockMvc.perform(post("/api/tecnicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Niño\", \"edad\": 15}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListarTecnicos() throws Exception {
        Tecnico tecnico = new Tecnico("Ana Gómez", 25);
        tecnico.setId(1L);
        tecnico.setTecnicoActivo();
        
        when(tecnicoService.listarTecnicos()).thenReturn(Arrays.asList(tecnico));

        mockMvc.perform(get("/api/tecnicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ana Gómez"))
                .andExpect(jsonPath("$[0].esActivo").value(true))
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}