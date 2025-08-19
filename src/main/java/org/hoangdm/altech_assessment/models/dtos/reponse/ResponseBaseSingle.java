package org.hoangdm.altech_assessment.models.dtos.reponse;

import java.util.List;

public record ResponseBaseSingle<T> (
        Integer code,
        String message,
        T data
){
}
