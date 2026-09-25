package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.dto.Dtos.TechnicianInput;
import pe.edu.upc.fixcampus.dto.Dtos.TechnicianView;
import pe.edu.upc.fixcampus.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {
    private final CatalogService service;

    public TechnicianController(CatalogService service) { this.service = service; }

    @GetMapping
    public List<TechnicianView> list() { return service.technicians(); }

    @GetMapping("/{id}")
    public TechnicianView get(@PathVariable Long id) { return service.technician(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TechnicianView create(@Valid @RequestBody TechnicianInput input) { return service.saveTechnician(null, input); }

    @PutMapping("/{id}")
    public TechnicianView update(@PathVariable Long id, @Valid @RequestBody TechnicianInput input) {
        return service.saveTechnician(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.disableTechnician(id); }
}
