package com.dispositivos.device.infrastructure.adapter.in.rest;

import com.dispositivos.device.application.ports.in.DeviceUseCases;
import com.dispositivos.device.domain.model.Device;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador de entrada REST para dispositivos.
 * GET /api/devices?name=...&brandId=...&deviceTypeId=...&sortByReleaseDate=true
 */
@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class DeviceRestController {

    private final DeviceUseCases deviceUseCases;

    public DeviceRestController(DeviceUseCases deviceUseCases) {
        this.deviceUseCases = deviceUseCases;
    }

    @PostMapping
    public ResponseEntity<Device> create(@RequestBody DeviceRequest request) {
        Device created = deviceUseCases.create(
                request.getName(),
                request.getDescription(),
                request.getBrandId(),
                request.getDeviceTypeId(),
                request.getReleaseDate(),
                request.getImageUrl(),
                request.getImageUrls()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Device>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long deviceTypeId,
            @RequestParam(required = false, defaultValue = "true") boolean sortByReleaseDate
    ) {
        List<Device> list = deviceUseCases.findFiltered(name, brandId, deviceTypeId, sortByReleaseDate);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getById(@PathVariable Long id) {
        return deviceUseCases.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> update(@PathVariable Long id, @RequestBody DeviceRequest request) {
        try {
            Device updated = deviceUseCases.update(id,
                    request.getName(),
                    request.getDescription(),
                    request.getBrandId(),
                    request.getDeviceTypeId(),
                    request.getReleaseDate(),
                    request.getImageUrl(),
                    request.getImageUrls());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public static class DeviceRequest {
        private String name;
        private String description;
        private Long brandId;
        private Long deviceTypeId;
        private java.time.LocalDate releaseDate;
        private String imageUrl;
        private List<String> imageUrls;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getBrandId() { return brandId; }
        public void setBrandId(Long brandId) { this.brandId = brandId; }
        public Long getDeviceTypeId() { return deviceTypeId; }
        public void setDeviceTypeId(Long deviceTypeId) { this.deviceTypeId = deviceTypeId; }
        public java.time.LocalDate getReleaseDate() { return releaseDate; }
        public void setReleaseDate(java.time.LocalDate releaseDate) { this.releaseDate = releaseDate; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public List<String> getImageUrls() { return imageUrls; }
        public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    }
}
