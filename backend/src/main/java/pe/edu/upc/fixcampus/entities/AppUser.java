package pe.edu.upc.fixcampus.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, length = 120) public String name;
    @Column(nullable = false, unique = true, length = 180) public String email;
    @Column(nullable = false) public String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public Role role;
    @Column(nullable = false) public boolean active = true;
    public AppUser() { }
}
