package com.example.gameorder.utils;

import com.example.gameorder.payloads.request.PaginationRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {
    public static Pageable getPageable(PaginationRequest request) {
        Integer page = (request.getPage() != null) ? request.getPage() - 1 : 0;
        Integer size = (request.getSize() != null) ? request.getSize() : 10;

        Sort.Direction sortDirection = (request.getSortDirection() != null) ?
                request.getSortDirection() : Sort.Direction.ASC;

        String sortField = (request.getSortField() != null) ?
                request.getSortField().getValue() : "id";


        return PageRequest.of(page, size, sortDirection, sortField);
    }
}
