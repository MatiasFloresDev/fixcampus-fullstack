package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.dto.Dtos.NotificationView;
import pe.edu.upc.fixcampus.service.CurrentUser;
import pe.edu.upc.fixcampus.entities.NotificationLog;
import pe.edu.upc.fixcampus.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationLogController {
    private final NotificationRepository notifications;
    private final CurrentUser current;

    public NotificationLogController(NotificationRepository notifications, CurrentUser current) {
        this.notifications = notifications;
        this.current = current;
    }

    @GetMapping
    public List<NotificationView> list(@RequestParam(required = false) Long reportId) {
        current.admin();
        var values = reportId == null ? notifications.findAll() : notifications.findByReportIdOrderByIdAsc(reportId);
        return values.stream().map(this::view).toList();
    }

    @GetMapping("/{id}")
    public NotificationView get(@PathVariable Long id) {
        current.admin();
        return view(notifications.findById(id).orElseThrow(pe.edu.upc.fixcampus.exception.ApiException::missing));
    }

    private NotificationView view(NotificationLog value) {
        return new NotificationView(value.id, value.status, value.recipient, value.detail, value.createdAt);
    }
}
