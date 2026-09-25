package pe.edu.upc.fixcampus.fixcampus.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.fixcampus.fixcampus.dtos.LoginRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.LoginResponseDTO;
import pe.edu.upc.fixcampus.fixcampus.securities.JwtTokenService;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UsuarioRepository usuarioRepository;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtTokenService jwtTokenService,
                           UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(), request.getPassword()));

        UserDetails usuario = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenService.generarToken(usuario);

        Long idUsuario = usuarioRepository.findByCorreo(usuario.getUsername())
                .orElseThrow().getIdUsuario();
        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getUsername(), idUsuario));
    }
}
