package com.hayagou.myapp.model.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponseDto {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;
}
