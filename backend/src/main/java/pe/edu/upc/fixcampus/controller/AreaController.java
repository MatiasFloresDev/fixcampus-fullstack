package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.dto.Dtos.CatalogInput;
import pe.edu.upc.fixcampus.dto.Dtos.CatalogView;
import pe.edu.upc.fixcampus.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {
    private final CatalogService service;

    public AreaController(CatalogService service) { this.service = service; }

    @GetMapping
    public List<CatalogView> list() { return service.areas(); }

    @GetMapping("/{id}")
    public CatalogView get(@PathVariable Long id) { return service.area(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogView create(@Valid @RequestBody CatalogInput input) { return service.saveArea(null, input); }

    @PutMapping("/{id}")
    public CatalogView update(@PathVariable Long id, @Valid @RequestBody CatalogInput input) {
        return service.saveArea(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.disableArea(id); }
}
