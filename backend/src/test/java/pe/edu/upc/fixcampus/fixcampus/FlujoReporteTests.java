package pe.edu.upc.fixcampus.fixcampus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.upc.fixcampus.fixcampus.repositories.ReporteRepository;
import pe.edu.upc.fixcampus.fixcampus.repositories.UsuarioRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlujoReporteTests {

    @Value("${local.server.port}")
    private int port;

    private final ObjectMapper json = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private Long reporteCreado;
    private String correoCreado;

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    void limpiarDatosDePrueba() {
        if (reporteCreado != null) reporteRepository.deleteById(reporteCreado);
        if (correoCreado != null) usuarioRepository.findByCorreo(correoCreado)
                .ifPresent(usuarioRepository::delete);
    }

    @Test
    void usuarioSeRegistraYVeSuReporteGuardado() throws Exception {
        String base = "http://localhost:" + port;
        String correo = "prueba" + System.nanoTime() + "@example.com";
        correoCreado = correo;
        String registro = "{\"nombre\":\"Ana\",\"apellido\":\"Torres\",\"correo\":\""
                + correo + "\",\"password\":\"clave123\"}";
        assertThat(enviar(base + "/registro", registro, null).statusCode()).isEqualTo(201);

        String login = "{\"correo\":\"" + correo + "\",\"password\":\"clave123\"}";
        HttpResponse<String> respuestaLogin = enviar(base + "/login", login, null);
        assertThat(respuestaLogin.statusCode()).isEqualTo(200);
        JsonNode sesion = json.readTree(respuestaLogin.body());
        String token = sesion.get("token").asText();
        assertThat(sesion.get("idUsuario").asLong()).isPositive();

        JsonNode categorias = json.readTree(consultar(base + "/api/categories", token).body());
        JsonNode ubicaciones = json.readTree(consultar(base + "/api/locations", token).body());
        assertThat(categorias.size()).isPositive();
        assertThat(ubicaciones.size()).isPositive();

        String reporte = "{\"categoriaId\":" + categorias.get(0).get("idCategoria").asLong()
                + ",\"ubicacionId\":" + ubicaciones.get(0).get("idUbicacion").asLong()
                + ",\"titulo\":\"Proyector averiado\",\"descripcion\":\"No enciende\""
                + ",\"detalleUbicacion\":\"Aula 301\",\"prioridad\":\"MEDIA\",\"estado\":\"ABIERTO\"}";
        HttpResponse<String> respuestaReporte = enviar(base + "/api/reports", reporte, token);
        assertThat(respuestaReporte.statusCode()).isEqualTo(201);
        reporteCreado = json.readTree(respuestaReporte.body()).get("idReporte").asLong();

        HttpResponse<String> misReportes = consultar(base + "/api/reports/mis-reportes", token);
        assertThat(misReportes.statusCode()).isEqualTo(200);
        assertThat(misReportes.body()).contains("Proyector averiado", "Aula 301");
    }

    private HttpResponse<String> enviar(String url, String body, String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (token != null) request.header("Authorization", "Bearer " + token);
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> consultar(String url, String token) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", "Bearer " + token).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }
}
