package com.mgcss.domain;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
     * Historial de estados de la solicitud
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "solicitud_id")
    private List<EstadoHistorico> historicoEstados = new ArrayList<>();

    // ************************ METODOS AUXILIARES PRIVADOS ************************
    /**
     * Valida que la fecha tenga el formato (DD/MM/YYYY)
     * 
     * @param fecha Fecha cuyo formato queremos validar
     * @return Devuelve 'true' si la fecha es válida y 'false' en caso contrario
     */
    private boolean esFechaValida(String fecha) {
        return fecha != null && fecha.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    /**
     * Valida si la solicitud puede ser procesada (debe estar en estado 'Abierta')
     * 
     * @return Devuelve 'true' si la solicitud puede ser procesada y 'false' en caso
     *         contrario
     */
    private boolean puedeSerProcesada() {
        return this.estado == EstadoSolicitud.ABIERTA;
    }

    /**
     * Valida si la solicitud puede ser cerrada (debe estar en estado 'En Proceso')
     * 
     * @return Devuelve 'true' si la solicitud puede ser cerrada y 'false' en caso
     *         contrario
     */
    private boolean puedeSerCerrada() {
        return this.estado == EstadoSolicitud.EN_PROCESO;
    }

    /**
     * Constructor por defecto de la clase Solicitud
     */
    public Solicitud() {
        this.estado = EstadoSolicitud.ABIERTA;
        // Establece la fecha de creación al momento de instanciar la solicitud con el
        // formato (DD/MM/YYYY)
        this.fechaCreacion = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.tecnico = null;
        registrarCambioEstado(this.estado);
    }

    /**
     * Obtiene el id de la solicitud
     * 
     * @return Id de la solicitud
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Establece el id de la solicitud
     * 
     * @param id Id de la solicitud
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el estado de la solicitud
     * 
     * @return Estado de la solicitud
     */
    public EstadoSolicitud getEstado() {
        return this.estado;
    }

    /**
     * Obtiene el técnico asignado a la solicitud
     * 
     * @return Técnico asignado a la solicitud
     */
    public Tecnico getTecnico() {
        return this.tecnico;
    }

    /**
     * Obtiene el histórico de estados
     * 
     * @return Lista de estados históricos
     */
    public List<EstadoHistorico> getHistoricoEstados() {
        return this.historicoEstados;
    }

    /**
     * Establece el estado de la solicitud
     * 
     * @param estado Estado de la solicitud
     */
    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha de creación de la solicitud
     * 
     * @return Fecha de creación de la solicitud
     */
    public String getFechaCreacion() {
        return this.fechaCreacion;
    }

    /**
     * Establece la fecha de creación de la solicitud
     * 
     * @param fechaCreacion Fecha de creación de la solicitud
     * @return true si la fecha se estableció correctamente, false en caso contrario
     */
    public boolean setFechaCreacion(String fechaCreacion) {
        // Si la fecha tiene el formato (DD/MM/YYYY) se establece la fecha
        if (esFechaValida(fechaCreacion)) {
            this.fechaCreacion = fechaCreacion;
            return true;
        }
        return false;
    }

    /**
     * Establece el técnico asignado a la solicitud
     * 
     * @param tecnico Técnico asignado a la solicitud
     * @return true si el técnico se estableció correctamente, false en caso
     *         contrario
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
     * 
     * @return true si la solicitud se procesó correctamente, false en caso
     *         contrario
     */
    public boolean procesarSolicitud() {
        if (puedeSerProcesada()) {
            this.estado = EstadoSolicitud.EN_PROCESO;
            registrarCambioEstado(this.estado);
            return true;
        }
        return false;
    }

    /**
     * Cierra la solicitud
     * 
     * @return true si la solicitud se cerró correctamente, false en caso contrario
     */
    public boolean cerrarSolicitud() {
        if (puedeSerCerrada()) {
            this.estado = EstadoSolicitud.CERRADA;
            registrarCambioEstado(this.estado);
            return true;
        }
        return false;
    }

    /**
     * Reabre la solicitud
     * 
     * @return true si la solicitud se reabrió correctamente, false en caso
     *         contrario
     */
    public boolean reabrir() {
        if (this.estado == EstadoSolicitud.CERRADA) {
            this.estado = EstadoSolicitud.EN_PROCESO;
            registrarCambioEstado(this.estado);
            return true;
        }
        return false;
    }

    /**
     * Registra un cambio en el histórico de la solicitud
     */
    private void registrarCambioEstado(EstadoSolicitud nuevoEstado) {
        this.historicoEstados.add(new EstadoHistorico(nuevoEstado, LocalDateTime.now()));
    }
}
