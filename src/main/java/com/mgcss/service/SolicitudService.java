package com.mgcss.service;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.infraestructure.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Clase SolicitudService que representa el servicio de solicitudes.
 */
@Service
public class SolicitudService {

	private final SolicitudRepository solicitudRepository;

	/**
	 * Constructor de la clase SolicitudService con parámetros
	 * 
	 * @param solicitudRepository Repositorio de solicitudes
	 */
	public SolicitudService(SolicitudRepository solicitudRepository) {
		this.solicitudRepository = solicitudRepository;
	}

	/**
	 * Crea una solicitud
	 * 
	 * @param solicitud Solicitud a crear
	 * @return Solicitud creada
	 */
	public Solicitud crearSolicitud(Solicitud solicitud) {
		return solicitudRepository.save(solicitud);
	}

	/**
	 * Asigna un técnico a una solicitud
	 * 
	 * @param solicitudId ID de la solicitud
	 * @param tecnico     Técnico a asignar
	 */
	public void asignarTecnico(Long solicitudId, Tecnico tecnico) {
		Solicitud solicitud = consultarSolicitud(solicitudId);
		solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
		solicitud.setTecnico(tecnico);
		solicitudRepository.save(solicitud);
	}

	/**
	 * Cambia el estado de una solicitud
	 * 
	 * @param solicitud Solicitud a cambiar el estado
	 * @param estado    Estado al que se debe cambiar la solicitud
	 */
	public void cambiarEstado(Solicitud solicitud, EstadoSolicitud estado) {
		solicitud.setEstado(estado);
		solicitudRepository.save(solicitud);
	}

	/**
	 * Consulta una solicitud por ID
	 * 
	 * @param id ID de la solicitud
	 * @return Solicitud encontrada
	 */
	public Solicitud consultarSolicitud(Long id) {
		return solicitudRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("La solicitud no existe"));
	}

	/**
	 * Lista todas las solicitudes
	 * 
	 * @return Lista de solicitudes
	 */
	public List<Solicitud> listarSolicitudes() {
		return solicitudRepository.findAll();
	}

	/**
	 * Reabre una solicitud
	 * 
	 * @param id ID de la solicitud a reabrir
	 */
	public void reabrirSolicitud(Long id) {
		Solicitud solicitud = consultarSolicitud(id);
		solicitud.reabrir();
		solicitudRepository.save(solicitud);
	}
}
