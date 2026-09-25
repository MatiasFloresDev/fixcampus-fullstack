package pe.edu.upc.fixcampus;

import jakarta.persistence.*;

@Entity
public class Technician {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @OneToOne(optional = false) @JoinColumn(nullable = false, unique = true) AppUser user;
    @Column(nullable = false, length = 150) String specialty;
    @Column(nullable = false) boolean active = true;
    public Technician() { }
}
