package pe.edu.upc.fixcampus.repository;

import pe.edu.upc.fixcampus.entities.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<ReportComment, Long> {
    List<ReportComment> findByReportIdOrderByCreatedAtAsc(Long reportId);
    void deleteByReportId(Long reportId);
}
