package pe.edu.upc.fixcampus.service;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.event.*;
import pe.edu.upc.fixcampus.exception.*;
import pe.edu.upc.fixcampus.entities.*;
import pe.edu.upc.fixcampus.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final ReportService reports;
    public DashboardService(ReportService reports) { this.reports=reports; }
    @Transactional(readOnly=true)
    public Dashboard get() {
        List<Report> visible=reports.visibleReports();
        long open=visible.stream().filter(r -> r.status==ReportStatus.NEW || r.status==ReportStatus.ASSIGNED || r.status==ReportStatus.IN_PROGRESS).count();
        long resolved=visible.stream().filter(r -> r.status==ReportStatus.RESOLVED).count();
        long closed=visible.stream().filter(r -> r.status==ReportStatus.CLOSED).count();
        var average=visible.stream().filter(r -> r.resolvedAt!=null)
            .mapToDouble(r -> Duration.between(r.createdAt,r.resolvedAt).toMillis()/3_600_000.0).average();
        var month=DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneOffset.UTC);
        return new Dashboard(visible.size(),open,resolved,closed,average.isPresent()?average.getAsDouble():null,
            Arrays.stream(ReportStatus.values()).map(status -> new Count(status.name(),visible.stream().filter(r -> r.status==status).count())).toList(),
            group(visible,r -> r.category.name),group(visible,r -> r.area.name),group(visible,r -> month.format(r.createdAt)));
    }
    private List<Count> group(List<Report> reports,Function<Report,String> classifier) {
        return reports.stream().collect(Collectors.groupingBy(classifier,TreeMap::new,Collectors.counting()))
            .entrySet().stream().map(entry -> new Count(entry.getKey(),entry.getValue())).toList();
    }
}
