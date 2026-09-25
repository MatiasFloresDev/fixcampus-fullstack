package pe.edu.upc.fixcampus.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class NotificationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @ManyToOne(optional = false) public Report report;
    @Column(nullable = false) public String recipient;
    @Column(nullable = false) public String status;
    @Column(nullable = false, length = 500) public String detail;
    @Column(nullable = false) public Instant createdAt = Instant.now();
    public NotificationLog() { }
}
