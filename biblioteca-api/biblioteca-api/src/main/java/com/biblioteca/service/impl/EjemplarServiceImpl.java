package com.biblioteca.service.impl;

import com.biblioteca.dto.EjemplarRequest;
import com.biblioteca.dto.EjemplarResponse;
import com.biblioteca.dto.LibroResponse;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.Libro;
import com.biblioteca.repository.EjemplarRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.service.EjemplarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EjemplarServiceImpl implements EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;

    public EjemplarServiceImpl(EjemplarRepository ejemplarRepository, LibroRepository libroRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    public EjemplarResponse crearEjemplar(EjemplarRequest request) {
        // Verificar que el libro asociado exista
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RuntimeException("No se puede crear el ejemplar: Libro no encontrado con id: " + request.getLibroId()));

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setCodigoEjemplar(request.getCodigoEjemplar());
        // Por defecto el estado al crear es DISPONIBLE si no se especifica
        ejemplar.setEstado(request.getEstado() != null ? request.getEstado().toUpperCase() : "DISPONIBLE");
        ejemplar.setUbicacion(request.getUbicacion());
        ejemplar.setLibroId(request.getLibroId());

        Ejemplar ejemplarGuardado = ejemplarRepository.save(ejemplar);
        return mapToResponse(ejemplarGuardado, libro);
    }

    @Override
    public EjemplarResponse actualizarEjemplar(String id, EjemplarRequest request) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con id: " + id));

        // Verificar que el libro asociado exista si se cambia
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RuntimeException("No se puede actualizar el ejemplar: Libro no encontrado con id: " + request.getLibroId()));

        ejemplar.setCodigoEjemplar(request.getCodigoEjemplar());
        ejemplar.setEstado(request.getEstado() != null ? request.getEstado().toUpperCase() : ejemplar.getEstado());
        ejemplar.setUbicacion(request.getUbicacion());
        ejemplar.setLibroId(request.getLibroId());

        Ejemplar ejemplarActualizado = ejemplarRepository.save(ejemplar);
        return mapToResponse(ejemplarActualizado, libro);
    }

    @Override
    public void eliminarEjemplar(String id) {
        if (!ejemplarRepository.existsById(id)) {
            throw new RuntimeException("Ejemplar no encontrado con id: " + id);
        }
        ejemplarRepository.deleteById(id);
    }

    @Override
    public EjemplarResponse consultarEjemplar(String id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con id: " + id));
        return mapToResponse(ejemplar);
    }

    @Override
    public List<EjemplarResponse> listarEjemplares() {
        return ejemplarRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EjemplarResponse> listarEjemplaresPorLibro(String libroId) {
        // Verificar que el libro exista
        if (!libroRepository.existsById(libroId)) {
            throw new RuntimeException("Libro no encontrado con id: " + libroId);
        }
        return ejemplarRepository.findByLibroId(libroId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares de mapeo
    private EjemplarResponse mapToResponse(Ejemplar ejemplar) {
        Libro libro = libroRepository.findById(ejemplar.getLibroId()).orElse(null);
        return mapToResponse(ejemplar, libro);
    }

    private EjemplarResponse mapToResponse(Ejemplar ejemplar, Libro libro) {
        LibroResponse libroResponse = null;
        if (libro != null) {
            libroResponse = new LibroResponse(
                    libro.getId(),
                    libro.getIsbn(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getAnioPublicacion(),
                    libro.getCategoria()
            );
        }
        return new EjemplarResponse(
                ejemplar.getId(),
                ejemplar.getCodigoEjemplar(),
                ejemplar.getEstado(),
                ejemplar.getUbicacion(),
                libroResponse
        );
    }
}
