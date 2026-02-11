package com.dispositivos.device.application.ports.in;

import java.time.LocalDate;
import java.util.List;

public record DeviceCommand(
        String name,
        String description,
        Long brandId,
        Long deviceTypeId,
        LocalDate releaseDate,
        String imageUrl,
        List<String> imageUrls
) {
}
