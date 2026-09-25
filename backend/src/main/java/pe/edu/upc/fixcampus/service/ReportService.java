package pe.edu.upc.fixcampus.service;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.event.*;
import pe.edu.upc.fixcampus.exception.*;
import pe.edu.upc.fixcampus.model.*;
import pe.edu.upc.fixcampus.repository.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ReportService {
    private final ReportRepository reports;
    private final HistoryRepository history;
    private final NotificationRepository notifications;
    private final CommentRepository comments;
    private final CatalogService catalogs;
    private final CurrentUser current;
    private final ApplicationEventPublisher events;
    public ReportService(ReportRepository reports,HistoryRepository history,NotificationRepository notifications,CommentRepository comments,
                         CatalogService catalogs,CurrentUser current,ApplicationEventPublisher events) {
        this.reports=reports; this.history=history; this.notifications=notifications; this.comments=comments;
        this.catalogs=catalogs; this.current=current; this.events=events;
    }
    public List<ReportView> list(ReportStatus status,Long categoryId,Long areaId,Priority priority,String query) {
        String needle=query==null ? "" : query.toLowerCase(Locale.ROOT).trim();
        return visibleReports().stream()
            .filter(report -> status==null || report.status==status)
            .filter(report -> categoryId==null || report.category.id.equals(categoryId))
            .filter(report -> areaId==null || report.area.id.equals(areaId))
            .filter(report -> priority==null || report.priority==priority)
            .filter(report -> needle.isEmpty() || (report.title+" "+report.description+" "+report.location).toLowerCase(Locale.ROOT).contains(needle))
            .map(this::view).toList();
    }
    List<Report> visibleReports() {
        AppUser actor=current.get();
        return switch(actor.role) {
            case ADMIN -> reports.findAllByOrderByCreatedAtDesc();
            case REPORTER -> reports.findByReporterIdOrderByCreatedAtDesc(actor.id);
            case TECHNICIAN -> reports.findByTechnicianUserIdOrderByCreatedAtDesc(actor.id);
        };
    }
    public ReportView get(Long id) {
        Report report=find(id); requireRead(current.get(),report); return view(report);
    }
    public ReportView create(ReportInput input) {
        AppUser actor=current.get();
        if(actor.role==Role.TECHNICIAN) throw ApiException.forbidden();
        Report report=new Report(); report.reporter=actor;
        applyInput(report,input); reports.saveAndFlush(report);
        record(report,actor,null,"Reporte creado");
        return view(report);
    }
    public ReportView update(Long id,ReportInput input) {
        AppUser actor=current.get(); Report report=find(id); requireEditable(actor,report);
        applyInput(report,input); report.updatedAt=Instant.now();
        record(report,actor,report.status,"Datos del reporte actualizados");
        return view(report);
    }
    public void delete(Long id) {
        AppUser actor=current.get(); Report report=find(id); requireEditable(actor,report);
        notifications.deleteByReportId(id); history.deleteByReportId(id); comments.deleteByReportId(id); reports.delete(report);
    }
    public ReportView assign(Long id,Assignment input) {
        AppUser actor=current.admin(); Report report=find(id);
        if(report.status!=ReportStatus.NEW && report.status!=ReportStatus.ASSIGNED && report.status!=ReportStatus.IN_PROGRESS)
            throw new ApiException(409,"transition");
        Technician technician=catalogs.findTechnician(input.technicianId());
        if(!technician.active || !technician.user.active || technician.user.role!=Role.TECHNICIAN) throw new ApiException(400,"inactive");
        ReportStatus previous=report.status;
        report.technician=technician;
        if(report.status==ReportStatus.NEW) report.status=ReportStatus.ASSIGNED;
        report.updatedAt=Instant.now();
        record(report,actor,previous,"Asignado a "+technician.user.name+". "+text(input.note()));
        events.publishEvent(new AssignmentCreated(report.id,technician.user.email,report.title));
        return view(report);
    }
    public ReportView transition(Long id,Transition input) {
        AppUser actor=current.get(); Report report=find(id); requireRead(actor,report);
        ReportStatus previous=report.status;
        boolean closing=previous==ReportStatus.RESOLVED && input.status()==ReportStatus.CLOSED;
        boolean working=(previous==ReportStatus.ASSIGNED && input.status()==ReportStatus.IN_PROGRESS)
            || (previous==ReportStatus.IN_PROGRESS && input.status()==ReportStatus.RESOLVED);
        if(!closing && !working) throw new ApiException(409,"transition");
        boolean owner=report.reporter.id.equals(actor.id);
        boolean assigned=report.technician!=null && report.technician.user.id.equals(actor.id);
        if(actor.role!=Role.ADMIN && !(closing && actor.role==Role.REPORTER && owner)
            && !(working && actor.role==Role.TECHNICIAN && assigned)) throw ApiException.forbidden();
        String note=text(input.note());
        if(input.status()==ReportStatus.RESOLVED && note.isBlank()) throw new ApiException(400,"resolutionNote");
        report.status=input.status(); report.updatedAt=Instant.now();
        if(report.status==ReportStatus.RESOLVED) report.resolvedAt=report.updatedAt;
        if(report.status==ReportStatus.CLOSED) report.closedAt=report.updatedAt;
        record(report,actor,previous,note);
        return view(report);
    }
    private void applyInput(Report report,ReportInput input) {
        Category category=catalogs.findCategory(input.categoryId()); Area area=catalogs.findArea(input.areaId());
        if(!category.active || !area.active) throw new ApiException(400,"inactive");
        report.title=input.title().trim(); report.description=input.description().trim(); report.location=input.location().trim();
        report.category=category; report.area=area; report.priority=input.priority();
    }
    private void requireEditable(AppUser actor,Report report) {
        if(actor.role!=Role.ADMIN && !(actor.role==Role.REPORTER && report.reporter.id.equals(actor.id))) throw ApiException.forbidden();
        if(report.status!=ReportStatus.NEW) throw new ApiException(409,"onlyNew");
    }
    private void requireRead(AppUser actor,Report report) {
        boolean own=actor.role==Role.REPORTER && report.reporter.id.equals(actor.id);
        boolean assigned=actor.role==Role.TECHNICIAN && report.technician!=null && report.technician.user.id.equals(actor.id);
        if(actor.role!=Role.ADMIN && !own && !assigned) throw ApiException.forbidden();
    }
    private Report find(Long id) { return reports.findById(id).orElseThrow(ApiException::missing); }
    private void record(Report report,AppUser actor,ReportStatus from,String note) {
        ReportHistory entry=new ReportHistory(); entry.report=report; entry.actor=actor;
        entry.fromStatus=from; entry.toStatus=report.status; entry.note=note; history.saveAndFlush(entry);
    }
    private String text(String value) { return value==null ? "" : value.trim(); }
    private ReportView view(Report report) {
        var timeline=history.findByReportIdOrderByIdAsc(report.id).stream()
            .map(item -> new HistoryView(item.id,item.fromStatus,item.toStatus,item.note,item.actor.name,item.createdAt)).toList();
        var mail=notifications.findByReportIdOrderByIdAsc(report.id).stream()
            .map(item -> new NotificationView(item.id,item.status,item.recipient,item.detail,item.createdAt)).toList();
        return new ReportView(report.id,report.title,report.description,report.location,report.category.id,report.category.name,
            report.area.id,report.area.name,report.priority,report.status,report.reporter.id,report.reporter.name,
            report.technician==null ? null : report.technician.id,report.technician==null ? null : report.technician.user.name,
            report.createdAt,report.updatedAt,report.resolvedAt,report.closedAt,timeline,mail);
    }
}
