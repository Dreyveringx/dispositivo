package com.dispositivos.catalog.infrastructure.adapter.in.rest.mapper;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static <S, T> List<T> toResponseList(List<S> list, Function<S, T> mapper) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream().map(mapper).collect(Collectors.toList());
    }
}
