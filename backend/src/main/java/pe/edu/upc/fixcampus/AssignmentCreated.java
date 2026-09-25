package pe.edu.upc.fixcampus;

/** Datos mínimos para notificar después del commit, sin transportar entidades JPA. */
public record AssignmentCreated(Long reportId,String recipient,String title) { }
