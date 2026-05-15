package com.mgcss.service;

import com.mgcss.domain.Tecnico;
import com.mgcss.infraestructure.repository.TecnicoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Clase TecnicoService que representa el servicio de técnicos.
 * Contiene la lógica de negocio relacionada con los técnicos.
 */
@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    /**
     * Constructor de la clase TecnicoService.
     *
     * @param tecnicoRepository Repositorio de técnicos
     */
    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    /**
     * Crea un nuevo técnico en la base de datos.
     *
     * @param tecnico Tecnico a crear
     * @return Tecnico creado
     */
    public Tecnico crearTecnico(Tecnico tecnico) {
        tecnico.setTecnicoActivo(); // Lo activamos por defecto al darle de alta
        return tecnicoRepository.save(tecnico);
    }

    /**
     * Consulta un técnico por su ID.
     * @param id ID del técnico a consultar
     * @return Técnico encontrado o excepción si no se encuentra
     */
    public Tecnico consultarTecnico(Long id) {
        return tecnicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
    }

    /**
     * Lista todos los técnicos disponibles en la base de datos.
     *
     * @return Lista de técnicos
     */
    public List<Tecnico> listarTecnicos() {
        return tecnicoRepository.findAll();
    }

    public void cambiarEstado(Tecnico tecnico, boolean esActivo) {
        if(tecnico.isTecnicoActivo() && !esActivo) {
            tecnico.setTecnicoInactivo();
        }
        else {
            if (!tecnico.isTecnicoActivo() && esActivo) {
                tecnico.setTecnicoActivo();
            }
        }
        tecnicoRepository.save(tecnico);
    }
}