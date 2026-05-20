package com.biblioteca.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Estudiante extends Usuario {

    private String codigoEstudiante;
    private String programa;

    public Estudiante(String id, String nombre, String correo, String codigoEstudiante, String programa) {
        super(id, nombre, correo);
        setTipoUsuario("ESTUDIANTE");
        this.codigoEstudiante = codigoEstudiante;
        this.programa = programa;
    }

    public void solicitarPrestamo() {
        // Método de comportamiento del modelo conceptual
    }
}
