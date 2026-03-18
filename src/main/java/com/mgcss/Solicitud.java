package com.mgcss;

public class Solicitud {

    private Long id;

    private int estado;
    private String fechaCreacion;

    public Solicitud() {
    }

    public Long getId() {
        return id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}