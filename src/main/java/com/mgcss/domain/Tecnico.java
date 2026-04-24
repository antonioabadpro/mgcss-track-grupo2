package com.mgcss.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tecnicos")
public class Tecnico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false)
    private boolean tecnicoActivo;

    public Tecnico() {
        this.tecnicoActivo = false;
    }

    public Tecnico(String nombre, int edad) {
        this.tecnicoActivo = false;
        this.nombre = nombre;
        this.edad = edad;
    }

    public Tecnico(boolean estado) {
        this.tecnicoActivo = estado;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isTecnicoActivo() {
        return this.tecnicoActivo;
    }

    public void setTecnicoActivo() {
        this.tecnicoActivo = true;
    }

    public void setTecnicoInactivo() {
        this.tecnicoActivo = false;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return this.edad;
    }

    public void setEdad(int edad) {
        if (edad < 18) {
            throw new IllegalArgumentException("El tecnico NO puede ser menor de edad.");
        }

        this.edad = edad;
    }
}
