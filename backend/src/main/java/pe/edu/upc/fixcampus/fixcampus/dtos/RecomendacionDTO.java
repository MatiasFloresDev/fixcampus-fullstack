package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class RecomendacionDTO {
    private Long idRecomendacion;
    @NotNull private Long reporteId;
    @NotBlank @Size(max = 200) private String tituloSugerido;
    @NotBlank private String resumen;
    @NotBlank @Size(max = 30) private String prioridadSugerida;
    @NotBlank private String justificacion;
    private LocalDateTime fechaRecomendacion;

    public Long getIdRecomendacion() { return idRecomendacion; }
    public void setIdRecomendacion(Long idRecomendacion) { this.idRecomendacion = idRecomendacion; }
    public Long getReporteId() { return reporteId; }
    public void setReporteId(Long reporteId) { this.reporteId = reporteId; }
    public String getTituloSugerido() { return tituloSugerido; }
    public void setTituloSugerido(String tituloSugerido) { this.tituloSugerido = tituloSugerido; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getPrioridadSugerida() { return prioridadSugerida; }
    public void setPrioridadSugerida(String prioridadSugerida) { this.prioridadSugerida = prioridadSugerida; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
    public LocalDateTime getFechaRecomendacion() { return fechaRecomendacion; }
    public void setFechaRecomendacion(LocalDateTime fechaRecomendacion) { this.fechaRecomendacion = fechaRecomendacion; }
}
