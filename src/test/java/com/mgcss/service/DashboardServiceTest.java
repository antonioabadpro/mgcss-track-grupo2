package com.mgcss.service;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.TipoCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DashboardServiceTest {

    @Mock
    private SolicitudService solicitudService;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerMetricasVacio() {
        when(solicitudService.listarSolicitudes()).thenReturn(new ArrayList<>());

        Map<String, Object> metricas = dashboardService.obtenerMetricas();

        assertEquals(0L, metricas.get("totalAbiertas"));
        assertEquals(0L, metricas.get("totalEnProceso"));
        assertEquals(0L, metricas.get("totalCerradas"));
        assertEquals(0, metricas.get("totalSolicitudes"));
        assertEquals(0L, metricas.get("incumplenSla"));
        assertEquals("0,00", ((String) metricas.get("tiempoMedio")).replace('.', ','));
        assertEquals(0, ((List<?>) metricas.get("solicitudesIncumplenSla")).size());
    }

    @Test
    public void testObtenerMetricasConDatos() {
        List<Solicitud> solicitudes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Solicitud 1: Abierta, Cumple SLA (Premium: 10 días, han pasado 2)
        Solicitud s1 = new Solicitud();
        s1.setEstado(EstadoSolicitud.ABIERTA);
        s1.setFechaCreacion(LocalDate.now().minusDays(2).format(formatter));
        Cliente c1 = new Cliente();
        c1.setTipoCliente(TipoCliente.PREMIUM);
        s1.setCliente(c1);
        solicitudes.add(s1);

        // Solicitud 2: Cerrada, Cumple SLA (Premium: 10 días, tardó 5 días)
        Solicitud s2 = new Solicitud();
        s2.setEstado(EstadoSolicitud.CERRADA);
        s2.setFechaCreacion(LocalDate.now().minusDays(10).format(formatter));
        s2.setFechaCierre(LocalDate.now().minusDays(5).format(formatter));
        s2.setCliente(c1);
        solicitudes.add(s2);

        // Solicitud 3: En proceso, Incumple SLA (Standard: 30 días, han pasado 40)
        Solicitud s3 = new Solicitud();
        s3.setEstado(EstadoSolicitud.EN_PROCESO);
        s3.setFechaCreacion(LocalDate.now().minusDays(40).format(formatter));
        Cliente c2 = new Cliente();
        c2.setTipoCliente(TipoCliente.STANDARD);
        s3.setCliente(c2);
        solicitudes.add(s3);

        when(solicitudService.listarSolicitudes()).thenReturn(solicitudes);

        Map<String, Object> metricas = dashboardService.obtenerMetricas();

        assertEquals(1L, metricas.get("totalAbiertas"));
        assertEquals(1L, metricas.get("totalEnProceso"));
        assertEquals(1L, metricas.get("totalCerradas"));
        assertEquals(3, metricas.get("totalSolicitudes"));
        // s1: 2 días (máx 1) -> incumple
        // s2: 5 días (máx 1) -> incumple
        // s3: 40 días (máx 5) -> incumple
        assertEquals(3L, metricas.get("incumplenSla"));
        // tiempo medio = 5 días (solo la solicitud 2 se cerró)
        assertEquals("5,00", ((String) metricas.get("tiempoMedio")).replace('.', ','));
        assertEquals(3, ((List<?>) metricas.get("solicitudesIncumplenSla")).size());
    }

    @Test
    public void testObtenerMetricasFechasMalFormateadas() {
        List<Solicitud> solicitudes = new ArrayList<>();

        Solicitud s1 = new Solicitud();
        s1.setEstado(EstadoSolicitud.CERRADA);
        s1.setFechaCreacion("fecha-invalida");
        s1.setFechaCierre("otra-invalida");
        solicitudes.add(s1);

        when(solicitudService.listarSolicitudes()).thenReturn(solicitudes);

        Map<String, Object> metricas = dashboardService.obtenerMetricas();

        assertEquals(1L, metricas.get("totalCerradas"));
        assertEquals(0L, metricas.get("incumplenSla"));
        assertEquals("0,00", ((String) metricas.get("tiempoMedio")).replace('.', ','));
    }
}
