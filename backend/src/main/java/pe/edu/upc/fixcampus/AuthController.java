package pe.edu.upc.fixcampus;

import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static pe.edu.upc.fixcampus.Dtos.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager manager;
    private final HttpSessionSecurityContextRepository contexts;
    private final HttpSessionCsrfTokenRepository csrf;
    private final CurrentUser current;
    public AuthController(AuthenticationManager manager, HttpSessionSecurityContextRepository contexts,
                          HttpSessionCsrfTokenRepository csrf, CurrentUser current) {
        this.manager=manager; this.contexts=contexts; this.csrf=csrf; this.current=current;
    }
    @GetMapping("/csrf") Map<String,String> csrf(CsrfToken token) {
        return Map.of("token",token.getToken(),"headerName",token.getHeaderName());
    }
    @PostMapping("/login") UserView login(@Valid @RequestBody Login input, HttpServletRequest request, HttpServletResponse response) {
        try {
            var auth = manager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(input.email(),input.password()));
            if (request.getSession(false) != null) request.changeSessionId();
            var context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            contexts.saveContext(context,request,response);
            csrf.saveToken(null,request,response);
            return UserService.view(current.get());
        } catch (AuthenticationException ex) { throw new ApiException(401,"credentials"); }
    }
    @GetMapping("/me") UserView me() { return UserService.view(current.get()); }
    @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT)
    void logout(HttpServletRequest request) {
        if (request.getSession(false) != null) request.getSession(false).invalidate();
        SecurityContextHolder.clearContext();
    }
}
