package pe.edu.upc.fixcampus.fixcampus.dtos;

public class ComentariosPorReporteDTO {
    private Long reporteId;
    private String tituloReporte;
    private Long cantidad;

    public ComentariosPorReporteDTO(Long reporteId, String tituloReporte, Long cantidad) {
        this.reporteId = reporteId;
        this.tituloReporte = tituloReporte;
        this.cantidad = cantidad;
    }

    public Long getReporteId() { return reporteId; }
    public String getTituloReporte() { return tituloReporte; }
    public Long getCantidad() { return cantidad; }
}
