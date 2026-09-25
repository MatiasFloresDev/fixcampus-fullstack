package pe.edu.upc.fixcampus.repository;

import pe.edu.upc.fixcampus.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByOrderByCreatedAtDesc();
    List<Report> findByReporterIdOrderByCreatedAtDesc(Long reporterId);
    List<Report> findByTechnicianUserIdOrderByCreatedAtDesc(Long userId);
}
