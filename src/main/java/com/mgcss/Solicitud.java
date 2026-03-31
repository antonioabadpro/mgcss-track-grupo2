package com.mgcss;

import com.mgcss.domain.EstadoSolicitud;

public class Solicitud {

    private Long id;

    private EstadoSolicitud estado;
    private String fechaCreacion;

    public Solicitud() {
    }

    public Long getId() {
        return id;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void cerrar() {
        if (this.estado != EstadoSolicitud.EN_PROCESO) {
            this.estado = EstadoSolicitud.CERRADA;
        }
    }

}