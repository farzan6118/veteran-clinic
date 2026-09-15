package com.github.farzan6118.petclinic.common.dto.response;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        boolean first,
        boolean last,
        long totalElements,
        int totalPages,
        int size
) {
}
