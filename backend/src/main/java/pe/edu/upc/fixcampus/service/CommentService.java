package pe.edu.upc.fixcampus.service;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.event.*;
import pe.edu.upc.fixcampus.exception.*;
import pe.edu.upc.fixcampus.model.*;
import pe.edu.upc.fixcampus.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository comments;
    private final ReportRepository reports;
    private final CurrentUser current;
    public CommentService(CommentRepository comments, ReportRepository reports, CurrentUser current) {
        this.comments=comments; this.reports=reports; this.current=current;
    }
    @Transactional(readOnly=true)
    public List<CommentView> list(Long reportId) {
        Report report=find(reportId); requireRead(report);
        return comments.findByReportIdOrderByCreatedAtAsc(reportId).stream()
            .map(c -> new CommentView(c.id,c.body,c.author.id,c.author.name,c.createdAt)).toList();
    }
    public CommentView create(Long reportId, CommentInput input) {
        Report report=find(reportId); requireRead(report);
        ReportComment c=new ReportComment(); c.report=report; c.author=current.get();
        c.body=input.body().trim(); c.createdAt=Instant.now(); comments.saveAndFlush(c);
        return new CommentView(c.id,c.body,c.author.id,c.author.name,c.createdAt);
    }
    private Report find(Long id) { return reports.findById(id).orElseThrow(ApiException::missing); }
    private void requireRead(Report report) {
        AppUser actor=current.get();
        boolean own=actor.role==Role.REPORTER && report.reporter.id.equals(actor.id);
        boolean assigned=actor.role==Role.TECHNICIAN && report.technician!=null && report.technician.user.id.equals(actor.id);
        if(actor.role!=Role.ADMIN && !own && !assigned) throw ApiException.forbidden();
    }
}
