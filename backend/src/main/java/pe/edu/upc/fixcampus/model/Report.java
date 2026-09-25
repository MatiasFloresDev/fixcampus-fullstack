package pe.edu.upc.fixcampus.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(indexes = {@Index(columnList = "status"), @Index(columnList = "reporter_id")})
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Version public Long version;
    @Column(nullable = false, length = 160) public String title;
    @Column(nullable = false, length = 5000) public String description;
    @Column(nullable = false, length = 200) public String location;
    @ManyToOne(optional = false) public Category category;
    @ManyToOne(optional = false) public Area area;
    @ManyToOne(optional = false) public AppUser reporter;
    @ManyToOne public Technician technician;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public Priority priority;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public ReportStatus status = ReportStatus.NEW;
    @Column(nullable = false) public Instant createdAt = Instant.now();
    @Column(nullable = false) public Instant updatedAt = createdAt;
    public Instant resolvedAt;
    public Instant closedAt;
    public Report() { }
}
