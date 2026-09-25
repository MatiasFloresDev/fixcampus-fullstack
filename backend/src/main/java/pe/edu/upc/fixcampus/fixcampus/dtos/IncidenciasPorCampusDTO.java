package pe.edu.upc.fixcampus.fixcampus.dtos;

public class IncidenciasPorCampusDTO {
    private String campus;
    private Long cantidad;

    public IncidenciasPorCampusDTO(String campus, Long cantidad) {
        this.campus = campus;
        this.cantidad = cantidad;
    }

    public String getCampus() { return campus; }
    public Long getCantidad() { return cantidad; }
}
