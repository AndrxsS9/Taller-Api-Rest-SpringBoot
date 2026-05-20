package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    private String nombre;
    private String correo;
    private String tipoUsuario; // "ESTUDIANTE", "PROFESOR", "BIBLIOTECARIO"

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
