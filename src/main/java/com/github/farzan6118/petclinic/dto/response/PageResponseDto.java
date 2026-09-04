package com.github.farzan6118.petclinic.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        Integer pageNumber,
        Integer pageSize,
        Boolean first,
        Boolean last,
        Long totalElements,
        Integer totalPages,
        Integer size
) {

    public static <T> PageResponseDto<T> from(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize()
        );
    }
}