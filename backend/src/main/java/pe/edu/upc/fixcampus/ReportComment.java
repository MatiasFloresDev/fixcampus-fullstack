package pe.edu.upc.fixcampus;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="report_comment", indexes=@Index(columnList="report_id"))
public class ReportComment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) Report report;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) AppUser author;
    @Column(nullable=false, length=2000) String body;
    @Column(nullable=false) Instant createdAt=Instant.now();
    public ReportComment() { }
}
