package com.biblioteca.controller;

import com.biblioteca.dto.EjemplarRequest;
import com.biblioteca.dto.EjemplarResponse;
import com.biblioteca.service.EjemplarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejemplares")
public class EjemplarController {

    private final EjemplarService ejemplarService;

    public EjemplarController(EjemplarService ejemplarService) {
        this.ejemplarService = ejemplarService;
    }

    @PostMapping
    public ResponseEntity<EjemplarResponse> crearEjemplar(@RequestBody EjemplarRequest request) {
        EjemplarResponse response = ejemplarService.crearEjemplar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EjemplarResponse>> listarEjemplares() {
        List<EjemplarResponse> ejemplares = ejemplarService.listarEjemplares();
        return ResponseEntity.ok(ejemplares);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EjemplarResponse> consultarEjemplar(@PathVariable String id) {
        EjemplarResponse response = ejemplarService.consultarEjemplar(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EjemplarResponse> actualizarEjemplar(
            @PathVariable String id,
            @RequestBody EjemplarRequest request) {
        EjemplarResponse response = ejemplarService.actualizarEjemplar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplar(@PathVariable String id) {
        ejemplarService.eliminarEjemplar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<EjemplarResponse>> listarEjemplaresPorLibro(@PathVariable String libroId) {
        List<EjemplarResponse> ejemplares = ejemplarService.listarEjemplaresPorLibro(libroId);
        return ResponseEntity.ok(ejemplares);
    }
}
