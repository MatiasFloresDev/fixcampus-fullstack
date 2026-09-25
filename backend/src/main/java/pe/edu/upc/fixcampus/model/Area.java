package pe.edu.upc.fixcampus.model;

import jakarta.persistence.*;

@Entity
public class Area {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false, unique = true, length = 100) public String name;
    @Column(length = 500) public String description;
    @Column(nullable = false) public boolean active = true;
    public Area() { }
}
