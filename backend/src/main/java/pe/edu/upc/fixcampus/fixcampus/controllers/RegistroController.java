package pe.edu.upc.fixcampus.fixcampus.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroRequestDTO;
import pe.edu.upc.fixcampus.fixcampus.dtos.RegistroResponseDTO;
import pe.edu.upc.fixcampus.fixcampus.entities.Rol;
import pe.edu.upc.fixcampus.fixcampus.entities.Usuario;
import pe.edu.upc.fixcampus.fixcampus.exceptions.ResourceNotFoundException;
import pe.edu.upc.fixcampus.fixcampus.repositories.RolRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroController(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<RegistroResponseDTO> registrar(
            @Valid @RequestBody RegistroRequestDTO request) {
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Rol rol = rolRepository.findByNombre("USUARIO")
                .orElseThrow(() -> new ResourceNotFoundException("Rol USUARIO no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasenaHash(passwordEncoder.encode(request.getPassword()));
        usuario.setEstado("ACTIVO");
        usuario.setFechaRegistro(LocalDateTime.now());

        Usuario guardado = usuarioRepository.save(usuario);
        RegistroResponseDTO response = new RegistroResponseDTO(
                guardado.getIdUsuario(), guardado.getCorreo(), "Cuenta creada correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
