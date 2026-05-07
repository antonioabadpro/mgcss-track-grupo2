package com.mgcss.infraestructure.repository;

import com.mgcss.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz SolicitudRepository que representa el repositorio de solicitudes.
 */
@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}
