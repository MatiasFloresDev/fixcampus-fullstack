package pe.edu.upc.fixcampus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.event.*;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Map;

@Service
public class EmailService {
    private final NotificationRepository logs;
    private final ReportRepository reports;
    private final ObjectMapper json;
    private final String key;
    private final String from;
    private final HttpClient http=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    public EmailService(NotificationRepository logs,ReportRepository reports,ObjectMapper json,
        @Value("${fixcampus.email.key}") String key,@Value("${fixcampus.email.from}") String from) {
        this.logs=logs; this.reports=reports; this.json=json; this.key=key; this.from=from;
    }
    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void onAssignment(AssignmentCreated event) {
        var report=reports.findById(event.reportId());
        if(report.isEmpty()) return;
        NotificationLog log=new NotificationLog(); log.report=report.get(); log.recipient=event.recipient();
        if(key.isBlank() || from.isBlank()) {
            log.status="SKIPPED"; log.detail="Correo no configurado; no se envió ningún mensaje.";
        } else {
            try {
                String body=json.writeValueAsString(Map.of("from",from,"to",new String[]{event.recipient()},
                    "subject","FixCampus: incidencia #"+event.reportId(),"text","Se te asignó la incidencia #"+event.reportId()+": "+event.title()+". Ingresa a FixCampus para revisarla."));
                var request=HttpRequest.newBuilder(URI.create("https://api.resend.com/emails"))
                    .timeout(Duration.ofSeconds(10)).header("Authorization","Bearer "+key).header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body)).build();
                var response=http.send(request,HttpResponse.BodyHandlers.discarding());
                log.status=response.statusCode()>=200 && response.statusCode()<300 ? "SENT" : "FAILED";
                log.detail=log.status.equals("SENT") ? "Aceptado por Resend; entrega al buzón no verificada." : "Resend rechazó la solicitud (HTTP "+response.statusCode()+").";
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt(); log.status="FAILED"; log.detail="Envío interrumpido.";
            } catch(Exception ex) {
                log.status="FAILED"; log.detail="No fue posible contactar al proveedor de correo.";
            }
        }
        logs.save(log);
    }
}
