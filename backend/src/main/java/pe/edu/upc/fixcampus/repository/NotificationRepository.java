package pe.edu.upc.fixcampus.repository;

import pe.edu.upc.fixcampus.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByReportIdOrderByIdAsc(Long reportId);
    void deleteByReportId(Long reportId);
}
