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
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service=service; }
    @GetMapping List<UserView> list() { return service.list(); }
    @GetMapping("/{id}") UserView get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) UserView create(@Valid @RequestBody UserInput input) { return service.save(null,input); }
    @PutMapping("/{id}") UserView update(@PathVariable Long id,@Valid @RequestBody UserInput input) { return service.save(id,input); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) void delete(@PathVariable Long id) { service.disable(id); }
}
