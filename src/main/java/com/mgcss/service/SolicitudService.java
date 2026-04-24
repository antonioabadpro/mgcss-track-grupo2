package com.mgcss.service;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.infraestructure.repository.SolicitudRepository;

public class SolicitudService {
	
	private final SolicitudRepository solicitudRepository;
	
	public SolicitudService(SolicitudRepository solicitudRepository) {
		this.solicitudRepository = solicitudRepository;
	}
	
	public Solicitud crearSolicitud(Solicitud solicitud) {
		return solicitudRepository.save(solicitud);
	}
	
	public void asignarTecnico(Long solicitudId, Tecnico tecnico) {
		Solicitud solicitud = solicitudRepository.findById(solicitudId)
				.orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
		solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
		solicitud.setTecnico(tecnico);
		solicitudRepository.save(solicitud);
	}
	
	public void cambiarEstado(Solicitud solicitud, EstadoSolicitud estado) {
		solicitud.setEstado(estado);
		solicitudRepository.save(solicitud);
	}
}
