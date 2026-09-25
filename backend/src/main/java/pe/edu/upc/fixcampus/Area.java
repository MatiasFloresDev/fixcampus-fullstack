package pe.edu.upc.fixcampus;

import jakarta.persistence.*;

@Entity
public class Area {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(nullable = false, unique = true, length = 100) String name;
    @Column(length = 500) String description;
    @Column(nullable = false) boolean active = true;
    public Area() { }
}
