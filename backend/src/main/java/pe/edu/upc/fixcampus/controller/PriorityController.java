package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.entities.Priority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/priorities")
public class PriorityController {
    @GetMapping
    public List<Priority> list() { return Arrays.asList(Priority.values()); }
}
