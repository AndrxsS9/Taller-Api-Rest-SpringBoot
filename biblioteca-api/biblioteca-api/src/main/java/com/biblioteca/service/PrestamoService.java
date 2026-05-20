package com.biblioteca.service;

import com.biblioteca.dto.PrestamoRequest;
import com.biblioteca.dto.PrestamoResponse;

import java.util.List;

public interface PrestamoService {

    PrestamoResponse crearPrestamo(PrestamoRequest request);

    PrestamoResponse devolverPrestamo(String id);

    void eliminarPrestamo(String id);

    PrestamoResponse consultarPrestamo(String id);

    List<PrestamoResponse> listarPrestamos();

    List<PrestamoResponse> listarPrestamosPorUsuario(String usuarioId);

    List<PrestamoResponse> listarPrestamosPorEjemplar(String ejemplarId);
}
