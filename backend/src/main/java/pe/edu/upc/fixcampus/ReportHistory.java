package pe.edu.upc.fixcampus;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ReportHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @ManyToOne(optional = false) Report report;
    @ManyToOne(optional = false) AppUser actor;
    @Enumerated(EnumType.STRING) ReportStatus fromStatus;
    @Enumerated(EnumType.STRING) @Column(nullable = false) ReportStatus toStatus;
    @Column(nullable = false, length = 2000) String note;
    @Column(nullable = false) Instant createdAt = Instant.now();
    public ReportHistory() { }
}
