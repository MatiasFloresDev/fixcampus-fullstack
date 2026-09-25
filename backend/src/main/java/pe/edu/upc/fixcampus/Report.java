package pe.edu.upc.fixcampus;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(indexes = {@Index(columnList = "status"), @Index(columnList = "reporter_id")})
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Version Long version;
    @Column(nullable = false, length = 160) String title;
    @Column(nullable = false, length = 5000) String description;
    @Column(nullable = false, length = 200) String location;
    @ManyToOne(optional = false) Category category;
    @ManyToOne(optional = false) Area area;
    @ManyToOne(optional = false) AppUser reporter;
    @ManyToOne Technician technician;
    @Enumerated(EnumType.STRING) @Column(nullable = false) Priority priority;
    @Enumerated(EnumType.STRING) @Column(nullable = false) ReportStatus status = ReportStatus.NEW;
    @Column(nullable = false) Instant createdAt = Instant.now();
    @Column(nullable = false) Instant updatedAt = createdAt;
    Instant resolvedAt;
    Instant closedAt;
    public Report() { }
}
