package pe.edu.upc.fixcampus.fixcampus.dtos;

import java.time.LocalDateTime;

// Datos públicos del usuario. La contraseña y su hash nunca salen en la respuesta.
public class UsuarioDTO {
    private Long idUsuario;
    private Long rolId;
    private String nombre;
    private String apellido;
    private String correo;
    private String estado;
    private LocalDateTime fechaRegistro;

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
