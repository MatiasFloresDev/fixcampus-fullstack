package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.dto.Dtos.HistoryView;
import pe.edu.upc.fixcampus.entities.ReportHistory;
import pe.edu.upc.fixcampus.repository.HistoryRepository;
import pe.edu.upc.fixcampus.service.CurrentUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report-history")
public class ReportHistoryController {
    private final HistoryRepository history;
    private final CurrentUser current;

    public ReportHistoryController(HistoryRepository history, CurrentUser current) {
        this.history = history;
        this.current = current;
    }

    @GetMapping
    public List<HistoryView> list(@RequestParam(required = false) Long reportId) {
        current.admin();
        var values = reportId == null ? history.findAll() : history.findByReportIdOrderByIdAsc(reportId);
        return values.stream().map(this::view).toList();
    }

    @GetMapping("/{id}")
    public HistoryView get(@PathVariable Long id) {
        current.admin();
        return view(history.findById(id).orElseThrow(pe.edu.upc.fixcampus.exception.ApiException::missing));
    }

    private HistoryView view(ReportHistory value) {
        return new HistoryView(value.id, value.fromStatus, value.toStatus, value.note, value.actor.name, value.createdAt);
    }
}
