package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.dto.Dtos.CommentInput;
import pe.edu.upc.fixcampus.dto.Dtos.CommentView;
import pe.edu.upc.fixcampus.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports/{reportId}/comments")
public class ReportCommentController {
    private final CommentService service;

    public ReportCommentController(CommentService service) { this.service = service; }

    @GetMapping
    public List<CommentView> list(@PathVariable Long reportId) { return service.list(reportId); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentView create(@PathVariable Long reportId, @Valid @RequestBody CommentInput input) {
        return service.create(reportId, input);
    }

    @PutMapping("/{commentId}")
    public CommentView update(@PathVariable Long reportId, @PathVariable Long commentId,
                              @Valid @RequestBody CommentInput input) {
        return service.update(reportId, commentId, input);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long reportId, @PathVariable Long commentId) {
        service.delete(reportId, commentId);
    }
}
