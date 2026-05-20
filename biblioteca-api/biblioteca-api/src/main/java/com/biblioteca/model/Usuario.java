package com.biblioteca.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    private String nombre;
    private String correo;
    private String tipoUsuario; // "ESTUDIANTE", "PROFESOR", "BIBLIOTECARIO"

    public Usuario(String id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public void consultarPrestamos() {
        // Método de comportamiento del modelo conceptual
    }
}
