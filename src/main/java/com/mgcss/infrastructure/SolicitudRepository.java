package com.mgcss.infrastructure;

import com.mgcss.domain.Solicitud;

import java.util.Optional;

public interface SolicitudRepository {
    Solicitud save(Solicitud solicitud);
    Optional findById(Long id);
}
