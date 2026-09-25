package pe.edu.upc.fixcampus.dto;
import pe.edu.upc.fixcampus.model.*;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.List;

public final class Dtos {
    private Dtos() { }
    public record Login(@NotBlank @Email String email, @NotBlank String password) { }
    public record UserInput(@NotBlank @Size(max=120) String name, @NotBlank @Email @Size(max=180) String email,
                            @Size(max=72) String password, @NotNull Role role, boolean active) { }
    public record UserView(Long id, String name, String email, Role role, boolean active) { }
    public record CatalogInput(@NotBlank @Size(max=100) String name, @Size(max=500) String description, boolean active) { }
    public record CatalogView(Long id, String name, String description, boolean active) { }
    public record TechnicianInput(@NotNull Long userId, @NotBlank @Size(max=150) String specialty, boolean active) { }
    public record TechnicianView(Long id, Long userId, String name, String email, String specialty, boolean active) { }
    public record ReportInput(@NotBlank @Size(max=160) String title, @NotBlank @Size(max=5000) String description,
                              @NotBlank @Size(max=200) String location, @NotNull Long categoryId, @NotNull Long areaId,
                              @NotNull Priority priority) { }
    public record Assignment(@NotNull Long technicianId, @Size(max=1800) String note) { }
    public record Transition(@NotNull ReportStatus status, @Size(max=2000) String note) { }
    public record HistoryView(Long id, ReportStatus fromStatus, ReportStatus toStatus, String note, String actorName, Instant createdAt) { }
    public record NotificationView(Long id, String status, String recipient, String detail, Instant createdAt) { }
    public record CommentInput(@NotBlank @Size(max=2000) String body) { }
    public record CommentView(Long id, String body, Long authorId, String authorName, Instant createdAt) { }
    public record ReportView(Long id, String title, String description, String location, Long categoryId,
                             String categoryName, Long areaId, String areaName, Priority priority, ReportStatus status,
                             Long reporterId, String reporterName, Long technicianId, String technicianName,
                             Instant createdAt, Instant updatedAt, Instant resolvedAt, Instant closedAt,
                             List<HistoryView> history, List<NotificationView> notifications) { }
    public record Count(String label, long count) { }
    public record Dashboard(long total, long open, long resolved, long closed, Double averageResolutionHours,
                            List<Count> byStatus, List<Count> byCategory, List<Count> byArea, List<Count> byMonth) { }
}
