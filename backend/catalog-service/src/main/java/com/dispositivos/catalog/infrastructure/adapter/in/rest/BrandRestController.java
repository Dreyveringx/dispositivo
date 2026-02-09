package com.dispositivos.catalog.infrastructure.adapter.in.rest;

import com.dispositivos.catalog.application.ports.in.BrandUseCases;
import com.dispositivos.catalog.domain.model.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador de entrada REST: expone el puerto BrandUseCases como API HTTP.
 */
@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "*")
public class BrandRestController {

    private final BrandUseCases brandUseCases;

    public BrandRestController(BrandUseCases brandUseCases) {
        this.brandUseCases = brandUseCases;
    }

    @PostMapping
    public ResponseEntity<Brand> create(@RequestBody BrandRequest request) {
        Brand created = brandUseCases.create(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Brand>> findAll() {
        return ResponseEntity.ok(brandUseCases.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> findById(@PathVariable Long id) {
        return brandUseCases.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> update(@PathVariable Long id, @RequestBody BrandRequest request) {
        try {
            Brand updated = brandUseCases.update(id, request.getName(), request.getDescription());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public static class BrandRequest {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
