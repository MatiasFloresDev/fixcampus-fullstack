package pe.edu.upc.fixcampus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);
}
interface CategoryRepository extends JpaRepository<Category, Long> { }
interface AreaRepository extends JpaRepository<Area, Long> { }
interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Optional<Technician> findByUserId(Long userId);
}
interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByOrderByCreatedAtDesc();
    List<Report> findByReporterIdOrderByCreatedAtDesc(Long reporterId);
    List<Report> findByTechnicianUserIdOrderByCreatedAtDesc(Long userId);
}
interface HistoryRepository extends JpaRepository<ReportHistory, Long> {
    List<ReportHistory> findByReportIdOrderByIdAsc(Long reportId);
    void deleteByReportId(Long reportId);
}
interface NotificationRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByReportIdOrderByIdAsc(Long reportId);
    void deleteByReportId(Long reportId);
}
interface CommentRepository extends JpaRepository<ReportComment, Long> {
    List<ReportComment> findByReportIdOrderByCreatedAtAsc(Long reportId);
    void deleteByReportId(Long reportId);
}
