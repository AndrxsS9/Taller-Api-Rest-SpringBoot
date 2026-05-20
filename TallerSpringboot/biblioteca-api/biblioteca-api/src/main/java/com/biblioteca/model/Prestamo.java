package com.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "prestamos")
public class Prestamo {

    @Id
    private String id;

    private String usuarioId;
    private String ejemplarId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private String estado; // "ACTIVO", "DEVUELTO", "ATRASADO"

    public Prestamo(String id, LocalDate fechaPrestamo, LocalDate fechaDevolucionEsperada, LocalDate fechaDevolucionReal, String estado) {
        this.id = id;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estado = estado;
    }

    public double calcularMulta() {
        if (fechaDevolucionReal != null && fechaDevolucionReal.isAfter(fechaDevolucionEsperada)) {
            long diasRetraso = java.time.temporal.ChronoUnit.DAYS.between(fechaDevolucionEsperada, fechaDevolucionReal);
            return diasRetraso * 2000.0; // Ejemplo: 2000 COP multa por día de retraso
        }
        if (fechaDevolucionReal == null && LocalDate.now().isAfter(fechaDevolucionEsperada)) {
            long diasRetraso = java.time.temporal.ChronoUnit.DAYS.between(fechaDevolucionEsperada, LocalDate.now());
            return diasRetraso * 2000.0;
        }
        return 0.0;
    }

    public void cerrarPrestamo() {
        this.estado = "DEVUELTO";
        this.fechaDevolucionReal = LocalDate.now();
    }
}
