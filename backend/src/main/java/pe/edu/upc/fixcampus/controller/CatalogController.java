package pe.edu.upc.fixcampus.controller;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.model.*;
import pe.edu.upc.fixcampus.service.*;
import pe.edu.upc.fixcampus.exception.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogController {
    private final CatalogService service;
    public CatalogController(CatalogService service) { this.service=service; }
    @GetMapping("/categories") List<CatalogView> categories() { return service.categories(); }
    @GetMapping("/categories/{id}") CatalogView category(@PathVariable Long id) { return service.category(id); }
    @PostMapping("/categories") @ResponseStatus(HttpStatus.CREATED) CatalogView createCategory(@Valid @RequestBody CatalogInput input) { return service.saveCategory(null,input); }
    @PutMapping("/categories/{id}") CatalogView updateCategory(@PathVariable Long id,@Valid @RequestBody CatalogInput input) { return service.saveCategory(id,input); }
    @DeleteMapping("/categories/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) void deleteCategory(@PathVariable Long id) { service.disableCategory(id); }
    @GetMapping("/areas") List<CatalogView> areas() { return service.areas(); }
    @GetMapping("/areas/{id}") CatalogView area(@PathVariable Long id) { return service.area(id); }
    @PostMapping("/areas") @ResponseStatus(HttpStatus.CREATED) CatalogView createArea(@Valid @RequestBody CatalogInput input) { return service.saveArea(null,input); }
    @PutMapping("/areas/{id}") CatalogView updateArea(@PathVariable Long id,@Valid @RequestBody CatalogInput input) { return service.saveArea(id,input); }
    @DeleteMapping("/areas/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) void deleteArea(@PathVariable Long id) { service.disableArea(id); }
    @GetMapping("/technicians") List<TechnicianView> technicians() { return service.technicians(); }
    @GetMapping("/technicians/{id}") TechnicianView technician(@PathVariable Long id) { return service.technician(id); }
    @PostMapping("/technicians") @ResponseStatus(HttpStatus.CREATED) TechnicianView createTechnician(@Valid @RequestBody TechnicianInput input) { return service.saveTechnician(null,input); }
    @PutMapping("/technicians/{id}") TechnicianView updateTechnician(@PathVariable Long id,@Valid @RequestBody TechnicianInput input) { return service.saveTechnician(id,input); }
    @DeleteMapping("/technicians/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) void deleteTechnician(@PathVariable Long id) { service.disableTechnician(id); }
}
