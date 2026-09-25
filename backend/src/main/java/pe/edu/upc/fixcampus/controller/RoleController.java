package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.entities.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @GetMapping
    public List<Role> list() { return Arrays.asList(Role.values()); }
}
