package pe.edu.upc.fixcampus;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 120) String name;
    @Column(nullable = false, unique = true, length = 180) String email;
    @Column(nullable = false) String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) Role role;
    @Column(nullable = false) boolean active = true;
    public AppUser() { }
}
