package pe.edu.upc.fixcampus.fixcampus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.fixcampus.fixcampus.entities.Recomendacion;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    boolean existsByReporte_IdReporte(Long idReporte);
}
