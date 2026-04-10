package com.mgcss.domain;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Solicitud {

    private Long id;

    private EstadoSolicitud estado;
    private String fechaCreacion;
    private Tecnico tecnico;

    public Solicitud() {
        this.estado = EstadoSolicitud.ABIERTA;
        // Establece la fecha de creación al momento de instanciar la solicitud con el formato (DD/MM/YYYY)
        this.fechaCreacion = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.tecnico = null;
    }

    public Long getId() {
        return this.id;
    }

    public EstadoSolicitud getEstado() {
        return this.estado;
    }

    public Tecnico getTecnico() {
        return this.tecnico;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return this.fechaCreacion;
    }

    public boolean setFechaCreacion(String fechaCreacion) {
        // Si la fecha tiene el formato (DD/MM/YYYY) se establece la fecha
        if (fechaCreacion.matches("\\d{2}/\\d{2}/\\d{4}")) {
            this.fechaCreacion = fechaCreacion;
            return true;
        }
        return false;
    }

    public boolean setTecnico(Tecnico tecnico){

        if(tecnico.isTecnicoActivo() == true){
            this.tecnico = tecnico;
            return true;
        }
        return false;
    }

    public boolean procesarSolicitud() {
        if (this.estado == EstadoSolicitud.ABIERTA) {
            this.estado = EstadoSolicitud.EN_PROCESO;
            return true;
        }
        return false;
    }

    public boolean cerrarSolicitud() {
        if (this.estado == EstadoSolicitud.EN_PROCESO) {
            this.estado = EstadoSolicitud.CERRADA;
            return true;
        }
        return false;
    }

}