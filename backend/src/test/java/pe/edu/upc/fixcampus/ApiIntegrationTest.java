package pe.edu.upc.fixcampus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:h2:mem:tests;DB_CLOSE_DELAY=-1", "spring.jpa.hibernate.ddl-auto=create-drop",
    "fixcampus.seed.enabled=true", "fixcampus.seed.password=OnlyTests-9284!"})
class ApiIntegrationTest {
    @LocalServerPort int port;
    @Autowired ObjectMapper json;

    class Client {
        HttpClient http = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
        String csrf;
        HttpResponse<String> call(String method, String path, Object body) throws Exception {
            var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api" + path));
            if (csrf != null) request.header("X-CSRF-TOKEN", csrf);
            request.header("Content-Type", "application/json");
            request.method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body)));
            return http.send(request.build(), HttpResponse.BodyHandlers.ofString());
        }
        JsonNode ok(String method, String path, Object body) throws Exception {
            var response = call(method, path, body);
            assertThat(response.statusCode()).as(response.body()).isBetween(200,299);
            return response.body().isBlank() ? json.nullNode() : json.readTree(response.body());
        }
        void login(String email) throws Exception {
            csrf = ok("GET", "/auth/csrf", null).get("token").asText();
            ok("POST", "/auth/login", Map.of("email", email,"password","OnlyTests-9284!"));
            csrf = ok("GET", "/auth/csrf", null).get("token").asText();
        }
    }

    @Test void anonymousCannotReadReportsAndCsrfProtectsLogin() throws Exception {
        var client = new Client();
        assertThat(client.call("GET", "/reports", null).statusCode()).isEqualTo(401);
        assertThat(client.call("POST", "/auth/login", Map.of("email","admin@fixcampus.local","password","OnlyTests-9284!")).statusCode()).isEqualTo(403);
    }

    @Test void swaggerDocumentsCrudAndReportFilters() throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/v3/api-docs"))
            .GET().build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("/api/users", "/api/categories", "/api/areas", "/api/technicians", "/api/reports",
            "/api/reports/{reportId}/comments", "/api/notifications", "/api/report-history", "/api/priorities",
            "/api/report-statuses", "/api/roles");
        assertThat(response.body()).contains("categoryId", "areaId", "priority", "status", "q");
        assertThat(response.body()).contains("\"put\"", "\"delete\"");
    }

    @Test void ownershipTransitionsHistoryDashboardAndFallbackWorkTogether() throws Exception {
        var admin = new Client(); admin.login("admin@fixcampus.local");
        var reporter = new Client(); reporter.login("reporter@fixcampus.local");
        var other = new Client(); other.login("other@fixcampus.local");
        var tech = new Client(); tech.login("technician@fixcampus.local");
        long category = admin.ok("GET","/categories",null).get(0).get("id").asLong();
        long area = admin.ok("GET","/areas",null).get(0).get("id").asLong();
        long technician = admin.ok("GET","/technicians",null).get(0).get("id").asLong();
        var input = Map.of("title","Proyector no enciende","description","No enciende al presionar el botón","location","Aula 302", "categoryId",category,"areaId",area,"priority","HIGH");
        var report = reporter.ok("POST","/reports",input);
        String path = "/reports/" + report.get("id").asLong();
        assertThat(reporter.ok("GET","/reports?status=NEW&categoryId="+category+"&areaId="+area+"&priority=HIGH&q=proyector",null).size()).isEqualTo(1);
        var comment = reporter.ok("POST",path+"/comments",Map.of("body","Lo reporté al salir de clase"));
        String commentPath = path+"/comments/"+comment.get("id").asLong();
        assertThat(reporter.ok("PUT",commentPath,Map.of("body","Actualicé el detalle del reporte")).get("body").asText())
            .isEqualTo("Actualicé el detalle del reporte");
        reporter.ok("DELETE",commentPath,null);
        assertThat(other.call("GET",path,null).statusCode()).isEqualTo(403);
        assertThat(other.call("PUT",path,input).statusCode()).isEqualTo(403);
        assertThat(reporter.call("POST",path+"/assign",Map.of("technicianId",technician,"note","Asignar")).statusCode()).isEqualTo(403);
        assertThat(reporter.call("POST","/categories",Map.of("name","Forbidden","description","","active",true)).statusCode()).isEqualTo(403);
        admin.ok("POST",path+"/assign",Map.of("technicianId",technician,"note","Revisar aula"));
        assertThat(reporter.call("PUT",path,input).statusCode()).isEqualTo(409);
        assertThat(tech.call("POST",path+"/transition",Map.of("status","RESOLVED","note","Salto inválido")).statusCode()).isEqualTo(409);
        tech.ok("POST",path+"/transition",Map.of("status","IN_PROGRESS","note","En revisión"));
        assertThat(tech.call("POST",path+"/transition",Map.of("status","RESOLVED","note","")).statusCode()).isEqualTo(400);
        tech.ok("POST",path+"/transition",Map.of("status","RESOLVED","note","Cable reemplazado"));
        var closed = reporter.ok("POST",path+"/transition",Map.of("status","CLOSED","note","Confirmado"));
        assertThat(closed.get("status").asText()).isEqualTo("CLOSED");
        assertThat(closed.get("history").size()).isEqualTo(5);
        assertThat(closed.get("notifications").get(0).get("status").asText()).isEqualTo("SKIPPED");
        var dashboard = reporter.ok("GET","/dashboard",null);
        assertThat(dashboard.get("total").asInt()).isEqualTo(1);
        assertThat(dashboard.get("closed").asInt()).isEqualTo(1);
        assertThat(dashboard.get("averageResolutionHours").asDouble()).isGreaterThanOrEqualTo(0);
        assertThat(other.ok("GET","/dashboard",null).get("total").asInt()).isZero();
    }

    @Test void adminCrudDisablesRecordsAndProtectsExistingSessions() throws Exception {
        var admin = new Client(); admin.login("admin@fixcampus.local");
        var created = admin.ok("POST","/users",Map.of("name","Nuevo usuario","email","new@fixcampus.local","password","OnlyTests-9284!","role","REPORTER","active",true));
        String path = "/users/" + created.get("id").asLong();
        var session = new Client(); session.login("new@fixcampus.local");
        assertThat(admin.call("POST","/users",Map.of("name","Duplicado","email","new@fixcampus.local","password","OnlyTests-9284!","role","REPORTER","active",true)).statusCode()).isEqualTo(409);
        admin.ok("DELETE",path,null);
        assertThat(session.call("GET","/reports",null).statusCode()).isEqualTo(401);
        var category = admin.ok("POST","/categories",Map.of("name","Prueba CRUD","description","Categoría de prueba","active",true));
        admin.ok("PUT","/categories/"+category.get("id").asLong(),Map.of("name","Editada","description","Cambio","active",true));
        admin.ok("DELETE","/categories/"+category.get("id").asLong(),null);
        var categories = admin.ok("GET","/categories",null);
        boolean foundInactive = false;
        for(var item : categories) if(item.get("id").equals(category.get("id"))) foundInactive = !item.get("active").asBoolean();
        assertThat(foundInactive).isTrue();
    }
}
