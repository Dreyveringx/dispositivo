package com.dispositivos.device.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private Long id;
    private String name;
    private String description;
    private Long brandId;
    private Long deviceTypeId;
    private LocalDate releaseDate;
    private String imageUrl;
    private List<String> imageUrls = new ArrayList<>();

}
