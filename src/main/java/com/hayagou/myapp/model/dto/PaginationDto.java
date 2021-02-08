package com.hayagou.myapp.model.dto;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
public class PaginationDto<T> {
    List<T> items;
    int totalPage;
    int totalCount;
    int currentPage;
}
