package com.hayagou.myapp.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    @ApiModelProperty(value = "오류 정보")
    private ErrorResponseInfo error;


}
