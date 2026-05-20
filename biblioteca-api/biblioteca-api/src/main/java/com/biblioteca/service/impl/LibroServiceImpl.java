package com.biblioteca.service.impl;

import com.biblioteca.dto.LibroRequest;
import com.biblioteca.dto.LibroResponse;
import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.service.LibroService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public LibroResponse crearLibro(LibroRequest request) {
        Libro libro = new Libro();
        libro.setIsbn(request.getIsbn());
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setAnioPublicacion(request.getAnioPublicacion());
        libro.setCategoria(request.getCategoria());

        Libro libroGuardado = libroRepository.save(libro);
        return mapToResponse(libroGuardado);
    }

    @Override
    public LibroResponse actualizarLibro(String id, LibroRequest request) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));

        libro.setIsbn(request.getIsbn());
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setAnioPublicacion(request.getAnioPublicacion());
        libro.setCategoria(request.getCategoria());

        Libro libroActualizado = libroRepository.save(libro);
        return mapToResponse(libroActualizado);
    }

    @Override
    public void eliminarLibro(String id) {
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        libroRepository.deleteById(id);
    }

    @Override
    public LibroResponse consultarLibro(String id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
        return mapToResponse(libro);
    }

    @Override
    public List<LibroResponse> listarLibros() {
        return libroRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private LibroResponse mapToResponse(Libro libro) {
        return new LibroResponse(
                libro.getId(),
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAnioPublicacion(),
                libro.getCategoria()
        );
    }
}
