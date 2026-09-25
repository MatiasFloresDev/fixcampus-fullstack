package pe.edu.upc.fixcampus.controller;

import pe.edu.upc.fixcampus.entities.ReportStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/report-statuses")
public class ReportStatusController {
    @GetMapping
    public List<ReportStatus> list() { return Arrays.asList(ReportStatus.values()); }
}
