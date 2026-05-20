package com.biblioteca.service.impl;

import com.biblioteca.dto.UsuarioRequest;
import com.biblioteca.dto.UsuarioResponse;
import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Estudiante;
import com.biblioteca.model.Profesor;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        Usuario usuario;
        String tipo = request.getTipoUsuario() != null ? request.getTipoUsuario().toUpperCase() : "ESTUDIANTE";

        switch (tipo) {
            case "ESTUDIANTE":
                usuario = new Estudiante(
                        null,
                        request.getNombre(),
                        request.getCorreo(),
                        request.getCodigoEstudiante(),
                        request.getPrograma()
                );
                break;
            case "PROFESOR":
                usuario = new Profesor(
                        null,
                        request.getNombre(),
                        request.getCorreo(),
                        request.getCodigoProfesor(),
                        request.getFacultad()
                );
                break;
            case "BIBLIOTECARIO":
                usuario = new Bibliotecario(
                        null,
                        request.getNombre(),
                        request.getCorreo(),
                        request.getCodigoEmpleado(),
                        request.getTurno()
                );
                break;
            default:
                throw new RuntimeException("Tipo de usuario no válido: " + request.getTipoUsuario());
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return mapToResponse(usuarioGuardado);
    }

    @Override
    public UsuarioResponse actualizarUsuario(String id, UsuarioRequest request) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        String tipo = request.getTipoUsuario() != null ? request.getTipoUsuario().toUpperCase() : usuarioExistente.getTipoUsuario();

        Usuario usuarioActualizado;
        if (usuarioExistente.getTipoUsuario() != null && usuarioExistente.getTipoUsuario().equalsIgnoreCase(tipo)) {
            usuarioActualizado = usuarioExistente;
            usuarioActualizado.setNombre(request.getNombre());
            usuarioActualizado.setCorreo(request.getCorreo());

            if (usuarioActualizado instanceof Estudiante) {
                ((Estudiante) usuarioActualizado).setCodigoEstudiante(request.getCodigoEstudiante());
                ((Estudiante) usuarioActualizado).setPrograma(request.getPrograma());
            } else if (usuarioActualizado instanceof Profesor) {
                ((Profesor) usuarioActualizado).setCodigoProfesor(request.getCodigoProfesor());
                ((Profesor) usuarioActualizado).setFacultad(request.getFacultad());
            } else if (usuarioActualizado instanceof Bibliotecario) {
                ((Bibliotecario) usuarioActualizado).setCodigoEmpleado(request.getCodigoEmpleado());
                ((Bibliotecario) usuarioActualizado).setTurno(request.getTurno());
            }
        } else {
            switch (tipo) {
                case "ESTUDIANTE":
                    usuarioActualizado = new Estudiante(
                            id,
                            request.getNombre(),
                            request.getCorreo(),
                            request.getCodigoEstudiante(),
                            request.getPrograma()
                    );
                    break;
                case "PROFESOR":
                    usuarioActualizado = new Profesor(
                            id,
                            request.getNombre(),
                            request.getCorreo(),
                            request.getCodigoProfesor(),
                            request.getFacultad()
                    );
                    break;
                case "BIBLIOTECARIO":
                    usuarioActualizado = new Bibliotecario(
                            id,
                            request.getNombre(),
                            request.getCorreo(),
                            request.getCodigoEmpleado(),
                            request.getTurno()
                    );
                    break;
                default:
                    throw new RuntimeException("Tipo de usuario no válido: " + tipo);
            }
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuarioActualizado);
        return mapToResponse(usuarioGuardado);
    }

    @Override
    public void eliminarUsuario(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioResponse consultarUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return mapToResponse(usuario);
    }

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setCorreo(usuario.getCorreo());
        response.setTipoUsuario(usuario.getTipoUsuario());

        if (usuario instanceof Estudiante) {
            response.setCodigoEstudiante(((Estudiante) usuario).getCodigoEstudiante());
            response.setPrograma(((Estudiante) usuario).getPrograma());
        } else if (usuario instanceof Profesor) {
            response.setCodigoProfesor(((Profesor) usuario).getCodigoProfesor());
            response.setFacultad(((Profesor) usuario).getFacultad());
        } else if (usuario instanceof Bibliotecario) {
            response.setCodigoEmpleado(((Bibliotecario) usuario).getCodigoEmpleado());
            response.setTurno(((Bibliotecario) usuario).getTurno());
        }

        return response;
    }
}
