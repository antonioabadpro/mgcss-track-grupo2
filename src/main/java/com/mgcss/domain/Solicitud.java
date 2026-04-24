package com.mgcss.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Column(nullable = false, length = 10)
    private String fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    public Solicitud() {
        this.estado = EstadoSolicitud.ABIERTA;
        this.fechaCreacion = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.tecnico = null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (fechaCreacion.matches("\\d{2}/\\d{2}/\\d{4}")) {
            this.fechaCreacion = fechaCreacion;
            return true;
        }
        return false;
    }

    public boolean setTecnico(Tecnico tecnico) {
        if (tecnico != null && tecnico.isTecnicoActivo()) {
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
