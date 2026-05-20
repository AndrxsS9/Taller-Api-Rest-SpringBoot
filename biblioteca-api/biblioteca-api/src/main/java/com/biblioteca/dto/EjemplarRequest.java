package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarRequest {

    private String codigoEjemplar;
    private String estado; // e.g. "DISPONIBLE", "PRESTADO", "MANTENIMIENTO"
    private String ubicacion;
    private String libroId;
}
