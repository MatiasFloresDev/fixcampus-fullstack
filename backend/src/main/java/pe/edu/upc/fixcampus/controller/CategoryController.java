package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.dto.Dtos.CatalogInput;
import pe.edu.upc.fixcampus.dto.Dtos.CatalogView;
import pe.edu.upc.fixcampus.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CatalogService service;

    public CategoryController(CatalogService service) { this.service = service; }

    @GetMapping
    public List<CatalogView> list() { return service.categories(); }

    @GetMapping("/{id}")
    public CatalogView get(@PathVariable Long id) { return service.category(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogView create(@Valid @RequestBody CatalogInput input) { return service.saveCategory(null, input); }

    @PutMapping("/{id}")
    public CatalogView update(@PathVariable Long id, @Valid @RequestBody CatalogInput input) {
        return service.saveCategory(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.disableCategory(id); }
}
