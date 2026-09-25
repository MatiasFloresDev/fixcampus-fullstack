package pe.edu.upc.fixcampus.fixcampus.entities;
import jakarta.persistence.*;
@Entity
@Table(name="categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    public Categoria(){

    }

    public Long getIdCategoria() { return idCategoria; }

    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
