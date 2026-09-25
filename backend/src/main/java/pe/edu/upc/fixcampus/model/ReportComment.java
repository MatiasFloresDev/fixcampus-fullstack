package pe.edu.upc.fixcampus.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="report_comment", indexes=@Index(columnList="report_id"))
public class ReportComment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) public Report report;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) public AppUser author;
    @Column(nullable=false, length=2000) public String body;
    @Column(nullable=false) public Instant createdAt=Instant.now();
    public ReportComment() { }
}
