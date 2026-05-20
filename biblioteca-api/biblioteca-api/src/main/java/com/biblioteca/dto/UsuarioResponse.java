package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private String id;
    private String nombre;
    private String correo;
    private String tipoUsuario;

    // Estudiante
    private String codigoEstudiante;
    private String programa;

    // Profesor
    private String codigoProfesor;
    private String facultad;

    // Bibliotecario
    private String codigoEmpleado;
    private String turno;
}
