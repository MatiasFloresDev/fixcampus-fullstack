package pe.edu.upc.fixcampus.fixcampus.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Long idUbicacion;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String campus;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String edificio;

    private Integer piso;

    @Size(max = 150)
    @Column(length = 150)
    private String zona;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String tipo;

    public Ubicacion() {
    }

    public Long getIdUbicacion() {
        return idUbicacion;
    }
    public void setIdUbicacion(Long idUbicacion) {
        this.idUbicacion = idUbicacion;
    }
    public String getCampus() {
        return campus;
    }
    public void setCampus(String campus) {
        this.campus = campus;
    }
    public String getEdificio() {
        return edificio;
    }
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }
    public Integer getPiso() {
        return piso;
    }
    public void setPiso(Integer piso) {
        this.piso = piso;
    }
    public String getZona() {
        return zona;
    }
    public void setZona(String zona) {
        this.zona = zona;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
