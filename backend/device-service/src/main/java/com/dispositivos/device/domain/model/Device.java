package com.dispositivos.device.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
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
