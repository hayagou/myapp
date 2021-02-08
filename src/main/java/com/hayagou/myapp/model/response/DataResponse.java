package com.hayagou.myapp.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse<T> {
    private T data;
}