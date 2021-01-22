package com.hayagou.myapp.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDto {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
