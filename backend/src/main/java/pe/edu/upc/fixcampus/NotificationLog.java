package pe.edu.upc.fixcampus;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class NotificationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @ManyToOne(optional = false) Report report;
    @Column(nullable = false) String recipient;
    @Column(nullable = false) String status;
    @Column(nullable = false, length = 500) String detail;
    @Column(nullable = false) Instant createdAt = Instant.now();
    public NotificationLog() { }
}
