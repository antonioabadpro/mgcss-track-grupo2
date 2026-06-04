package com.mgcss.service;

import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.TipoCliente;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final SolicitudService solicitudService;

    public DashboardService(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    public Map<String, Object> obtenerMetricas() {
        List<Solicitud> solicitudes = solicitudService.listarSolicitudes();
        
        long totalAbiertas = solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.ABIERTA).count();
        long totalEnProceso = solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.EN_PROCESO).count();
        long totalCerradas = solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.CERRADA).count();

        long incumplenSla = 0;
        double tiempoTotal = 0;
        int cerradasConFecha = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Solicitud s : solicitudes) {
            try {
                LocalDate fechaCreacion = LocalDate.parse(s.getFechaCreacion(), formatter);
                LocalDate fechaCierreOCualquier = s.getFechaCierre() != null 
                    ? LocalDate.parse(s.getFechaCierre(), formatter) 
                    : LocalDate.now();

                long diasTranscurridos = ChronoUnit.DAYS.between(fechaCreacion, fechaCierreOCualquier);

                if (diasTranscurridos > s.getTiempoMaximo()) {
                    incumplenSla++;
                }

                if (s.getEstado() == EstadoSolicitud.CERRADA && s.getFechaCierre() != null) {
                    tiempoTotal += diasTranscurridos;
                    cerradasConFecha++;
                }
            } catch (Exception e) {
                // Ignore parse errors for badly formatted dates in test data
            }
        }

        double tiempoMedio = cerradasConFecha > 0 ? (tiempoTotal / cerradasConFecha) : 0;

        Map<String, Object> metricas = new HashMap<>();
        metricas.put("totalAbiertas", totalAbiertas);
        metricas.put("totalEnProceso", totalEnProceso);
        metricas.put("totalCerradas", totalCerradas);
        metricas.put("totalSolicitudes", solicitudes.size());
        metricas.put("incumplenSla", incumplenSla);
        metricas.put("tiempoMedio", String.format("%.2f", tiempoMedio));
        
        // SLA info (solicitudes that are failing SLA)
        List<Solicitud> solicitudesIncumplen = solicitudes.stream().filter(s -> {
            try {
                LocalDate fechaCreacion = LocalDate.parse(s.getFechaCreacion(), formatter);
                LocalDate fechaCierreOCualquier = s.getFechaCierre() != null 
                    ? LocalDate.parse(s.getFechaCierre(), formatter) 
                    : LocalDate.now();
                return ChronoUnit.DAYS.between(fechaCreacion, fechaCierreOCualquier) > s.getTiempoMaximo();
            } catch(Exception e) { return false; }
        }).collect(Collectors.toList());

        metricas.put("solicitudesIncumplenSla", solicitudesIncumplen);

        return metricas;
    }
}
