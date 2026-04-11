package com.mgcss.domain;

public class Tecnico {
    private Long id;
    
    String nombre;
    int edad;
    boolean tecnicoActivo;

    public Tecnico() {
        this.tecnicoActivo = false;
    }

    public Tecnico(String nombre, int edad) {
        this.tecnicoActivo = false;
        this.nombre = nombre;
        this.edad = edad;
    }

    public Long getId() {
        return this.id;
    }

    public Tecnico(boolean estado) {
        this.tecnicoActivo = estado;
    }

    public boolean isTecnicoActivo(){
        return this.tecnicoActivo;
    }

    public void setTecnicoActivo(){
        this.tecnicoActivo = true;
    }

    public void setTecnicoInactivo(){
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
        if(edad < 18){
            throw new IllegalArgumentException("El tecnico NO puede ser menor de edad.");
        }
        
        this.edad = edad;
    }
}
