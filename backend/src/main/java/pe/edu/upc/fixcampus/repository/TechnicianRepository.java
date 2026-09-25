package pe.edu.upc.fixcampus.repository;

import pe.edu.upc.fixcampus.entities.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Optional<Technician> findByUserId(Long userId);
}
