package pe.edu.upc.fixcampus;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import static pe.edu.upc.fixcampus.Dtos.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService service;
    public AiController(AiService service) { this.service=service; }
    @PostMapping("/suggest") Suggestion suggest(@Valid @RequestBody SuggestionInput input) { return service.suggest(input.description()); }
}
