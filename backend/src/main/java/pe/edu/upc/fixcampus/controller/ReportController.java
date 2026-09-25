package pe.edu.upc.fixcampus.controller;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.entities.*;
import pe.edu.upc.fixcampus.service.*;
import pe.edu.upc.fixcampus.exception.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService service) { this.service=service; }
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
}
