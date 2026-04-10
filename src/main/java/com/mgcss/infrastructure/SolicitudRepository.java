public interface SolicitudRepository {
    Solicitud save(Solicitud solicitud);
    Optional findById(Long id);
}
