package com.dispositivos.device.infrastructure.adapter.in.rest.mapper;

import com.dispositivos.device.domain.model.Device;
import com.dispositivos.device.infrastructure.adapter.in.rest.dto.DeviceResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class DeviceMapper {

    private DeviceMapper() {
    }

    public static DeviceResponse toResponse(Device domain) {
        if (domain == null) {
            return null;
        }
        DeviceResponse dto = new DeviceResponse();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        dto.setBrandId(domain.getBrandId());
        dto.setDeviceTypeId(domain.getDeviceTypeId());
        dto.setReleaseDate(domain.getReleaseDate());
        dto.setImageUrl(domain.getImageUrl());
        dto.setImageUrls(domain.getImageUrls() != null
                ? List.copyOf(domain.getImageUrls())
                : Collections.emptyList());
        return dto;
    }

    public static List<DeviceResponse> toResponseList(List<Device> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(DeviceMapper::toResponse)
                .collect(Collectors.toList());
    }
}
