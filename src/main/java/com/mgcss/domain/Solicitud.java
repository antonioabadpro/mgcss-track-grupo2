package com.mgcss.domain;

public class Solicitud {

    private Long id;

    private EstadoSolicitud estado;
    private String fechaCreacion;
    private Tecnico tecnico;

    public Solicitud() {
    }

    public Long getId() {
        return id;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public Tecnico getTecnico() {
        return tecnico;
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

    public boolean setTecnico(Tecnico tecnico){

        if(tecnico.isTecnicoActivo() == true){
            this.tecnico = tecnico;
            return true;
        }
        else
            return false;
    }

    public void cerrar() {
        if (this.estado != EstadoSolicitud.EN_PROCESO) {
            this.estado = EstadoSolicitud.CERRADA;
        }
    }

}