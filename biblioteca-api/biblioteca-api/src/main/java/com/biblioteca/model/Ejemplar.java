package com.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ejemplares")
public class Ejemplar {

    @Id
    private String id;

    private String codigoEjemplar;
    private String estado; // "DISPONIBLE", "PRESTADO", "MANTENIMIENTO"
    private String ubicacion;
    private String libroId;

    public Ejemplar(String codigoEjemplar, String estado, String ubicacion) {
        this.codigoEjemplar = codigoEjemplar;
        this.estado = estado;
        this.ubicacion = ubicacion;
    }

    public void marcarDisponible() {
        this.estado = "DISPONIBLE";
      }

      public void marcarPrestado() {
          this.estado = "PRESTADO";
      }
}
