package com.example.gameorder.payloads.request;

import com.example.gameorder.utils.SortField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data @SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class PaginationRequest {
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 5;

    @Builder.Default
    private SortField sortField = SortField.ORDERID;

    @Builder.Default
    private Sort.Direction sortDirection = Sort.Direction.ASC;
}