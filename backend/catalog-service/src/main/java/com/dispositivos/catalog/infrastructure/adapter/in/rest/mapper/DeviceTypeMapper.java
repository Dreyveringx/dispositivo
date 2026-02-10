package com.dispositivos.catalog.infrastructure.adapter.in.rest.mapper;

import com.dispositivos.catalog.domain.model.DeviceType;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.dto.DeviceTypeResponse;

import java.util.List;

public final class DeviceTypeMapper {

    private DeviceTypeMapper() {
    }

    public static DeviceTypeResponse toResponse(DeviceType domain) {
        if (domain == null) {
            return null;
        }
        DeviceTypeResponse dto = new DeviceTypeResponse();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        return dto;
    }

    public static List<DeviceTypeResponse> toResponseList(List<DeviceType> list) {
        return MapperUtils.toResponseList(list, DeviceTypeMapper::toResponse);
    }
}
