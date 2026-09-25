package pe.edu.upc.fixcampus.entities;

import jakarta.persistence.*;

@Entity
public class Technician {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @OneToOne(optional = false) @JoinColumn(nullable = false, unique = true) public AppUser user;
    @Column(nullable = false, length = 150) public String specialty;
    @Column(nullable = false) public boolean active = true;
    public Technician() { }
}
