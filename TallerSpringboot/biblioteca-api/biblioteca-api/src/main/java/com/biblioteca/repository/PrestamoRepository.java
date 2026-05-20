package com.biblioteca.repository;

import com.biblioteca.model.Prestamo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends MongoRepository<Prestamo, String> {

    List<Prestamo> findByUsuarioId(String usuarioId);

    List<Prestamo> findByEjemplarId(String ejemplarId);
}
