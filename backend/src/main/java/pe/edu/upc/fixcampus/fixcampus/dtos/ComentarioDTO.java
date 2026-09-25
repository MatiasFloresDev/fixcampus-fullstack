package pe.edu.upc.fixcampus.fixcampus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ComentarioDTO {
    private Long idComentario;
    @NotNull private Long reporteId;
    @NotNull private Long usuarioId;
    @NotBlank private String textoComentario;
    private LocalDateTime fechaComentario;

    public Long getIdComentario() { return idComentario; }
    public void setIdComentario(Long idComentario) { this.idComentario = idComentario; }
    public Long getReporteId() { return reporteId; }
    public void setReporteId(Long reporteId) { this.reporteId = reporteId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getTextoComentario() { return textoComentario; }
    public void setTextoComentario(String textoComentario) { this.textoComentario = textoComentario; }
    public LocalDateTime getFechaComentario() { return fechaComentario; }
    public void setFechaComentario(LocalDateTime fechaComentario) { this.fechaComentario = fechaComentario; }
}
