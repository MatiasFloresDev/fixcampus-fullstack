package pe.edu.upc.fixcampus.fixcampus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiSwaggerTests {

    @Value("${local.server.port}")
    private int port;

    @Test
    void swaggerMuestraLasOchoTablasYUsuariosSinContrasena() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String base = "http://localhost:" + port;

        HttpResponse<String> inicio = client.send(
                HttpRequest.newBuilder(URI.create(base + "/")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertThat(inicio.statusCode()).isEqualTo(302);
        assertThat(inicio.headers().firstValue("Location").orElseThrow())
                .endsWith("/swagger-ui/index.html");

        HttpResponse<String> docs = client.send(
                HttpRequest.newBuilder(URI.create(base + "/v3/api-docs")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertThat(docs.statusCode()).isEqualTo(200);
        for (String ruta : new String[] {"/api/categories", "/api/reports", "/api/users",
                "/api/roles", "/api/locations", "/api/comments",
                "/api/attachments", "/api/recomendaciones"}) {
            assertThat(docs.body()).contains(ruta);
        }
        assertThat(docs.body()).contains("Contar incidencias por usuario y mes");
        assertThat(docs.body()).contains("Nombre exacto de la categoría; consulta con JOIN");
        assertThat(docs.body()).contains("/api/reports/estadisticas/por-campus");
        assertThat(docs.body()).contains("/api/comments/estadisticas/por-reporte");

        HttpResponse<String> login = client.send(
                HttpRequest.newBuilder(URI.create(base + "/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"correo\":\"admin@fixcampus.com\",\"password\":\"admin123\"}"))
                        .build(), HttpResponse.BodyHandlers.ofString());
        assertThat(login.statusCode()).isEqualTo(200);
        Matcher token = Pattern.compile("\"token\":\"([^\"]+)\"").matcher(login.body());
        assertThat(token.find()).isTrue();

        HttpResponse<String> usuarios = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users"))
                        .header("Authorization", "Bearer " + token.group(1))
                        .GET().build(), HttpResponse.BodyHandlers.ofString());
        assertThat(usuarios.statusCode()).isEqualTo(200);
        assertThat(usuarios.body()).contains("admin@fixcampus.com");
        assertThat(usuarios.body()).doesNotContain("contrasenaHash", "admin123");

        HttpResponse<String> cantidad = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users/count"))
                        .header("Authorization", "Bearer " + token.group(1))
                        .GET().build(), HttpResponse.BodyHandlers.ofString());
        assertThat(cantidad.statusCode()).isEqualTo(200);
        assertThat(Long.parseLong(cantidad.body())).isGreaterThanOrEqualTo(2);
    }
}
