package pe.edu.upc.fixcampus.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ReportHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @ManyToOne(optional = false) public Report report;
    @ManyToOne(optional = false) public AppUser actor;
    @Enumerated(EnumType.STRING) public ReportStatus fromStatus;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public ReportStatus toStatus;
    @Column(nullable = false, length = 2000) public String note;
    @Column(nullable = false) public Instant createdAt = Instant.now();
    public ReportHistory() { }
}
