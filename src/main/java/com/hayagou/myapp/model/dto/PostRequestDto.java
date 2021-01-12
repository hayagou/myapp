package com.hayagou.myapp.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDto {
    @ApiModelProperty(value = "내용", required = true)
    @NotEmpty
    private String title;
    @NotEmpty
    @ApiModelProperty(value = "제목", required = true)
    private String content;


}
