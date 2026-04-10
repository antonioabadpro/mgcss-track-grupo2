package com.mgcss.domain;

public class Tecnico {
    boolean tecnicoActivo;

    public Tecnico(boolean estado) {
        this.tecnicoActivo = estado;
    }

    public void setTecnicoActivo(){
        this.tecnicoActivo = true;
    }

    public void setTecnicoInactivo(){
        this.tecnicoActivo = false;
    }

    public boolean isTecnicoActivo(){
        return this.tecnicoActivo;
    }
    
}
