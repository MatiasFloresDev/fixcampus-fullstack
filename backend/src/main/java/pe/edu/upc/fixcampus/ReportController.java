package pe.edu.upc.fixcampus;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static pe.edu.upc.fixcampus.Dtos.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService service;
    private final CommentService comments;
    public ReportController(ReportService service, CommentService comments) { this.service=service; this.comments=comments; }
    @GetMapping List<ReportView> list(@RequestParam(required=false) ReportStatus status,
        @RequestParam(required=false) Long categoryId,@RequestParam(required=false) Long areaId,
        @RequestParam(required=false) Priority priority,@RequestParam(required=false,name="q") String query) {
        return service.list(status,categoryId,areaId,priority,query);
    }
    @GetMapping("/{id}") ReportView get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) ReportView create(@Valid @RequestBody ReportInput input) { return service.create(input); }
    @PutMapping("/{id}") ReportView update(@PathVariable Long id,@Valid @RequestBody ReportInput input) { return service.update(id,input); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) void delete(@PathVariable Long id) { service.delete(id); }
    @PostMapping("/{id}/assign") ReportView assign(@PathVariable Long id,@Valid @RequestBody Assignment input) { return service.assign(id,input); }
    @PostMapping("/{id}/transition") ReportView transition(@PathVariable Long id,@Valid @RequestBody Transition input) { return service.transition(id,input); }
    @GetMapping("/{id}/comments") List<CommentView> comments(@PathVariable Long id) { return comments.list(id); }
    @PostMapping("/{id}/comments") @ResponseStatus(HttpStatus.CREATED) CommentView addComment(@PathVariable Long id,@Valid @RequestBody CommentInput input) { return comments.create(id,input); }
}
