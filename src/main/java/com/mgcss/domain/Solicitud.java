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

/**
 * Clase solicitud que representa una solicitud de servicio técnico.
 */
@Entity
@Table(name = "solicitud")
public class Solicitud {

    /**
     * Id autogenerado de la solicitud
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Estado de la solicitud
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    /**
     * Fecha de creación de la solicitud
     */
    @Column(nullable = false, length = 10)
    private String fechaCreacion;

    /**
     * Técnico asignado a la solicitud
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    /**
     * Constructor por defecto de la clase Solicitud
     */
    public Solicitud() {
        this.estado = EstadoSolicitud.ABIERTA;
        // Establece la fecha de creación al momento de instanciar la solicitud con el formato (DD/MM/YYYY)
        this.fechaCreacion = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.tecnico = null;
    }

    /**
     * Obtiene el id de la solicitud
     * @return Id de la solicitud
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Establece el id de la solicitud
     * @param id Id de la solicitud
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el estado de la solicitud
     * @return Estado de la solicitud
     */
    public EstadoSolicitud getEstado() {
        return this.estado;
    }

    /**
     * Obtiene el técnico asignado a la solicitud
     * @return Técnico asignado a la solicitud
     */
    public Tecnico getTecnico() {
        return this.tecnico;
    }

    /**
     * Establece el estado de la solicitud
     * @param estado Estado de la solicitud
     */
    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha de creación de la solicitud
     * @return Fecha de creación de la solicitud
     */
    public String getFechaCreacion() {
        return this.fechaCreacion;
    }

    /**
     * Establece la fecha de creación de la solicitud
     * @param fechaCreacion Fecha de creación de la solicitud
     * @return true si la fecha se estableció correctamente, false en caso contrario
     */
    public boolean setFechaCreacion(String fechaCreacion) {
        // Si la fecha tiene el formato (DD/MM/YYYY) se establece la fecha
        if (fechaCreacion.matches("\\d{2}/\\d{2}/\\d{4}")) {
            this.fechaCreacion = fechaCreacion;
            return true;
        }
        return false;
    }

    /**
     * Establece el técnico asignado a la solicitud
     * @param tecnico Técnico asignado a la solicitud
     * @return true si el técnico se estableció correctamente, false en caso contrario
     */
    public boolean setTecnico(Tecnico tecnico) {
        if (tecnico != null && tecnico.isTecnicoActivo()) {
            this.tecnico = tecnico;
            return true;
        }
        return false;
    }

    /**
     * Procesa la solicitud
     * @return true si la solicitud se procesó correctamente, false en caso contrario
     */
    public boolean procesarSolicitud() {
        if (this.estado == EstadoSolicitud.ABIERTA) {
            this.estado = EstadoSolicitud.EN_PROCESO;
            return true;
        }
        return false;
    }

    /**
     * Cierra la solicitud
     * @return true si la solicitud se cerró correctamente, false en caso contrario
     */
    public boolean cerrarSolicitud() {
        if (this.estado == EstadoSolicitud.EN_PROCESO) {
            this.estado = EstadoSolicitud.CERRADA;
            return true;
        }
        return false;
    }
}
