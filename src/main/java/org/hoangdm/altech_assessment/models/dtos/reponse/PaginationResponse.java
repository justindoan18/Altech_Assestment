package org.hoangdm.altech_assessment.models.dtos.reponse;

import java.util.List;

public record PaginationResponse<T>(
        Integer code,
        String message,
        List<T> data,
        int currentPage,
        int currentSize,
        int totalPage,
        int totalElement
){
}
