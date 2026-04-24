package com.mgcss.service;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.infraestructure.repository.SolicitudRepository;

/**
 * Clase SolicitudService que representa el servicio de solicitudes.
 */
public class SolicitudService {
	
	private final SolicitudRepository solicitudRepository;
	
	/**
	 * Constructor de la clase SolicitudService con parámetros
	 * @param solicitudRepository Repositorio de solicitudes
	 */
	public SolicitudService(SolicitudRepository solicitudRepository) {
		this.solicitudRepository = solicitudRepository;
	}
	
	/**
	 * Crea una solicitud
	 * @param solicitud Solicitud a crear
	 * @return Solicitud creada
	 */
	public Solicitud crearSolicitud(Solicitud solicitud) {
		return solicitudRepository.save(solicitud);
	}
	
	/**
	 * Asigna un técnico a una solicitud
	 * @param solicitudId ID de la solicitud
	 * @param tecnico Técnico a asignar
	 */
	public void asignarTecnico(Long solicitudId, Tecnico tecnico) {
		Solicitud solicitud = solicitudRepository.findById(solicitudId)
				.orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
		solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
		solicitud.setTecnico(tecnico);
		solicitudRepository.save(solicitud);
	}
	
	/**
	 * Cambia el estado de una solicitud
	 * @param solicitud Solicitud a cambiar el estado
	 * @param estado Estado al que se debe cambiar la solicitud
	 */
	public void cambiarEstado(Solicitud solicitud, EstadoSolicitud estado) {
		solicitud.setEstado(estado);
		solicitudRepository.save(solicitud);
	}
}
