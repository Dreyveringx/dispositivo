package com.dispositivos.catalog.infrastructure.adapter.in.rest;

import com.dispositivos.catalog.application.ports.in.DeviceTypeUseCases;
import com.dispositivos.catalog.domain.model.DeviceType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-types")
@CrossOrigin(origins = "*")
public class DeviceTypeRestController {

    private final DeviceTypeUseCases deviceTypeUseCases;

    public DeviceTypeRestController(DeviceTypeUseCases deviceTypeUseCases) {
        this.deviceTypeUseCases = deviceTypeUseCases;
    }

    @PostMapping
    public ResponseEntity<DeviceType> create(@RequestBody DeviceTypeRequest request) {
        DeviceType created = deviceTypeUseCases.create(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<DeviceType>> findAll() {
        return ResponseEntity.ok(deviceTypeUseCases.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceType> findById(@PathVariable Long id) {
        return deviceTypeUseCases.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceType> update(@PathVariable Long id, @RequestBody DeviceTypeRequest request) {
        try {
            DeviceType updated = deviceTypeUseCases.update(id, request.getName(), request.getDescription());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceTypeUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public static class DeviceTypeRequest {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
