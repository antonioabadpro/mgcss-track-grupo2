public class Tecnico {
    boolean activo;

    public Tecnico tecnicoActivo(){
        activo = true;
    }

    public Tecnico tecnicoInactivo(){
        activo = false;
    }

    public void setEstado(boolean estado){
        this.estado = estado;
    }

    public boolean getEstado(){
        return estado;
    }
    
}
