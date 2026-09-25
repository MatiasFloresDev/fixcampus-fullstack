package pe.edu.upc.fixcampus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name="fixcampus.seed.enabled",havingValue="true")
public class DemoData implements CommandLineRunner {
    private final UserRepository users;
    private final CategoryRepository categories;
    private final AreaRepository areas;
    private final TechnicianRepository technicians;
    private final PasswordEncoder encoder;
    private final String password;
    public DemoData(UserRepository users,CategoryRepository categories,AreaRepository areas,TechnicianRepository technicians,
                    PasswordEncoder encoder,@Value("${fixcampus.seed.password}") String password) {
        this.users=users; this.categories=categories; this.areas=areas; this.technicians=technicians; this.encoder=encoder; this.password=password;
    }
    @Override @Transactional public void run(String... args) {
        UserService.validatePassword(password);
        user("Administrador","admin@fixcampus.local",Role.ADMIN);
        user("María Torres","reporter@fixcampus.local",Role.REPORTER);
        user("José Ramos","other@fixcampus.local",Role.REPORTER);
        AppUser technician=user("Carlos Ruiz","technician@fixcampus.local",Role.TECHNICIAN);
        if(technicians.findByUserId(technician.id).isEmpty()) {
            Technician profile=new Technician(); profile.user=technician; profile.specialty="Soporte e infraestructura"; technicians.save(profile);
        }
        if(categories.count()==0) {
            category("Tecnología","Computadoras, proyectores y conectividad");
            category("Electricidad","Luminarias, tomacorrientes y suministro eléctrico");
            category("Infraestructura","Mobiliario, puertas y espacios comunes");
            category("Limpieza","Limpieza y mantenimiento sanitario");
        }
        if(areas.count()==0) {
            area("Pabellón A","Aulas y laboratorios");
            area("Biblioteca","Salas de estudio");
            area("Áreas comunes","Patios y servicios");
        }
    }
    private AppUser user(String name,String email,Role role) {
        return users.findByEmailIgnoreCase(email).orElseGet(() -> {
            AppUser value=new AppUser(); value.name=name; value.email=email; value.role=role; value.passwordHash=encoder.encode(password);
            return users.save(value);
        });
    }
    private void category(String name,String description) { Category value=new Category(); value.name=name; value.description=description; categories.save(value); }
    private void area(String name,String description) { Area value=new Area(); value.name=name; value.description=description; areas.save(value); }
}
