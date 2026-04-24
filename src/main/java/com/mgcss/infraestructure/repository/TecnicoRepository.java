package com.mgcss.infraestructure.repository;

import com.mgcss.domain.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz TecnicoRepository que representa el repositorio de técnicos.
 */
@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
}
