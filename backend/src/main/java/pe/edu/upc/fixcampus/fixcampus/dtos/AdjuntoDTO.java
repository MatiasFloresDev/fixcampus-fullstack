package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class AdjuntoDTO {
    private Long idAdjunto;
    @NotNull private Long reporteId;
    @NotBlank @Size(max = 255) private String nombreArchivo;
    @NotBlank @Size(max = 500) private String urlArchivo;
    @Size(max = 100) private String tipoArchivo;
    private LocalDateTime fechaSubida;

    public Long getIdAdjunto() { return idAdjunto; }
    public void setIdAdjunto(Long idAdjunto) { this.idAdjunto = idAdjunto; }
    public Long getReporteId() { return reporteId; }
    public void setReporteId(Long reporteId) { this.reporteId = reporteId; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}
