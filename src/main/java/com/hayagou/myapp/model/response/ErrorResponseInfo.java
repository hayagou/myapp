package com.hayagou.myapp.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseInfo {
    @ApiModelProperty(value = "에러 코드")
    private int code;

    @ApiModelProperty(value = "에러 메시지")
    private String msg;

    private String info;
}
