package com.biblioteca.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Bibliotecario extends Usuario {

    private String codigoEmpleado;
    private String turno;

    public Bibliotecario(String id, String nombre, String correo, String codigoEmpleado, String turno) {
        super(id, nombre, correo);
        setTipoUsuario("BIBLIOTECARIO");
        this.codigoEmpleado = codigoEmpleado;
        this.turno = turno;
    }

    public void registrarPrestamo() {
        // Método de comportamiento del modelo conceptual
    }

    public void registrarDevolucion() {
        // Método de comportamiento del modelo conceptual
    }
}
