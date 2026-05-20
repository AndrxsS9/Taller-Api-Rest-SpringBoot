package com.biblioteca.service.impl;

import com.biblioteca.dto.*;
import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.Estudiante;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Profesor;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.EjemplarRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.PrestamoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository,
                               UsuarioRepository usuarioRepository,
                               EjemplarRepository ejemplarRepository,
                               LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    public PrestamoResponse crearPrestamo(PrestamoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("No se puede registrar el préstamo: Usuario no encontrado con id: " + request.getUsuarioId()));

        Ejemplar ejemplar = ejemplarRepository.findById(request.getEjemplarId())
                .orElseThrow(() -> new RuntimeException("No se puede registrar el préstamo: Ejemplar no encontrado con id: " + request.getEjemplarId()));

        if (!"DISPONIBLE".equalsIgnoreCase(ejemplar.getEstado())) {
            throw new RuntimeException("No se puede registrar el préstamo: El ejemplar no está disponible (Estado actual: " + ejemplar.getEstado() + ")");
        }

        // Llamar al método del modelo
        ejemplar.marcarPrestado();
        ejemplarRepository.save(ejemplar);

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuarioId(request.getUsuarioId());
        prestamo.setEjemplarId(request.getEjemplarId());
        prestamo.setFechaPrestamo(LocalDate.now());
        
        if (request.getFechaDevolucionEsperada() != null) {
            prestamo.setFechaDevolucionEsperada(request.getFechaDevolucionEsperada());
        } else {
            prestamo.setFechaDevolucionEsperada(LocalDate.now().plusDays(14));
        }
        prestamo.setFechaDevolucionReal(null);
        prestamo.setEstado("ACTIVO");

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
        return mapToResponse(prestamoGuardado, usuario, ejemplar);
    }

    @Override
    public PrestamoResponse devolverPrestamo(String id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));

        if ("DEVUELTO".equalsIgnoreCase(prestamo.getEstado())) {
            throw new RuntimeException("El préstamo con id: " + id + " ya ha sido devuelto anteriormente.");
        }

        Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getEjemplarId()).orElse(null);
        if (ejemplar != null) {
            ejemplar.marcarDisponible();
            ejemplarRepository.save(ejemplar);
        }

        // Llamar al método del modelo
        prestamo.cerrarPrestamo();

        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);
        Usuario usuario = usuarioRepository.findById(prestamo.getUsuarioId()).orElse(null);

        return mapToResponse(prestamoActualizado, usuario, ejemplar);
    }

    @Override
    public void eliminarPrestamo(String id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));

        if ("ACTIVO".equalsIgnoreCase(prestamo.getEstado())) {
            Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getEjemplarId()).orElse(null);
            if (ejemplar != null) {
                ejemplar.marcarDisponible();
                ejemplarRepository.save(ejemplar);
            }
        }
        prestamoRepository.deleteById(id);
    }

    @Override
    public PrestamoResponse consultarPrestamo(String id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));
        return mapToResponse(prestamo);
    }

    @Override
    public List<PrestamoResponse> listarPrestamos() {
        return prestamoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrestamoResponse> listarPrestamosPorUsuario(String usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
        return prestamoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrestamoResponse> listarPrestamosPorEjemplar(String ejemplarId) {
        if (!ejemplarRepository.existsById(ejemplarId)) {
            throw new RuntimeException("Ejemplar no encontrado con id: " + ejemplarId);
        }
        return prestamoRepository.findByEjemplarId(ejemplarId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PrestamoResponse mapToResponse(Prestamo prestamo) {
        Usuario usuario = usuarioRepository.findById(prestamo.getUsuarioId()).orElse(null);
        Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getEjemplarId()).orElse(null);
        return mapToResponse(prestamo, usuario, ejemplar);
    }

    private PrestamoResponse mapToResponse(Prestamo prestamo, Usuario usuario, Ejemplar ejemplar) {
        UsuarioResponse usuarioResponse = null;
        if (usuario != null) {
            usuarioResponse = new UsuarioResponse();
            usuarioResponse.setId(usuario.getId());
            usuarioResponse.setNombre(usuario.getNombre());
            usuarioResponse.setCorreo(usuario.getCorreo());
            usuarioResponse.setTipoUsuario(usuario.getTipoUsuario());

            if (usuario instanceof Estudiante) {
                usuarioResponse.setCodigoEstudiante(((Estudiante) usuario).getCodigoEstudiante());
                usuarioResponse.setPrograma(((Estudiante) usuario).getPrograma());
            } else if (usuario instanceof Profesor) {
                usuarioResponse.setCodigoProfesor(((Profesor) usuario).getCodigoProfesor());
                usuarioResponse.setFacultad(((Profesor) usuario).getFacultad());
            } else if (usuario instanceof Bibliotecario) {
                usuarioResponse.setCodigoEmpleado(((Bibliotecario) usuario).getCodigoEmpleado());
                usuarioResponse.setTurno(((Bibliotecario) usuario).getTurno());
            }
        }

        EjemplarResponse ejemplarResponse = null;
        if (ejemplar != null) {
            Libro libro = libroRepository.findById(ejemplar.getLibroId()).orElse(null);
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
            ejemplarResponse = new EjemplarResponse(
                    ejemplar.getId(),
                    ejemplar.getCodigoEjemplar(),
                    ejemplar.getEstado(),
                    ejemplar.getUbicacion(),
                    libroResponse
            );
        }

        return new PrestamoResponse(
                prestamo.getId(),
                usuarioResponse,
                ejemplarResponse,
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucionEsperada(),
                prestamo.getFechaDevolucionReal(),
                prestamo.getEstado(),
                prestamo.calcularMulta()
        );
    }
}
