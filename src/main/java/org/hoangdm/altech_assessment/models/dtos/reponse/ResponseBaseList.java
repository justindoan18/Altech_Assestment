package org.hoangdm.altech_assessment.models.dtos.reponse;

import java.util.List;

public record ResponseBaseList<T> (
        Integer code,
        String message,
        List<T> data
){
}

