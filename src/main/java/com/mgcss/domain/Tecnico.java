package com.mgcss.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Clase tecnico que representa un tecnico de servicio técnico.
 */
@Entity
@Table(name = "tecnico")
public class Tecnico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * Id autogenerado del técnico
     */
    private Long id;

    /**
     * Nombre del técnico
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Edad del técnico
     */
    @Column(nullable = false)
    private int edad;

    /**
     * Estado del técnico
     */
    @Column(nullable = false)
    private boolean tecnicoActivo;

    /**
     * Constructor por defecto de la clase Tecnico
     */
    public Tecnico() {
        this.tecnicoActivo = false;
    }

    /**
     * Constructor de la clase Tecnico con parámetros
     * @param nombre Nombre del técnico
     * @param edad Edad del técnico
     */
    public Tecnico(String nombre, int edad) {
        this.tecnicoActivo = false;
        this.nombre = nombre;
        this.edad = edad;
    }

    /**
     * Constructor de la clase Tecnico con parámetros
     * @param estado Estado del técnico
     */
    public Tecnico(boolean estado) {
        this.tecnicoActivo = estado;
    }

    /**
     * Obtiene el id del técnico
     * @return Id del técnico
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Establece el id del técnico
     * @param id Id del técnico
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el estado del técnico
     * @return Estado del técnico
     */
    public boolean isTecnicoActivo() {
        return this.tecnicoActivo;
    }

    /**
     * Establece el estado del técnico como activo
     */
    public void setTecnicoActivo() {
        this.tecnicoActivo = true;
    }

    /**
     * Establece el estado del técnico como inactivo
     */
    public void setTecnicoInactivo() {
        this.tecnicoActivo = false;
    }

    /**
     * Obtiene el nombre del técnico
     * @return Nombre del técnico
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Establece el nombre del técnico
     * @param nombre Nombre del técnico
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la edad del técnico
     * @return Edad del técnico
     */
    public int getEdad() {
        return this.edad;
    }

    /**
     * Establece la edad del técnico
     * @param edad Edad del técnico
     */
    public void setEdad(int edad) {
        if (edad < 18) {
            throw new IllegalArgumentException("El tecnico NO puede ser menor de edad.");
        }

        this.edad = edad;
    }
}
