package com.mgcss.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entidad que representa el registro histórico de un cambio de estado.
 */
@Entity
@Table(name = "estado_historico")
public class EstadoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;
    
    private LocalDateTime fechaCambio;

    public EstadoHistorico() {
    }

    public EstadoHistorico(EstadoSolicitud estado, LocalDateTime fechaCambio) {
        this.estado = estado;
        this.fechaCambio = fechaCambio;
    }

    public Long getId() {
        return id;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }
}
