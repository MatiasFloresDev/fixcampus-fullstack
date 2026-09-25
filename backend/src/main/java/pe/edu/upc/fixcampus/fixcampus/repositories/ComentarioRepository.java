package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Comentario;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    // Consulta 6: lista los comentarios de un reporte concreto mediante su relación.
    List<Comentario> findByReporte_IdReporte(Long idReporte);

    // Consulta 12, con JOIN: cuenta los comentarios de un usuario en cada reporte.
    @Query("select new pe.edu.upc.fixcampus.fixcampus.dtos.ComentariosPorReporteDTO(" +
            "r.idReporte, r.titulo, count(c)) " +
            "from Comentario c join c.reporte r join c.usuario u " +
            "where lower(u.correo) = lower(:correo) " +
            "group by r.idReporte, r.titulo order by count(c) desc")
    List<ComentariosPorReporteDTO> contarPorReporteYCorreo(@Param("correo") String correo);
}
