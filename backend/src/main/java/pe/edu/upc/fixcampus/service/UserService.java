package pe.edu.upc.fixcampus.service;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.event.*;
import pe.edu.upc.fixcampus.exception.*;
import pe.edu.upc.fixcampus.entities.*;
import pe.edu.upc.fixcampus.repository.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class UserService {
    private final UserRepository users;
    private final TechnicianRepository technicians;
    private final PasswordEncoder encoder;
    private final CurrentUser current;
    public UserService(UserRepository users, TechnicianRepository technicians, PasswordEncoder encoder, CurrentUser current) {
        this.users=users; this.technicians=technicians; this.encoder=encoder; this.current=current;
    }
    public List<UserView> list() { current.admin(); return users.findAll().stream().map(UserService::view).toList(); }
    public UserView get(Long id) { current.admin(); return view(find(id)); }
    public UserView save(Long id, UserInput input) {
        AppUser actor = current.admin();
        AppUser user = id == null ? new AppUser() : find(id);
        if (actor.id.equals(id) && (!input.active() || input.role()!=Role.ADMIN)) throw new ApiException(409,"selfAdmin");
        if (id != null && input.role()!=Role.TECHNICIAN && technicians.findByUserId(id).isPresent()) throw new ApiException(409,"technicianLinked");
        String email = input.email().trim().toLowerCase(Locale.ROOT);
        users.findByEmailIgnoreCase(email).filter(existing -> !existing.id.equals(id)).ifPresent(existing -> { throw new ApiException(409,"duplicate"); });
        if (id == null || (input.password()!=null && !input.password().isBlank())) {
            validatePassword(input.password());
            user.passwordHash=encoder.encode(input.password());
        }
        user.name=input.name().trim(); user.email=email; user.role=input.role(); user.active=input.active();
        return view(users.save(user));
    }
    public void disable(Long id) {
        if (current.admin().id.equals(id)) throw new ApiException(409,"selfAdmin");
        find(id).active=false;
    }
    private AppUser find(Long id) { return users.findById(id).orElseThrow(ApiException::missing); }
    static void validatePassword(String value) {
        if(value==null || value.length()<12 || value.getBytes(StandardCharsets.UTF_8).length>72) throw new ApiException(400,"password");
    }
    public static UserView view(AppUser user) { return new UserView(user.id,user.name,user.email,user.role,user.active); }
}
