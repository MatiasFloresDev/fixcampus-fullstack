package pe.edu.upc.fixcampus.repository;

import pe.edu.upc.fixcampus.entities.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<ReportHistory, Long> {
    List<ReportHistory> findByReportIdOrderByIdAsc(Long reportId);
    void deleteByReportId(Long reportId);
}
