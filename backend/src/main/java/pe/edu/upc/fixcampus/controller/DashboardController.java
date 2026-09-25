package pe.edu.upc.fixcampus.controller;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.entities.*;
import pe.edu.upc.fixcampus.service.*;
import pe.edu.upc.fixcampus.exception.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;
    public DashboardController(DashboardService service) { this.service=service; }
    @GetMapping Dtos.Dashboard get() { return service.get(); }
}
