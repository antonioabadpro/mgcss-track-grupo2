package com.mgcss.api.controller;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.TipoCliente;
import com.mgcss.api.dto.ClienteDTO;
import com.mgcss.service.ClienteService;
import com.mgcss.service.DashboardService;
import com.mgcss.service.SolicitudService;
import com.mgcss.service.TecnicoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mgcss.config.SecurityConfig;
import org.springframework.context.annotation.Import;

@WebMvcTest(WebController.class)
@Import(SecurityConfig.class)
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitudService solicitudService;

    @MockBean
    private TecnicoService tecnicoService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private DashboardService dashboardService;

    @Test
    @WithMockUser
    public void testIndexRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"));
    }

    @Test
    @WithMockUser
    public void testSolicitudesPage() throws Exception {
        when(solicitudService.listarSolicitudes()).thenReturn(new ArrayList<>());
        when(tecnicoService.listarTecnicos()).thenReturn(new ArrayList<>());
        when(clienteService.listarClientes()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(view().name("solicitudes"))
                .andExpect(model().attributeExists("solicitudes"))
                .andExpect(model().attributeExists("tecnicos"))
                .andExpect(model().attributeExists("clientes"));
    }

    @Test
    @WithMockUser
    public void testCrearSolicitudExito() throws Exception {
        ClienteDTO cliDto = new ClienteDTO();
        cliDto.setId(1L);
        cliDto.setNombre("Test");
        when(clienteService.consultarCliente(1L)).thenReturn(Optional.of(cliDto));

        mockMvc.perform(post("/solicitudes/crear")
                        .with(csrf())
                        .param("descripcion", "Test Desc")
                        .param("clienteId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"))
                .andExpect(flash().attributeExists("exito"));
    }

    @Test
    @WithMockUser
    public void testTecnicosPage() throws Exception {
        when(tecnicoService.listarTecnicos()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/tecnicos"))
                .andExpect(status().isOk())
                .andExpect(view().name("tecnicos"))
                .andExpect(model().attributeExists("tecnicos"));
    }

    @Test
    @WithMockUser
    public void testCrearTecnicoExito() throws Exception {
        mockMvc.perform(post("/tecnicos/crear")
                        .with(csrf())
                        .param("nombre", "Juan")
                        .param("edad", "30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tecnicos"))
                .andExpect(flash().attributeExists("exito"));
    }

    @Test
    @WithMockUser
    public void testClientesPage() throws Exception {
        when(clienteService.listarClientes()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(view().name("clientes"))
                .andExpect(model().attributeExists("clientes"));
    }

    @Test
    @WithMockUser
    public void testCrearClienteExito() throws Exception {
        mockMvc.perform(post("/clientes/crear")
                        .with(csrf())
                        .param("nombre", "ACME")
                        .param("email", "acme@test.com")
                        .param("tipoCliente", "PREMIUM"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clientes"))
                .andExpect(flash().attributeExists("exito"));
    }

    @Test
    @WithMockUser
    public void testDashboardPage() throws Exception {
        Map<String, Object> mockMetricas = new java.util.HashMap<>();
        mockMetricas.put("totalSolicitudes", 10);
        mockMetricas.put("totalAbiertas", 2);
        mockMetricas.put("totalEnProceso", 3);
        mockMetricas.put("totalCerradas", 5);
        mockMetricas.put("incumplenSla", 1);
        mockMetricas.put("tiempoMedio", "2,50");
        mockMetricas.put("solicitudesIncumplenSla", new ArrayList<>());

        when(dashboardService.obtenerMetricas()).thenReturn(mockMetricas);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("metricas"));
    }

    @Test
    @WithMockUser
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser
    public void testCrearSolicitudException() throws Exception {
        when(clienteService.consultarCliente(any())).thenThrow(new RuntimeException("Test Exception"));

        mockMvc.perform(post("/solicitudes/crear")
                        .param("descripcion", "Test")
                        .param("clienteId", "99")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @WithMockUser
    public void testProcesarSolicitudException() throws Exception {
        when(tecnicoService.consultarTecnico(any())).thenThrow(new RuntimeException("Test Exception"));

        mockMvc.perform(post("/solicitudes/1/procesar")
                        .param("tecnicoId", "99")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @WithMockUser
    public void testCerrarSolicitudException() throws Exception {
        when(solicitudService.consultarSolicitud(any())).thenThrow(new RuntimeException("Test Exception"));

        mockMvc.perform(post("/solicitudes/1/cerrar")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"))
                .andExpect(flash().attributeExists("error"));
    }
}
