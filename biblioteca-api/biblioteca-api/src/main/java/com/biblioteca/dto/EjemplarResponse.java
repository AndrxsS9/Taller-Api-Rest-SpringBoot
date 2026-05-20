package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarResponse {

    private String id;
    private String codigoEjemplar;
    private String estado;
    private String ubicacion;
    private LibroResponse libro;
}
