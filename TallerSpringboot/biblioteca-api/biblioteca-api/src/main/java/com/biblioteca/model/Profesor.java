package com.biblioteca.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Profesor extends Usuario {

    private String codigoProfesor;
    private String facultad;

    public Profesor(String id, String nombre, String correo, String codigoProfesor, String facultad) {
        super(id, nombre, correo);
        setTipoUsuario("PROFESOR");
        this.codigoProfesor = codigoProfesor;
        this.facultad = facultad;
    }

    public void solicitarPrestamo() {
        // Método de comportamiento del modelo conceptual
    }
}
