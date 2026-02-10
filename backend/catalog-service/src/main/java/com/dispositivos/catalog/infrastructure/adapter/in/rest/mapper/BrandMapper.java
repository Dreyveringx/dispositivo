package com.dispositivos.catalog.infrastructure.adapter.in.rest.mapper;

import com.dispositivos.catalog.domain.model.Brand;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.dto.BrandResponse;

import java.util.List;

public final class BrandMapper {

    private BrandMapper() {
    }

    public static BrandResponse toResponse(Brand domain) {
        if (domain == null) {
            return null;
        }
        BrandResponse dto = new BrandResponse();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        return dto;
    }

    public static List<BrandResponse> toResponseList(List<Brand> list) {
        return MapperUtils.toResponseList(list, BrandMapper::toResponse);
    }
}
